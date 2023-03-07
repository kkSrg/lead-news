package com.heima.model.media.entities;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * 自媒体图文内容信息表(News)实体类
 *
 * @author shawn
 * @since 2023-01-09 11:57:04
 */
@Data
@TableName("wm_news")
public class News implements Serializable {
    private static final long serialVersionUID = -83383073798303308L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 自媒体用户ID
     */
    private Integer userId;
    /**
     * 标题
     */
    private String title;
    /**
     * 图文内容
     */
    private String content;
    /**
     * 文章布局
            0 无图文章
            1 单图文章
            3 多图文章
     */
    private Short type;
    /**
     * 图文频道ID
     */
    private Integer channelId;
    
    private String labels;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 提交时间
     */
    private Date submitedTime;
    /**
     * 当前状态
            0 草稿
            1 提交（待审核）
            2 审核失败
            3 人工审核
            4 人工审核通过
            8 审核通过（待发布）
            9 已发布
     */
    private short status;
    /**
     * 定时发布时间，不定时则为空
     */
    private Date publishTime;
    /**
     * 拒绝理由
     */
    private String reason;
    /**
     * 发布库文章ID
     */
    private Long articleId;
    /**
     * //图片用逗号分隔
     */
    private String images;
    
    private Integer enable;


}

