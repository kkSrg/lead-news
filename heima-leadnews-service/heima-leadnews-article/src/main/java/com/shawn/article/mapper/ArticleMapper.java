package com.shawn.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.entities.Article;
import com.heima.model.common.dtos.ResponseResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shawn
 * @date 2023年 01月 07日 10:24
 */

public interface ArticleMapper extends BaseMapper<Article> {

    List<Article> loadArticles(@Param("dto") ArticleHomeDto dto, @Param("loadType") Short loadType);
}
