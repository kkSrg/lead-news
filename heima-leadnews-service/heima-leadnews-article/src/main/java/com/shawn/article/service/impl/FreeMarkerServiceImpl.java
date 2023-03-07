package com.shawn.article.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.entities.Article;
import com.heima.model.article.entities.ArticleContent;
import com.shawn.article.mapper.ArticleMapper;
import com.shawn.article.service.FreeMarkerService;
import com.shawn.minio.template.MinIOTemplate;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shawn
 * @date 2023年 01月 15日 18:57
 */
@Service
public class FreeMarkerServiceImpl implements FreeMarkerService {

    @Resource
    private MinIOTemplate minIOTemplate;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private Configuration configuration;

    @Override
    @Async
    public void initStaticFile(Article article, String content) {
        StringWriter stringWriter = null;

        try {
            //封装模板
            Template template = configuration.getTemplate("article.ftl");

            Map<String,Object> params = new HashMap<>();
            params.put("content", JSONArray.parseArray(content));
            stringWriter = new StringWriter();
            template.process(params,stringWriter);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            return;
        }
        //存储MinIO
        InputStream is = new ByteArrayInputStream(stringWriter.toString().getBytes());
        String path = minIOTemplate.uploadHtmlFile("", article.getId() + ".html", is);
        article.setStaticUrl(path);
        articleMapper.updateById(article);
    }
}
