package com.shawn.media.controller.v1;

import com.heima.model.media.dtos.LoginDto;
import com.heima.model.common.dtos.ResponseResult;
import com.shawn.media.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**LoginDto
 * @author shawn
 * @date 2023年 01月 06日 10:41
 */
@RestController
@RequestMapping("/login")
public class WeMediaLoginController {
    @Resource
    private UserService userService;

    @PostMapping("/in")
    public ResponseResult login(@RequestBody LoginDto dto) {
       return userService.login(dto);
    }
}
