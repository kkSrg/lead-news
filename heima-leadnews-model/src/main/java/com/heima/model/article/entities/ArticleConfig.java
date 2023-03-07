package com.heima.model.article.entities;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;

/**
 * APP已发布文章配置表(ArticleConfig)实体类
 *
 * @author shawn
 * @since 2023-01-06 17:18:12
 */
@Data
@TableName("ap_article_config")
public class ArticleConfig implements Serializable {
    private static final long serialVersionUID = -34437306962358697L;
    public ArticleConfig(Long articleId){
        this.articleId = articleId;
        this.isComment = 1;
        this.isForward = 1;
        this.isDown = 0;
        this.isDelete = 0;
    }

    public ArticleConfig(){
    }
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
     * 是否可评论
     */
    private Integer isComment;
    /**
     * 是否转发
     */
    private Integer isForward;
    /**
     * 是否下架
     */
    private Integer isDown;
    /**
     * 是否已删除
     */
    private Integer isDelete;


}

