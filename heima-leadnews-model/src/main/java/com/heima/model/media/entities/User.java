package com.heima.model.media.entities;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
/**
 * 自媒体用户信息表(User)实体类
 *
 * @author shawn
 * @since 2023-01-09 11:57:04
 */
@Data
@TableName("wm_user")
public class User implements Serializable {
    private static final long serialVersionUID = 649009694069961726L;
    /**
     * 主键
     */
    private Integer id;
    
    private Integer apUserId;
    
    private Integer apAuthorId;
    /**
     * 登录用户名
     */
    private String name;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String image;
    /**
     * 归属地
     */
    private String location;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 状态
            0 暂时不可用
            1 永久不可用
            9 正常可用
     */
    private Object status;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 账号类型
            0 个人 
            1 企业
            2 子账号
     */
    private Integer type;
    /**
     * 运营评分
     */
    private Integer score;
    /**
     * 最后一次登录时间
     */
    private Date loginTime;
    /**
     * 创建时间
     */
    private Date createdTime;


}

