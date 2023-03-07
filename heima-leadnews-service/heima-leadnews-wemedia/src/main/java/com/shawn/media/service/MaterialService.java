package com.shawn.media.service;

import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.media.dtos.PageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author shawn
 * @date 2023年 01月 09日 16:21
 */
public interface MaterialService {
    ResponseResult uploadPicture(MultipartFile picture) throws IOException;

    ResponseResult listMaterial(PageDto dto);

    ResponseResult collectMaterial(Integer materialId,Integer whetherCollect);

    ResponseResult deleteMaterial(Integer materialId);
}
