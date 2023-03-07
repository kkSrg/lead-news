package com.shawn.media.test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.model.media.entities.Sensitive;
import com.heima.utils.common.SensitiveWordUtil;
import com.shawn.leadnews.audit.result.AuditResult;
import com.shawn.leadnews.audit.service.ContentAuditService;
import com.shawn.media.WeMediaApplication;
import com.shawn.media.mapper.SensitiveMapper;
import com.shawn.minio.template.MinIOTemplate;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.xmlgraphics.ps.PSImageUtils;
import org.assertj.core.util.Arrays;
import org.ghost4j.util.ImageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shawn
 * @date 2023年 01月 10日 21:10
 */
@SpringBootTest(classes = WeMediaApplication.class)
@RunWith(SpringRunner.class)
public class MediaTest {

    @Resource
    private ContentAuditService auditService;

    @Resource
    private MinIOTemplate minIOTemplate;


    @Test
    public void textAuditTest(){
        AuditResult auditResult = auditService.auditText("尼玛去死!习近平!");
        System.out.println(auditResult);
    }

    @Test
    public void imageAuditTest(){
//        byte[] bytes = minIOTemplate.downLoadFile("http://192.168.200.130:9000/leadnews/2021/04/26/a73f5b60c0d84c32bfe175055aaaac40.jpg");
        byte[] bytes = minIOTemplate.downLoadFile("http://192.168.200.130:9000/leadnews/ak47.jpg");
        auditService.auditImage(bytes);
    }

    @Resource
    private SensitiveMapper sensitiveMapper;

    @Test
    public void sensitiveTest(){
        ITesseract iTesseract = new Tesseract();
        //配置敏感词库
        List<Sensitive> sensitives = sensitiveMapper.selectList(Wrappers.<Sensitive>lambdaQuery().select(Sensitive::getSensitives));
        List<String> strings = sensitives.stream().map(Sensitive::getSensitives).collect(Collectors.toList());
        SensitiveWordUtil.initMap(strings);

        Map<String, Integer> result = SensitiveWordUtil.matchWords("蚁力神");
        System.out.println(result);
    }

    @Test
    public  void ocr() throws IOException {
        ITesseract iTesseract = new Tesseract();
        //设置中文识别数据
        iTesseract.setDatapath("D:\\Java Work Place\\Develop\\heima-leadnews\\heima-leadnews-service\\heima-leadnews-wemedia\\src\\main\\resources\\chinese-thesaurus");
        //设置语言
        iTesseract.setLanguage("chi_sim");

        byte[] bytes = minIOTemplate.downLoadFile("http://192.168.200.130:9000/leadnews/Snipaste_2023-01-14_17-16-34.png");

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        BufferedImage bi = ImageIO.read(in);

        try {
            String text = iTesseract.doOCR(bi,null);
            text = text.replaceAll("[\r\n]","-").replaceAll(" ","");
            System.out.println(text);
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
    }
}
