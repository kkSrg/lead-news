package com.shawn.media.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.media.dtos.DownUpDto;
import com.heima.model.media.dtos.NewsDto;
import com.heima.model.media.dtos.WmNewsDto;
import com.shawn.media.service.NewsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author shawn
 * @date 2023年 01月 09日 21:37
 */
@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    @Resource
    private NewsService newsService;
    
    @PostMapping("/up-and-down")
    public ResponseResult upAndDown(@RequestBody DownUpDto dto){
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return newsService.upAndDown(dto);
    }

    /**
     * 分页获取文章
     *
     * @param newsDto 新闻dto
     * @return {@link ResponseResult}
     */
    @PostMapping("/list")
    public ResponseResult getNews(@RequestBody NewsDto newsDto){
        if (Objects.isNull(newsDto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        return newsService.getNews(newsDto);
    }


    /**
     * 发布
     *
     * @param draft 是否为草稿
     * @return {@link ResponseResult}
     */
    @PostMapping("/submit")
    public ResponseResult publish(@RequestBody WmNewsDto dto , Boolean draft){
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        return newsService.publish(draft,dto);
    }

    @GetMapping("/one/{newsId}")
    public ResponseResult getById(@PathVariable Integer newsId){
        return newsService.getById(newsId);
    }
}
