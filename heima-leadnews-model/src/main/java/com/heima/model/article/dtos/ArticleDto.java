package com.heima.model.article.dtos;

import com.heima.model.article.entities.Article;
import lombok.Data;

/**
 * @author shawn
 * @date 2023年 01月 11日 20:54
 */
@Data
public class ArticleDto extends Article {
    private String content;
}
