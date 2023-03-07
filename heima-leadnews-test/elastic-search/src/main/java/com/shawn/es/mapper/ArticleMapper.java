package com.shawn.es.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.entities.Article;
import com.shawn.es.entities.ArticleDoc;

import java.util.List;

/**
 * @author shawn
 * @date 2023年 01月 26日 19:56
 */
public interface ArticleMapper extends BaseMapper<Article> {


    public List<ArticleDoc> initElasticSearch();
}
