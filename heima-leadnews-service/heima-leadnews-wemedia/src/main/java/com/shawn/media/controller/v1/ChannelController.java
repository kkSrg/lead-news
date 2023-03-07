package com.shawn.media.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.media.dtos.NewsDto;
import com.shawn.media.service.ChannelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author shawn
 * @date 2023年 01月 09日 17:42
 */
@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController {

    @Resource
    private ChannelService channelService;



    @GetMapping("/channels")
    public ResponseResult getAllChannel(){
        return channelService.getAllChannel();
    }





}
