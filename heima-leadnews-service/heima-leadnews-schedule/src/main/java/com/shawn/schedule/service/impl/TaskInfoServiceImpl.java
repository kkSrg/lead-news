package com.shawn.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.heima.common.constants.ScheduleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.common.enums.TaskTypeEnum;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.schedule.entities.TaskInfo;
import com.heima.model.schedule.entities.TaskInfoLogs;
import com.shawn.schedule.mapper.TaskInfoLogsMapper;
import com.shawn.schedule.mapper.TaskInfoMapper;
import com.shawn.schedule.service.TaskInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author shawn
 * @date 2023年 01月 16日 14:36
 */
@Service
@Slf4j
public class TaskInfoServiceImpl implements TaskInfoService {

    @Resource
    private CacheService cacheService;

    @Resource
    private TaskInfoMapper taskInfoMapper;

    @Resource
    private TaskInfoLogsMapper taskInfoLogsMapper;

    /**
     * 添加任务
     *
     * @param task 任务
     */
    @Override
    @Transactional
    public void addTask(Task task) {
        log.info("正在添加任务,任务数据={}", task);
        if (Objects.isNull(task)) {
            log.error("task NPE error -- TaskInfoServiceImpl");
            return;
        }
        //将任务存储到数据库
        boolean DBResult = saveInDB(task);
        //将任务添加到redis
        if (DBResult) {
            saveInRedis(task);
        }
    }

    /**
     * 取消任务
     *
     * @param taskId 任务id
     */
    @Override
    public void cancel(Long taskId) {
        log.info("正在删除任务数据,任务id={}", taskId);
        //删除数据库任务数据
        //修改数据库任务日志状态
        Task task = upDateInDB(taskId, ScheduleConstants.CANCELLED);
        //删除redis数据
        if (Objects.nonNull(task)) {
            cancelInRedis(task);
            log.info("任务:{},已成功删除", taskId);
        }
    }

