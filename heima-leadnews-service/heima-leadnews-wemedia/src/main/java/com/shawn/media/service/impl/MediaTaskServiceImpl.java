package com.shawn.media.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.TaskTypeEnum;
import com.heima.model.media.entities.News;
import com.heima.model.schedule.dtos.Task;
import com.heima.utils.common.ProtostuffUtil;
import com.shawn.feign.apis.ScheduleClient;
import com.shawn.media.service.AutoAuditArticleService;
import com.shawn.media.service.MediaTaskService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author shawn
 * @date 2023年 01月 25日 16:56
 */
@Service
@Slf4j
public class MediaTaskServiceImpl implements MediaTaskService {

    @Resource
    private ScheduleClient scheduleClient;

    @Resource
    private AutoAuditArticleService autoAuditArticleService;

    /**
     * 添加任务
     *
     * @param id          id
     * @param publishTime 发布时间
     */
    @Override
    @Async
    public void addTask(Integer id, Date publishTime) {
        log.info("开始添加任务到redis");
        Task task = new Task();
        task.setExecuteTime(publishTime);
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        News news = new News();
        news.setId(id);
        task.setParameters(ProtostuffUtil.serialize(news));
        scheduleClient.addTask(task);
        log.info("任务添加结束,news id:{}",id);
    }

    /**
     * 发布到期文章
     *
     * @return {@link List}<{@link Integer}>
     */
    @Scheduled(fixedRate = 1000)
    @SneakyThrows
    @Override
    public void finishNews() {
        log.info("文章审核---消费任务执行---begin---");
        ResponseResult result = scheduleClient.pullTask(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(), TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        Object data = result.getData();
        int code = result.getCode();
        if (code==200&& Objects.nonNull(data)){
            Task task = (Task) data;
            byte[] parameters = task.getParameters();
            News news = ProtostuffUtil.deserialize(parameters, News.class);
            autoAuditArticleService.autoAuditNews(news.getId());
        }
        log.info("文章审核---消费任务执行---end---");
    }
}
