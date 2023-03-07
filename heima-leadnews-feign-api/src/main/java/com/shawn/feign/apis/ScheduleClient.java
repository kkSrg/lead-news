package com.shawn.feign.apis;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.schedule.dtos.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author shawn
 * @date 2023年 01月 25日 16:20
 */
@FeignClient("leadnews-schedule")
public interface ScheduleClient {

    @PostMapping("/api/task/v1/add")
    public ResponseResult addTask(@RequestBody Task task);

    @GetMapping("/api/task/v1/{taskId}")
    public ResponseResult cancel(@PathVariable Long taskId);


    @GetMapping("/api/task/v1/{type}/{priority}")
    public ResponseResult pullTask(@PathVariable int type,@PathVariable int priority);
}
