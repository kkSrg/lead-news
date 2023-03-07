package com.shawn.media.controller.v1;

import com.heima.common.constants.MediaConstants;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.media.dtos.PageDto;
import com.shawn.media.mapper.MaterialMapper;
import com.shawn.media.service.MaterialService;
import com.shawn.minio.template.MinIOTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;


/**
 * @author shawn
 * @date 2023年 01月 09日 16:07
 */
@RestController
@RequestMapping("/api/v1/material")
public class MaterialController {




    @Resource
    private MaterialService materialService;

    /**
     * 上传照片
     *
     * @param multipartFile 图片
     * @return {@link ResponseResult}
     * @throws IOException ioexception
     */
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile) throws IOException {
        if (Objects.isNull(multipartFile) || multipartFile.getSize() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        return materialService.uploadPicture(multipartFile);
    }

    /**
     * 分页查询素材库
     *
     * @param dto dto
     * @return {@link ResponseResult}
     */
    @PostMapping("/list")
    public ResponseResult listMaterial(@RequestBody PageDto dto){
        return materialService.listMaterial(dto);
    }

    /**
     * 收藏素材
     *
     * @param materialId 材料标识
     * @return {@link ResponseResult}
     */
    @GetMapping("/collect/{materialId}")
    public ResponseResult collectMaterial(@PathVariable Integer materialId){
        return materialService.collectMaterial(materialId, MediaConstants.MATERIAL_NOT_COLLECTION);
    }

    @GetMapping("/cancel_collect/{materialId}")
    public ResponseResult cancelCollect(@PathVariable Integer materialId){
        return materialService.collectMaterial(materialId,MediaConstants.MATERIAL_COLLECTION);
    }


    ///del_picture/67

    @GetMapping("/del_picture/{materialId}")
    public ResponseResult deleteMaterial(@PathVariable Integer materialId){
        return materialService.deleteMaterial(materialId);
    }
}
