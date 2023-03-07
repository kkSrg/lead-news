package com.heima.model.schedule.entities;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * (Taskinfo)实体类
 *
 * @author shawn
 * @since 2023-01-15 19:42:04
 */
@Data
@TableName("taskinfo")
public class TaskInfo implements Serializable {
    private static final long serialVersionUID = -32827466210199261L;
    /**
     * 任务id
     */
    @TableId(type = IdType.ID_WORKER)
    private Long taskId;
    /**
     * 执行时间
     */
    private Date executeTime;
    /**
     * 参数
     */
    private byte[] parameters;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 任务类型
     */
    private Integer taskType;

}

