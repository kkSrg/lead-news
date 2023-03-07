package com.shawn.article.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.heima.common.constants.MediaConstants;
import com.heima.model.article.entities.ArticleConfig;
import com.shawn.article.mapper.ArticleConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * @author shawn
 * @date 2023年 01月 26日 12:59
 */
@Slf4j
@Component
public class KafkaListenerHandle {

    @Resource
    private ArticleConfigMapper articleConfigMapper;

    @KafkaListener(topics = MediaConstants.KAFKA_TOPIC_UP_OR_DOWN)
    public void messageHandle(String message){
        log.info("接收到消息:{}",message);
        Map map = JSON.parseObject(message, Map.class);
        Long articleId = Long.valueOf((String) map.get("articleId"));
        short enable = Short.parseShort((String) map.get("enable"));
        LambdaQueryWrapper<ArticleConfig> wrapper = new QueryWrapper<ArticleConfig>().lambda().eq(ArticleConfig::getArticleId, articleId);
        ArticleConfig articleConfig = articleConfigMapper.selectOne(wrapper);
        if (Objects.nonNull(articleConfig)){
            articleConfig.setIsDown(enable==0?1:0);
            articleConfigMapper.updateById(articleConfig);
            log.info("文章:{},已成功{}",articleId,enable==0?"下架":"上架");
        }
    }
}
