package com.heima.model.user.entities;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * APP用户信息表(User)实体类
 *
 * @author shawn
 * @since 2023-01-06 11:04:28
 */
@Data
@TableName("ap_user")
public class User implements Serializable {
    private static final long serialVersionUID = 403545832434042293L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 密码、通信等加密盐
     */
    private String salt;
    /**
     * 用户名
     */
    private String name;
    /**
     * 密码,md5加密
     */
    private String password;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像
     */
    private String image;
    /**
     * 0 男
            1 女
            2 未知
     */
    private Integer sex;
    /**
     * 0 未
            1 是
     */
    private Integer isCertification;
    /**
     * 是否身份认证
     */
    private Integer isIdentityAuthentication;
    /**
     * 0正常
            1锁定
     */
    private Integer status;
    /**
     * 0 普通用户
            1 自媒体人
            2 大V
     */
    private Integer flag;
    /**
     * 注册时间
     */
    private Date createdTime;


}

