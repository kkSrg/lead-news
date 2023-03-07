package com.shawn.media.service.impl;

import com.alibaba.nacos.common.utils.UuidUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.common.constants.MediaConstants;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.media.dtos.PageDto;
import com.heima.model.media.entities.Material;
import com.heima.model.media.entities.NewsMaterial;
import com.shawn.media.intercept.ThreadContent;
import com.shawn.media.mapper.MaterialMapper;
import com.shawn.media.mapper.NewsMaterialMapper;
import com.shawn.media.service.MaterialService;
import com.shawn.minio.template.MinIOTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author shawn
 * @date 2023年 01月 09日 16:22
 */
@Service
public class MaterialServiceImpl implements MaterialService {
    @Resource
    private MinIOTemplate minIOTemplate;

    @Resource
    private MaterialMapper materialMapper;

    @Resource
    private NewsMaterialMapper newsMaterialMapper;

    @Override
    public ResponseResult uploadPicture(MultipartFile picture) throws IOException {

        String filename = picture.getOriginalFilename();
        String postfix = filename.substring(filename.lastIndexOf("."));
        InputStream inputStream = picture.getInputStream();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String name = uuid + postfix;
        String path = minIOTemplate.uploadImgFile("", name, inputStream);

        Material material = new Material();

        material.setUserId(ThreadContent.getId());
        material.setType(MediaConstants.MATERIAL_TYPE_IMAGE);
        material.setIsCollection(MediaConstants.MATERIAL_NOT_COLLECTION);
        material.setCreatedTime(new Date());
        material.setUrl(path);
        materialMapper.insert(material);
        return ResponseResult.okResult(material);
    }

    @Override
    public ResponseResult listMaterial(PageDto dto) {
        Integer cPage = dto.getPage();
        if (Objects.isNull(cPage)){
            cPage = 1;
        }
        Integer size = dto.getSize();
        if (Objects.isNull(size)||5==size){
            size = 10;
        }
        String collection = dto.getIsCollection();
        if (Objects.isNull(collection)){
            collection = "0";
        }
        Page<Material> page = new Page<>(cPage,size);
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Material::getUserId,ThreadContent.getId());
        if (StringUtils.isNotEmpty(collection)&&"1".equals(collection)){
            wrapper.lambda().eq(Material::getIsCollection,collection);
        }
        materialMapper.selectPage(page,wrapper);
        //3.结果返回
        ResponseResult responseResult = new PageResponseResult(cPage,size,(int)page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    /**
     * 收藏素材
     *
     * @param materialId 材料标识
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult collectMaterial(Integer materialId,Integer whetherCollect) {
        Material material = new Material();
        material.setId(materialId);
        material.setIsCollection(whetherCollect);
        materialMapper.updateById(material);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult deleteMaterial(Integer materialId) {
        List<Short> types = newsMaterialMapper.selectType(materialId);
        if (Objects.isNull(types)||types.size()==0){
            materialMapper.deleteById(materialId);
        }else {
            if (types.contains((short)1)&&types.contains((short)0)){
                return ResponseResult.errorResult(AppHttpCodeEnum.MATERIAL_HAS_BEEN_USED);
            }else if (types.contains((short)1)){
                return ResponseResult.errorResult(AppHttpCodeEnum.MATERIAL_USED_IN_COVER);
            }else {
                return ResponseResult.errorResult(AppHttpCodeEnum.MATERIAL_USED_IN_ARTICLE);
            }
        }
        return null;
    }
}
