package com.shawn.media.service;

import java.util.Date;
import java.util.List;

/**
 * @author shawn
 * @date 2023年 01月 25日 16:55
 */
public interface MediaTaskService {

    public void addTask(Integer id , Date publishTime);

    public void finishNews();
}
