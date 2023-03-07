package com.shawn.media.service;

import com.heima.model.media.dtos.LoginDto;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @author shawn
 * @date 2023年 01月 06日 10:45
 */

public interface UserService {
     ResponseResult login(LoginDto dto);
}
