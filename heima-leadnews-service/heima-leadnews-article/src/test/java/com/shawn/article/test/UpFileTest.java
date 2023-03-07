package com.shawn.article.test;

import com.shawn.article.ArticleApplication;
import com.shawn.minio.MinIOAutoConfiguration;
import com.shawn.minio.template.MinIOTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author shawn
 * @date 2023年 01月 07日 20:01
 */
@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class UpFileTest {
    @Resource
    private MinIOTemplate minIOTemplate;

    @Test
    public void upFileTest() throws IOException {
       FileInputStream fis = new FileInputStream("D:\\学习资料\\JAVA\\就业班资料\\谷粒商城\\课件和文档\\基础篇\\资料\\pics\\0d40c24b264aa511.jpg");
        String url = minIOTemplate.uploadHtmlFile("", "demo", fis);
        System.out.println(url);
    }
}
