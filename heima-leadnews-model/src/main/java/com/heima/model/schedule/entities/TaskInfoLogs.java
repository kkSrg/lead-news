package com.heima.model.schedule.entities;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * (TaskinfoLogs)实体类
 *
 * @author shawn
 * @since 2023-01-15 19:42:46
 */
@Data
@TableName("taskinfo_logs")
public class TaskInfoLogs implements Serializable {
    private static final long serialVersionUID = -10799856711285580L;
    /**
     * 任务id
     */
    @TableId(type = IdType.INPUT)
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
    /**
     * 版本号,用乐观锁
     */
    @Version
    private Integer version;
    /**
     * 状态 0=初始化状态 1=EXECUTED 2=CANCELLED
     */
    private Integer status;


}

