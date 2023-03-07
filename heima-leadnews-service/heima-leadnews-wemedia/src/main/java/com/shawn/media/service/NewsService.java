package com.shawn.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.media.dtos.DownUpDto;
import com.heima.model.media.dtos.NewsDto;
import com.heima.model.media.dtos.WmNewsDto;

/**
 * @author shawn
 * @date 2023年 01月 09日 21:38
 */
public interface NewsService {
    ResponseResult getNews(NewsDto newsDto);

    ResponseResult publish(Boolean draft, WmNewsDto dto);

    ResponseResult getById(Integer newsId);

    ResponseResult upAndDown(DownUpDto dto);
}
