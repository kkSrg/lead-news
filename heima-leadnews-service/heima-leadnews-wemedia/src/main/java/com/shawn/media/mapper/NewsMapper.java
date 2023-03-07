package com.shawn.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.media.dtos.NewsDto;
import com.heima.model.media.entities.News;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shawn
 * @date 2023年 01月 09日 21:40
 */
public interface NewsMapper extends BaseMapper<News> {
    List<News> getNewsByCondition(@Param("dto") NewsDto dto);
}
