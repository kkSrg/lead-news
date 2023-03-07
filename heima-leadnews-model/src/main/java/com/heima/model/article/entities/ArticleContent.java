package com.heima.model.article.entities;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * APP已发布文章内容表(ArticleContent)实体类
 *
 * @author shawn
 * @since 2023-01-06 17:18:12
 */
@Data
@TableName("ap_article_content")
public class ArticleContent implements Serializable {
    private static final long serialVersionUID = -60103163461204420L;
    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 文章内容
     */
    private String content;


}

