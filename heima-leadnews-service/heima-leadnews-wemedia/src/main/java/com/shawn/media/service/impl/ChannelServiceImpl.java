package com.shawn.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.media.dtos.NewsDto;
import com.heima.model.media.entities.Channel;
import com.heima.model.media.entities.News;
import com.shawn.media.mapper.ChannelMapper;
import com.shawn.media.service.ChannelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author shawn
 * @date 2023年 01月 09日 18:48
 */
@Service
public class ChannelServiceImpl implements ChannelService {

    @Resource
    private ChannelMapper channelMapper;

    @Override
    public ResponseResult getAllChannel() {
        List<Channel> channels = channelMapper.selectList(new QueryWrapper<>());
        return ResponseResult.okResult(channels);
    }



}
