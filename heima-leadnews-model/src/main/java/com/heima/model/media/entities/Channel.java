package com.heima.model.media.entities;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * 频道信息表(Channel)实体类
 *
 * @author shawn
 * @since 2023-01-09 11:57:02
 */
@Data
@TableName("wm_channel")
public class Channel implements Serializable {

    private static final long serialVersionUID = -74578401583343949L;
    
    private Integer id;
    /**
     * 频道名称
     */
    private String name;
    /**
     * 频道描述
     */
    private String description;
    /**
     * 是否默认频道
     */
    private Integer isDefault;
    
    private Integer status;
    /**
     * 默认排序
     */
    private Integer ord;
    /**
     * 创建时间
     */
    private Date createdTime;


}

