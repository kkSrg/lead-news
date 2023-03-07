package com.heima.model.media.entities;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * 自媒体图文引用素材信息表(NewsMaterial)实体类
 *
 * @author shawn
 * @since 2023-01-09 11:57:04
 */
@Data
@TableName("wm_news_material")
public class NewsMaterial implements Serializable {
    private static final long serialVersionUID = 640515000356820559L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 素材ID
     */
    private Integer materialId;
    /**
     * 图文ID
     */
    private Integer newsId;
    /**
     * 引用类型
            0 内容引用
            1 主图引用
     */
    private Integer type;
    /**
     * 引用排序
     */
    private Integer ord;


}

