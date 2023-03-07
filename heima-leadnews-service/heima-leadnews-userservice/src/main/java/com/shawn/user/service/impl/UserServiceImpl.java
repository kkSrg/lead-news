package com.shawn.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.entities.User;
import com.heima.utils.common.AppJwtUtil;
import com.heima.utils.common.MD5Utils;
import com.shawn.user.mapper.UserMapper;
import com.shawn.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author shawn
 * @date 2023年 01月 06日 11:25
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    public ResponseResult login(LoginDto dto) {
        //游客登录
        if (StringUtils.isEmpty(dto.getPhone())&&StringUtils.isEmpty(dto.getPassword())){
            //返回token  id = 0
            Map<String, Object> map = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(0l));
            return ResponseResult.okResult(map);
        }
        //空判断
        if (StringUtils.isEmpty(dto.getPhone())||StringUtils.isEmpty(dto.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR,"输入参数为空!");
        }
        //查询同名用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getPhone,dto.getPhone());
        //查询同名用户
        User user = userMapper.selectOne(wrapper);
        if (Objects.isNull(user)){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //对比密码
        String saltPassword = MD5Utils.encodeWithSalt(dto.getPassword(), user.getSalt());
        log.info("密码:{}",saltPassword);
        if (saltPassword.equals(user.getPassword())){
            //1.3 返回数据  jwt
            Map<String, Object> map = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(user.getId().longValue()));
            user.setSalt("");
            user.setPassword("");
            map.put("user", user);
            return ResponseResult.okResult(map);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
    }
}
