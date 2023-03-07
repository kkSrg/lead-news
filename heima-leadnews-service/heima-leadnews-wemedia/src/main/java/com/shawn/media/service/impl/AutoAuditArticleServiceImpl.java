package com.shawn.media.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.constants.MediaConstants;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.media.entities.Channel;
import com.heima.model.media.entities.News;
import com.heima.model.media.entities.Sensitive;
import com.heima.utils.common.SensitiveWordUtil;
import com.heima.utils.media.MethodUtils;
import com.shawn.feign.apis.ArticleFeignClient;
import com.shawn.leadnews.audit.result.AuditResult;
import com.shawn.leadnews.audit.service.ContentAuditService;
import com.shawn.media.mapper.ChannelMapper;
import com.shawn.media.mapper.NewsMapper;
import com.shawn.media.mapper.SensitiveMapper;
import com.shawn.media.service.AutoAuditArticleService;
import com.shawn.minio.template.MinIOTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shawn
 * @date 2023年 01月 11日 15:48
 */
@Service
public class AutoAuditArticleServiceImpl implements AutoAuditArticleService {
    @Resource
    private ContentAuditService contentAuditService;

    @Resource
    private MinIOTemplate minIOTemplate;

    @Resource
    private NewsMapper newsMapper;

    @Resource
    private ChannelMapper channelMapper;

    @Resource
    private ArticleFeignClient articleFeignClient;


    @Resource
    private SensitiveMapper sensitiveMapper;



    //审核文章
    @Async
    public void autoAuditNews(Integer newsId){
        //查询该文章
        News news = newsMapper.selectById(newsId);
        if (Objects.isNull(news)){
            throw new RuntimeException("AutoAuditArticleServiceImpl-autoAuditNews-29-文章"+newsId+"不存在!");
        }

        AuditResult result = new AuditResult();


        //审核文章内容图片
        String content = news.getContent();

        //初始化文字识别容器
        StringBuilder stringBuilder = new StringBuilder();
        List<byte[]> imageBytes = new ArrayList<>();
        if (StringUtils.isNotEmpty(content)){
            List<String> images = MethodUtils.getImages(content);
            String coverImages = news.getImages();
            if (StringUtils.isNotEmpty(coverImages)){
                String[] urls = coverImages.split(",");
                Collections.addAll(images,urls);
            }
            for (String image : images) {
                if (StringUtils.isNotEmpty(image)){
                    byte[] bytes = minIOTemplate.downLoadFile(image);
                    if (Objects.nonNull(bytes)&&bytes.length>0){
                        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                        BufferedImage read = null;
                        try {
                            read = ImageIO.read(in);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //文字识别
                        String ocrText = contentAuditService.OCRScan(read);
                        if (StringUtils.isNotEmpty(ocrText)){
                            stringBuilder.append("-").append(ocrText);
                        }
                        imageBytes.add(bytes);
                    }
                }
            }
        }
        AuditResult auditResult = contentAuditService.auditImage(imageBytes);


        //审核文章文本
        String text = news.getContent();
        text =text + news.getTitle()+ stringBuilder;
        if (StringUtils.isNotEmpty(text)){
            result = contentAuditService.auditText(text);
            //文本审核
            String res = sensitiveCheck(text);
            if (StringUtils.isNotEmpty(res)){
                //审核失败
                result.setMsg("文章内容存在敏感内容:"+res);
                result.setConclusionType(MediaConstants.AUTO_AUDIT_REJECT);
            }
        }

        Integer type1 = result.getConclusionType();
        Integer type2 = auditResult.getConclusionType();
        if (MediaConstants.AUTO_AUDIT_REJECT.equals(type1)){
            news.setStatus(MediaConstants.PUBLISH_AUDIT_FAIL_STATUS);
            //文本审核违规
            if (MediaConstants.AUTO_AUDIT_REJECT.equals(type2)){
                //图片审核违规
                result.setMsg(result.getMsg()+","+auditResult.getMsg());
            }
        }else if (MediaConstants.AUTO_AUDIT_SUSPECTED.equals(type1)){
            //文本审核不确定

            if (MediaConstants.AUTO_AUDIT_REJECT.equals(type2)){
                //图片审核违规
                news.setStatus(MediaConstants.PUBLISH_AUDIT_FAIL_STATUS);
                result.setMsg(auditResult.getMsg());
            }else {
                //待人工审核
                news.setStatus(MediaConstants.PUBLISH_MANUAL_AUDIT_STATUS);
                result.setMsg(result.getMsg()+","+auditResult.getMsg());
            }
        }else {
            //文本审核通过
            if (MediaConstants.AUTO_AUDIT_PASS.equals(type2)){
                //图片审核通过
                news.setStatus(MediaConstants.PUBLISH_AUDIT_PASS_STATUS);
                //同步文章至app端
                ArticleDto articleDto = new ArticleDto();

                //查询频道名称
                Integer channelId = news.getChannelId();
                if (Objects.nonNull(channelId)){
                    Channel channel = channelMapper.selectById(channelId);
                    articleDto.setChannelName(channel.getName());
                }

                //同步信息
                BeanUtils.copyProperties(news,articleDto);
                articleDto.setAuthorId(news.getUserId());
                articleDto.setLayout(news.getType());
                articleDto.setSyncStatus((short)1);

                ResponseResult responseResult = articleFeignClient.saveArticle(articleDto);
                Long articleId = (long)responseResult.getData();
                news.setArticleId(articleId);
                Date publishTime = news.getPublishTime();
                if (Objects.nonNull(publishTime)&& publishTime.before(new Date())){
                    news.setStatus(MediaConstants.PUBLISH_ON_STATUS);
                }
            }else {
                if (MediaConstants.AUTO_AUDIT_SUSPECTED.equals(type2)){
                    //待人工审核
                    news.setStatus(MediaConstants.PUBLISH_MANUAL_AUDIT_STATUS);
                }else {
                    //图片审核失败
                    news.setStatus(MediaConstants.PUBLISH_AUDIT_FAIL_STATUS);
                }
                result.setMsg(auditResult.getMsg());
            }

        }
        //更新news状态
        news.setReason(result.getMsg());
        newsMapper.updateById(news);
    }

    private String sensitiveCheck(String text) {
        //初始化敏感词库
        List<Sensitive> sensitives = sensitiveMapper.selectList(Wrappers.<Sensitive>lambdaQuery().select(Sensitive::getSensitives));
        List<String> strings = sensitives.stream().map(Sensitive::getSensitives).collect(Collectors.toList());
        SensitiveWordUtil.initMap(strings);

        Map<String, Integer> map = SensitiveWordUtil.matchWords(text);
        if (map.size()>0){
            return map.toString();
        }
        return null;
    }
}
