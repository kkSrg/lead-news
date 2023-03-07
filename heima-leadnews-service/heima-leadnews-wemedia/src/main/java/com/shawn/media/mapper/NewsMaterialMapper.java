package com.shawn.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.media.entities.NewsMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shawn
 * @date 2023年 01月 10日 11:46
 */
public interface NewsMaterialMapper extends BaseMapper<NewsMaterial> {
    void saveBatch(@Param("nms") List<NewsMaterial> nms);

    List<Short> selectType(Integer materialId);
}
