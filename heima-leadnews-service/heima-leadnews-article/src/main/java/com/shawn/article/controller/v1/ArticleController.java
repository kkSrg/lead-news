package com.shawn.article.controller.v1;

import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.common.dtos.ResponseResult;
import com.shawn.article.service.ArticleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author shawn
 * @date 2023年 01月 06日 17:03
 */
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 保存文章
     *
     * @param dto dto
     * @return {@link ResponseResult}
     */
    @PostMapping("/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto dto){
        if (Objects.isNull(dto)){
            throw new RuntimeException("参数为空!");
        }
        return articleService.saveArticle(dto);
    }

    /**
     * 加载首页
     *
     * @param dto dto
     * @return {@link ResponseResult}
     */
    @PostMapping("load")
    public ResponseResult loadIndex(@RequestBody ArticleHomeDto dto){
        return articleService.selectArticles(dto, ArticleConstants.LOAD_NEW_TYPE);
    }

    /**
     * 加载更多
     *
     * @param dto dto
     * @return {@link ResponseResult}
     */
    @PostMapping("/loadmore")
    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto){
        return articleService.selectArticles(dto,ArticleConstants.LOAD_MORE_TYPE);
    }

    /**
     * 加载最新
     *
     * @param dto dto
     * @return {@link ResponseResult}
     */
    @PostMapping("/loadnew")
    public ResponseResult loadNew(@RequestBody ArticleHomeDto dto){
        return articleService.selectArticles(dto,ArticleConstants.LOAD_NEW_TYPE);
    }
}
