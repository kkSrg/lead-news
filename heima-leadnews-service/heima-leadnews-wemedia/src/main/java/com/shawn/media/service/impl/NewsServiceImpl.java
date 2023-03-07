package com.shawn.media.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.heima.common.constants.MediaConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.media.dtos.DownUpDto;
import com.heima.model.media.dtos.NewsDto;
import com.heima.model.media.dtos.WmNewsDto;
import com.heima.model.media.entities.Material;
import com.heima.model.media.entities.News;
import com.heima.model.media.entities.NewsMaterial;
import com.heima.utils.media.MethodUtils;
import com.shawn.media.intercept.ThreadContent;
import com.shawn.media.mapper.MaterialMapper;
import com.shawn.media.mapper.NewsMapper;
import com.shawn.media.mapper.NewsMaterialMapper;
import com.shawn.media.mapper.SensitiveMapper;
import com.shawn.media.service.AutoAuditArticleService;
import com.shawn.media.service.MediaTaskService;
import com.shawn.media.service.NewsService;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shawn
 * @date 2023年 01月 09日 21:38
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Resource
    private MediaTaskService mediaTaskService;
    @Resource
    private NewsMapper newsMapper;

    @Resource
    private NewsMaterialMapper newsMaterialMapper;

    @Resource
    private MaterialMapper materialMapper;

    @Resource
    private AutoAuditArticleService autoAuditArticleService;


    @Resource
    private SensitiveMapper sensitiveMapper;

    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public ResponseResult getNews(NewsDto newsDto) {

        Integer page = newsDto.getPage();
        Integer size = newsDto.getSize();
        page = (page-1)*size;
        newsDto.setPage(page);
        List<News> list = newsMapper.getNewsByCondition(newsDto);

        return ResponseResult.okResult(list);
    }

    /**
     * 发布或提交草稿
     *
     * @param draft 草案
     * @param dto   dto
     * @return {@link ResponseResult}
     */
    @Override
    @Transactional
    public ResponseResult publish(Boolean draft, WmNewsDto dto) {
        Integer userId = ThreadContent.getId();
        Short status = dto.getStatus();
        if (status == null && draft != null) {
            status = (short) (draft ? 1 : 0);
        }
        // 构建news对象
        News news = new News();
        BeanUtils.copyProperties(dto,news);
        news.setUserId(userId);
        if (news.getType()==-1){
            news.setType(null);
        }

        // 1. 判断提交或者更新
        if (Objects.isNull(dto.getId())){
        // 1.1 提交
        // 1.1.1发布文章
            // 获取主体内容图片
            List<String> urls = MethodUtils.getImages(dto.getContent());
            news.setCreatedTime(new Date());
            //匹配封面内容
            List<Material> materials = saveFaceImage(dto, userId, news, urls);

            // 保存文章/草稿
          newsMapper.insert(news);
          //保存素材库关系
            saveNewsMaterial(dto, news, materials);

            if (status==0){
              //草稿结束方法
              return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
          }
         // 处理分图关系


          //保存素材文章引用关系
          // 查询素材id
            news.setSubmitedTime(new Date());
            newsMapper.updateById(news);
            saveMaterialBatch(news.getId(),userId,urls);
        }else {
        // 1.2 更新
            // 2. 判断是否为草稿
            if (status==0){
                //草稿结束方法
                newsMapper.updateById(news);
                return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
            }
            //重新维护新的图片关系
            List<String> urls = MethodUtils.getImages(dto.getContent());
            //更新封面素材关系
            List<Material> materials = saveFaceImage(dto, userId, news, urls);
            //保存素材库关系
            saveNewsMaterial(dto,news,materials);
            //删除对应素材关系
            QueryWrapper<NewsMaterial> wrapper = new QueryWrapper<>();
            wrapper.lambda()
                    .eq(NewsMaterial::getNewsId,dto.getId());
            newsMaterialMapper.delete(wrapper);

            newsMapper.updateById(news);

            //更新内容图片素材关系
            saveMaterialBatch(news.getId(),userId,urls);

        }
        //提交任务队列
        mediaTaskService.addTask(news.getId(),news.getPublishTime());
//        autoAuditArticleService.autoAuditNews(news.getId());

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult getById(Integer newsId) {

        News news = newsMapper.selectById(newsId);
        return ResponseResult.okResult(news);
    }

    @Override
    public ResponseResult upAndDown(DownUpDto dto) {
        //查询该文章
        News news = newsMapper.selectById(dto.getId());
        if (Objects.isNull(news)){
            //文章不存在
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        Short enable = dto.getEnable();
        short status = news.getStatus();
        if (MediaConstants.PUBLISH_ON_STATUS.equals(status)&&enable == 1||!MediaConstants.PUBLISH_ON_STATUS.equals(status)&&enable == 0){
            //1.已发布文章再次发布 2.已下架文章再次下架
            return ResponseResult.errorResult(AppHttpCodeEnum.NEWS_HAS_BEEN_PUBLISH);
        }
        //修改文章显示状态
        news.setEnable(Integer.valueOf(enable));
        newsMapper.updateById(news);

        //发布消息到Kafka,实现article端文章状态修改
        if (news.getArticleId()!=null){
            Map<String,Object> map = new HashMap<>();
            map.put("articleId",news.getArticleId());
            map.put("enable",enable);
            String json = JSON.toJSONString(map);
            kafkaTemplate.send(MediaConstants.KAFKA_TOPIC_UP_OR_DOWN,json);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private void saveNewsMaterial(WmNewsDto dto, News news, List<Material> materials) {
        if (Objects.nonNull(materials)&& materials.size()>0){
            List<NewsMaterial> newsMaterials = materials.stream().map(material -> {
                NewsMaterial newsMaterial = new NewsMaterial();
                newsMaterial.setNewsId(news.getId());
                newsMaterial.setMaterialId(material.getId());
                if (dto.getType() == -1) {
                    newsMaterial.setType(0);
                } else {
                    newsMaterial.setType(1);
                }
                newsMaterial.setOrd(0);
                return newsMaterial;
            }).collect(Collectors.toList());
            newsMaterialMapper.saveBatch(newsMaterials);
        }
    }

    private void imageConversion(StringBuilder builder, List<String> images) {
        for (int i = 0; i < images.size(); i++) {
            if (i!= images.size()-1){
                builder.append(images.get(i)).append(",");
            }else {
                builder.append(images.get(i));
            }
        }
    }

    /**
     * 保存封面图片
     *
     * @param dto          dto
     * @param userId       用户id
     * @param news         新闻
     * @param urls         文章内部图片地址
     */
    private List<Material> saveFaceImage(WmNewsDto dto, Integer userId, News news, List<String> urls) {
        List<String> pic = new ArrayList<>();
        if (dto.getType()!=-1) {
            StringBuilder builder = new StringBuilder();
            List<String> images = dto.getImages();
            imageConversion(builder, images);
            news.setImages(builder.toString());
            pic = images;
        }else {
            StringBuilder stringBuilder = new StringBuilder();
            if (urls.size()>0){
                if (urls.size()>=3){
                    List<String> collect = urls.stream().limit(3).collect(Collectors.toList());
                    imageConversion(stringBuilder,collect);
                    //多图
                    news.setType((short)3);
                    pic = collect ;
                }else {
                    stringBuilder.append(urls.get(0));
                    //单图
                    news.setType((short)1);
                    pic.add(urls.get(0));
                }
                String images = stringBuilder.toString();
                if (StringUtils.isNotEmpty(images)){
                    news.setImages(images);
                }
            }else {
                news.setType((short)0);
            }
        }

        //获取素材库关系
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .in(Material::getUrl, pic).eq(Material::getUserId, userId);
        return materialMapper.selectList(wrapper);

    }

    /**
     * 批量保存素材
     *
     * @param newsId 消息id
     * @param userId 用户id
     * @param urls   文章内部图片地址
     */
    private void saveMaterialBatch(Integer newsId, Integer userId, List<String> urls) {
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Material::getUserId, userId)
                .in(Material::getUrl, urls);
        List<Material> materials = materialMapper.selectList(wrapper);
        List<NewsMaterial> newsMaterials = new ArrayList<>();
        if (Objects.nonNull(materials) && materials.size() > 0) {
            for (int i = 0; i < materials.size(); i++) {
                NewsMaterial nm = new NewsMaterial();
                nm.setNewsId(newsId);
                nm.setMaterialId(materials.get(i).getId());
                nm.setType(0);
                nm.setOrd(i);
                newsMaterials.add(nm);
            }
            // 批量保存
            newsMaterialMapper.saveBatch(newsMaterials);
        }
    }


}
