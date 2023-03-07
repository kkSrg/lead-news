package com.shawn.article.service;

import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @author shawn
 * @date 2023年 01月 07日 9:03
 */
public interface ArticleService {
    ResponseResult selectArticles(ArticleHomeDto dto, Short loadType);

    ResponseResult saveArticle(ArticleDto dto);
}
