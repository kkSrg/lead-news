package com.heima.model.schedule.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shawn
 * @date 2023年 01月 16日 15:44
 */
@Data
public class Task implements Serializable {
    private static final long serialVersionUID = -32827466210199261L;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 任务类型
     */
    private Integer taskType;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 执行时间
     */
    private Date executeTime;
    /**
     * 参数
     */
    private byte[] parameters;

}
