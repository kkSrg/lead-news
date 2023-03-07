package com.shawn.schedule.test;
import com.heima.common.redis.CacheService;
import com.heima.model.schedule.dtos.Task;
import com.shawn.schedule.ScheduleApplication;
import com.shawn.schedule.service.TaskInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import java.util.Date;

/**
 * @author shawn
 * @date 2023年 01月 16日 20:26
 */
@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class ScheduleTest {



    @Resource
    private TaskInfoService taskService;

    @Test
    public void addTask() {
        for (int i = 0; i < 5; i++) {
            Task task = new Task();
            task.setTaskType(100+i);
            task.setPriority(50);
            task.setParameters("task test".getBytes());
            task.setExecuteTime(new Date(new Date().getTime()+500*i));

            taskService.addTask(task);
        }
    }



    @Test
    public void cancelTask(){
        taskService.cancel(1618124704473272321L);
    }

    @Test
    public void testPoll(){
//        Task task = taskService.poll(100, 50);
//        System.out.println(task);
    }

    @Resource
    private CacheService cacheService;

    @Test
    public void removeRedis(){
        String value = "{\"executeTime\":1674624970291,\"parameters\":\"dGFzayB0ZXN0\",\"priority\":50,\"taskId\":1618120522651480065,\"taskType\":100}";
        cacheService.lRemove("FUTURE:100_50",0,value);
    }

}
