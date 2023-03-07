package com.heima.model.media.entities;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * 敏感词信息表(Sensitive)实体类
 *
 * @author shawn
 * @since 2023-01-14 15:29:50
 */
@Data
@TableName("wm_sensitive")
public class Sensitive implements Serializable {
    private static final long serialVersionUID = -90586466057158725L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 敏感词
     */
    private String sensitives;
    /**
     * 创建时间
     */
    private Date createdTime;


}

