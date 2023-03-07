package com.shawn.article.service.impl;

import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.entities.Article;
import com.heima.model.article.entities.ArticleConfig;
import com.heima.model.article.entities.ArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import com.shawn.article.mapper.ArticleConfigMapper;
import com.shawn.article.mapper.ArticleContentMapper;
import com.shawn.article.mapper.ArticleMapper;
import com.shawn.article.service.ArticleService;
import com.shawn.article.service.FreeMarkerService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author shawn
 * @date 2023年 01月 07日 9:03
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleConfigMapper articleConfigMapper;


    @Resource
    private ArticleContentMapper articleContentMapper;

    @Resource
    private FreeMarkerService freeMarkerService;

    /**
     * 查询文章
     *
     * @param dto      dto
     * @param loadType 加载类型 1 加载更多 2 加载最新
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult selectArticles(ArticleHomeDto dto, Short loadType) {
        //分页数量校验
        Integer size = dto.getSize();
        if (size == null || size == 0) {
            size = 10;
        }
        size = Math.min(size, ArticleConstants.MAX_PAGE_SIZE);
        dto.setSize(size);

        //校验频道
        String tag = dto.getTag();
        if (Strings.isEmpty(tag)) {
            tag = ArticleConstants.DEFAULT_TAG;
        }
        dto.setTag(tag);

        //校验时间
        Date maxBehotTime = dto.getMaxBehotTime();
        Date minBehotTime = dto.getMinBehotTime();
        if (minBehotTime == null) {
            dto.setMinBehotTime(new Date());
        }

        if (maxBehotTime == null) {
            dto.setMaxBehotTime(new Date());
        }

        //校验加载类型
        if (!(loadType.equals(ArticleConstants.LOAD_MORE_TYPE) || loadType.equals(ArticleConstants.LOAD_NEW_TYPE))) {
            loadType = ArticleConstants.LOAD_MORE_TYPE;
        }
        List<Article> articles = articleMapper.loadArticles(dto, loadType);
        return ResponseResult.okResult(articles);
    }

    /**
     * 保存文章
     *
     * @param dto dto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {

        //保存文章内容
        Article article = new Article();
        BeanUtils.copyProperties(dto,article);

        articleMapper.insert(article);

        //保存文章配置
        ArticleConfig articleConfig = new ArticleConfig(article.getId());
        articleConfigMapper.insert(articleConfig);
        //保存文章文本内容
        String content = dto.getContent();
        if (content!=null&&content.length()>0){
            ArticleContent articleContent = new ArticleContent();
            articleContent.setArticleId(article.getId());
            articleContent.setContent(dto.getContent());
            articleContentMapper.insert(articleContent);
        }

        //异步生成文章静态文件
        freeMarkerService.initStaticFile(article, dto.getContent());
        return ResponseResult.okResult(article.getId());
    }
}
