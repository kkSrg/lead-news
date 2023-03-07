package com.shawn.article.service;

import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.entities.Article;
import org.apache.kafka.common.protocol.types.Field;

/**
 * @author shawn
 * @date 2023年 01月 15日 18:56
 */
public interface FreeMarkerService {

    public void initStaticFile(Article article, String content);
}
