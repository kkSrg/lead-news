package com.heima.model.article.entities;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * APP收藏信息表(Collection)实体类
 *
 * @author shawn
 * @since 2023-01-06 17:18:12
 */
@Data
@TableName("ap_collection")
public class Collection implements Serializable {
    private static final long serialVersionUID = -35460425528147137L;
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * 实体ID
     */
    private Integer entryId;
    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 点赞内容类型
            0文章
            1动态
     */
    private Integer type;
    /**
     * 创建时间
     */
    private Date collectionTime;
    /**
     * 发布时间
     */
    private Date publishedTime;


}

