package com.heima.model.user.dtos;

import lombok.Data;

/**
 * @author shawn
 * @date 2023年 01月 06日 10:43
 */
@Data
public class LoginDto {

    /**
     * 电话
     */
    private String phone;
    /**
     * 密码
     */
    private String password;
}
