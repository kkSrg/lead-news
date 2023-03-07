package com.shawn.schedule.service;

import com.heima.model.schedule.dtos.Task;
import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * @author shawn
 * @date 2023年 01月 16日 14:36
 */

public interface TaskInfoService {
    public void addTask(Task task);

    public void cancel(Long taskId);


    public Task pullTask(int type,int priority);

    public void reloadDBbData();

    public void refreshRedis();
}
