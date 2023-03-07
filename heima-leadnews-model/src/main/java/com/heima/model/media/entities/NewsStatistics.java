package com.heima.model.media.entities;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * 自媒体图文数据统计表(NewsStatistics)实体类
 *
 * @author shawn
 * @since 2023-01-09 11:57:04
 */
@Data
@TableName("wm_news_statistics")
public class NewsStatistics implements Serializable {
    private static final long serialVersionUID = 508282755726779762L;
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
    /**
     * 阅读量
     */
    private Integer readCount;
    /**
     * 评论量
     */
    private Integer comment;
    /**
     * 关注量
     */
    private Integer follow;
    /**
     * 收藏量
     */
    private Integer collection;
    /**
     * 转发量
     */
    private Integer forward;
    /**
     * 点赞量
     */
    private Integer likes;
    /**
     * 不喜欢
     */
    private Integer unlikes;
    /**
     * 取消关注量
     */
    private Integer unfollow;
    
    private String burst;
    /**
     * 创建时间
     */
    private Date createdTime;


}

