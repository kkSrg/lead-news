package com.shawn.article.test;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.heima.model.article.entities.ArticleContent;
import com.shawn.article.ArticleApplication;
import com.shawn.article.mapper.ArticleContentMapper;
import com.shawn.minio.template.MinIOTemplate;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shawn
 * @date 2023年 01月 09日 9:06
 */
@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class CombinedTest {
    @Resource
    private ArticleContentMapper articleContentMapper;

    @Resource
    private MinIOTemplate minIOTemplate;


    @Resource
    private Configuration configuration;

    @Test
    public void combinedTest() throws IOException, TemplateException {
        QueryWrapper<ArticleContent> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArticleContent::getArticleId,1302864436297482242L);
        //查询文章
        ArticleContent articleContent = articleContentMapper.selectOne(wrapper);
        //抽取文章内容
        String content = articleContent.getContent();
        //封装模板
        Template template = configuration.getTemplate("article.ftl");

        Map<String,Object> params = new HashMap<>();
        params.put("content", JSONArray.parseArray(content));
        StringWriter stringWriter = new StringWriter();
        template.process(params,stringWriter);
        //存储MinIO
        InputStream is = new ByteArrayInputStream(stringWriter.toString().getBytes());
        String path = minIOTemplate.uploadHtmlFile("", articleContent.getArticleId() + ".html", is);
        System.out.println(path);
    }
}
