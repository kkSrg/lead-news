package com.heima.model.media.entities;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * 自媒体图文素材信息表(Material)实体类
 *
 * @author shawn
 * @since 2023-01-09 11:57:04
 */
@Data
@TableName("wm_material")
public class Material implements Serializable {
    private static final long serialVersionUID = -22660883185766123L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 自媒体用户ID
     */
    private Integer userId;
    /**
     * 图片地址
     */
    private String url;
    /**
     * 素材类型
            0 图片
            1 视频
     */
    private Integer type;
    /**
     * 是否收藏
     */
    private Integer isCollection;
    /**
     * 创建时间
     */
    private Date createdTime;


}

