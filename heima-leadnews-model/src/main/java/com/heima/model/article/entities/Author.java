package com.heima.model.article.entities;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * APP文章作者信息表(Author)实体类
 *
 * @author shawn
 * @since 2023-01-06 17:18:12
 */
@Data
@TableName("ap_author")
public class Author implements Serializable {
    private static final long serialVersionUID = 965805547045900170L;
    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER)
    private Integer id;
    /**
     * 作者名称
     */
    private String name;
    /**
     *      0 爬取数据
            1 签约合作商
            2 平台自媒体人
            
     */
    private Integer type;
    /**
     * 社交账号ID
     */
    private Integer userId;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 自媒体账号
     */
    private Integer wmUserId;


}

