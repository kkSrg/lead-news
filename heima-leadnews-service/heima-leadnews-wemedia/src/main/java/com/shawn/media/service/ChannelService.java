package com.shawn.media.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.media.dtos.NewsDto;
import org.springframework.stereotype.Service;

/**
 * @author shawn
 * @date 2023年 01月 09日 18:48
 */
public interface ChannelService {
    ResponseResult getAllChannel();

}