    /**
     * 按照优先级和类型来拉取任务
     *
     * @param type     类型
     * @param priority 优先级
     * @return {@link Task}
     */
    @Override
    public Task pullTask(int type, int priority) {
        Task task = null;

        try {
            String key = ScheduleConstants.TOPIC + type + "_" + priority;
            //移除并获取列表最后一个元素
            String json = cacheService.lRightPop(key);

            if (StringUtils.isNotEmpty(json)) {
                task = JSON.parseObject(json, Task.class);
                //更新数据库内容
                upDateInDB(task.getTaskId(),ScheduleConstants.EXECUTED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return task;
    }

    /**
     * 更新任务,到期任务加载任务队列
     */
    @Override
    @Scheduled(cron = "0 */5 * * * ?") //每五分钟拉取数据库新数据
    @PostConstruct //项目启动时执行该方法
    public void reloadDBbData() {
        log.info("正在清除redis缓存");
        //删除数据库缓存
        clearCache();
        log.info("redis缓存已清除,正在拉取数据库数据...");
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE,5);
        LambdaQueryWrapper<TaskInfo> wrapper = new QueryWrapper<TaskInfo>().lambda().lt(TaskInfo::getExecuteTime, instance.getTime());
        List<TaskInfo> taskInfos = taskInfoMapper.selectList(wrapper);
        if (Objects.nonNull(taskInfos)&&taskInfos.size()>0){
            log.info("已拉取到:{}条任务数据,正在缓存!",taskInfos.size());
            for (TaskInfo taskInfo : taskInfos) {
                Task task = new Task();
                BeanUtils.copyProperties(taskInfo,task);
                addTask(task);
                log.info("已添加任务数据:{}",task);
            }
        }
    }

    private void clearCache(){
        // 删除缓存中未来数据集合和当前消费者队列的所有key
        Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");// future_
        log.info("扫描到未来任务数据:{}条",futureKeys==null?0:futureKeys.size());
        Set<String> topicKeys = cacheService.scan(ScheduleConstants.TOPIC + "*");// topic_
        log.info("扫描到当下队列任务数据:{}条",topicKeys==null?0:topicKeys.size());
        cacheService.delete(futureKeys);
        cacheService.delete(topicKeys);
    }


    /**
     * 将到期前五分钟任务加载至延迟队列,
     */
    @Override
    @Scheduled(fixedRate = 1000)
    public void refreshRedis() {
        log.info("正在更新缓存数据...");
        String token = cacheService.tryLock("FUTURE_TASK_SYNC", 1000 * 30);
        if (StringUtils.isNotEmpty(token)){
            Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");
            for (String futureKey : futureKeys) {//future_100_50

                //获取当前数据的key  topic
                String topicKey = ScheduleConstants.TOPIC+futureKey.split(ScheduleConstants.FUTURE)[1];

                //按照key和分值查询符合条件的数据 当前时间
                Set<String> tasks = cacheService.zRangeByScore(futureKey, 0, System.currentTimeMillis());

                //同步数据
                if(!tasks.isEmpty()){
                    cacheService.refreshWithPipeline(futureKey,topicKey,tasks);
                    log.info("成功的将"+futureKey+"刷新到了"+topicKey);
                }
            }
        }else {
            log.info("redis延迟队列暂无数据");
        }
    }


    /**
     * 取消在redis内任务
     *
     * @param task 任务
     */
    private void cancelInRedis(Task task) {
        log.info("正在删除redis任务数据,任务id={}", task.getTaskId());
        String key = task.getTaskType() + "_" + task.getPriority();
        long time = task.getExecuteTime().getTime();
        if (time <= System.currentTimeMillis()) {
            key = ScheduleConstants.TOPIC + key;
            String s = JSON.toJSONString(task);
            log.info(s);
            cacheService.lRemove(key, 0, s);
        } else {
            key = ScheduleConstants.FUTURE + key;
            cacheService.zRemove(key, JSON.toJSONString(task));
        }
        log.info("redis任务:{}删除完毕,正在返回", task.getTaskId());
    }

    /**
     * 取消在数据库
     *
     * @param taskId 任务id
     * @return {@link Task}
     */
    private Task upDateInDB(Long taskId, Integer status) {
        log.info("正在删除数据库任务数据,任务id={}", taskId);
        Task task = null;
        try {
            task = new Task();
            taskInfoMapper.deleteById(taskId);
            TaskInfoLogs taskInfoLogs = taskInfoLogsMapper.selectById(taskId);
            BeanUtils.copyProperties(taskInfoLogs, task);
            taskInfoLogs.setTaskType(status);
            taskInfoLogsMapper.updateById(taskInfoLogs);
            log.info("数据库任务:{}删除完毕,正在返回", taskId);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        return task;
    }

    private void saveInRedis(Task task) {
        log.info("任务正在添加到redis缓存,任务数据:{}", task);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        long executeTime = task.getExecuteTime().getTime();

        if (executeTime <= System.currentTimeMillis()) {
            //加入到任务队列List
            //定义任务key
            String taskKey = ScheduleConstants.TOPIC + task.getTaskType() + "_" + task.getPriority();
            cacheService.lLeftPush(taskKey, JSON.toJSONString(task));
        } else if (executeTime <= calendar.getTimeInMillis()) {
            //加入到延迟任务zSet
            //定义任务key
            String taskKey = ScheduleConstants.FUTURE + task.getTaskType() + "_" + task.getPriority();
            cacheService.zAdd(taskKey, JSON.toJSONString(task), task.getExecuteTime().getTime());
        }
        log.info("任务已成功添加到redis缓存,任务id={}", task.getTaskId());
    }

    private boolean saveInDB(Task task) {
        boolean res = false;
        try {
            log.info("任务正在添加到数据库,任务数据:{}", task);
            //1 保存任务
            TaskInfo taskInfo = new TaskInfo();
            BeanUtils.copyProperties(task, taskInfo);
            taskInfoMapper.insert(taskInfo);
            //2 保存任务数据日志
            TaskInfoLogs taskInfoLogs = new TaskInfoLogs();
            BeanUtils.copyProperties(taskInfo, taskInfoLogs);
            taskInfoLogs.setStatus(ScheduleConstants.SCHEDULED);
            taskInfoLogs.setVersion(1);
            taskInfoLogsMapper.insert(taskInfoLogs);
            task.setTaskId(taskInfo.getTaskId());
            res = true;
            log.info("任务已成功添加到数据库,任务id={}", taskInfo.getTaskId());
        } catch (BeansException e) {
            e.printStackTrace();
            log.error("任务添加至数据库异常,任务数据:{}", task);
        }
        return res;
    }
}
