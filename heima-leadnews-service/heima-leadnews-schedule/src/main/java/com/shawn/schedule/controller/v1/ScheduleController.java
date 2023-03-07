package com.shawn.schedule.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.schedule.dtos.Task;
import com.shawn.schedule.service.TaskInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author shawn
 * @date 2023年 01月 25日 16:30
 */
@RestController
@RequestMapping("/api/task/v1")
public class ScheduleController {

    @Resource
    private TaskInfoService taskInfoService;

    @PostMapping("/add")
    public ResponseResult addTask(@RequestBody Task task){
        taskInfoService.addTask(task);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @GetMapping("/{taskId}")
    public ResponseResult cancel(@PathVariable Long taskId){
        taskInfoService.cancel(taskId);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


    @GetMapping("/{type}/{priority}")
    public ResponseResult pullTask(@PathVariable int type,@PathVariable int priority){
        Task task = taskInfoService.pullTask(type, priority);
        return ResponseResult.okResult(task);
    }
}
