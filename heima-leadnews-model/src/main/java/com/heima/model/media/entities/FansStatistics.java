package com.heima.model.media.entities;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * 自媒体粉丝数据统计表(FansStatistics)实体类
 *
 * @author shawn
 * @since 2023-01-09 11:57:04
 */
@Data
@TableName("wm_fans_statistics")
public class FansStatistics implements Serializable {
    private static final long serialVersionUID = -78772767712962266L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 主账号ID
     */
    private Integer userId;
    /**
     * 子账号ID
     */
    private Integer article;
    
    private Integer readCount;
    
    private Integer comment;
    
    private Integer follow;
    
    private Integer collection;
    
    private Integer forward;
    
    private Integer likes;
    
    private Integer unlikes;
    
    private Integer unfollow;
    
    private String burst;
    /**
     * 创建时间
     */
    private Date createdTime;


}

