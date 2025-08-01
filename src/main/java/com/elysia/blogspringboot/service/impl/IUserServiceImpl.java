package com.elysia.blogspringboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elysia.blogspringboot.domain.entity.User;
import com.elysia.blogspringboot.domain.vo.LoginVo;
import com.elysia.blogspringboot.enumeration.JwtClaimsEnum;
import com.elysia.blogspringboot.mapper.UserMapper;
import com.elysia.blogspringboot.properties.JwtProperties;
import com.elysia.blogspringboot.result.Result;
import com.elysia.blogspringboot.service.IUserProfileService;
import com.elysia.blogspringboot.service.IUserService;
import com.elysia.blogspringboot.untils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final JwtProperties jwtProperties;
    private final IUserProfileService userProfileService;
    @Override
    public Result<String> checkUsername(String username) {
        if (lambdaQuery().eq(User::getUsername, username).exists())
            return Result.error("用户名已存在");
        return Result.success();
    }

    @Override
    public Result<String> checkEmail(String email) {
        if (lambdaQuery().eq(User::getEmail, email).exists())
            return Result.error("邮箱已存在");
        return Result.success();
    }

    @Override
    public Result checkUserLogin(User user) {
        if (!lambdaQuery().eq(User::getUsername, user.getUsername()).exists()) {
            return Result.error("用户名不存在");
        }
        if (!lambdaQuery().eq(User::getPassword, user.getPassword()).exists()) {
            return Result.error("密码错误");
        }
        User userSave = lambdaQuery().eq(User::getUsername, user.getUsername()).one();
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsEnum.USER_ID.getValue(), userSave.getId());
        String jwt = JwtUtils.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        LoginVo loginVo = LoginVo.builder()
                .id(userSave.getId())
                .tokenName(jwtProperties.getUserTokenName())
                .token(jwt)
                .build();
        return Result.success(loginVo);
    }

    @Override
    public Result<String> checkUserRegister(User user) {
        if (lambdaQuery().eq(User::getUsername, user.getUsername()).exists())
            return Result.error("用户名已存在");
        if (lambdaQuery().eq(User::getEmail, user.getEmail()).exists())
            return Result.error("邮箱已存在");
        save(user);
        userProfileService.init(user.getId(), user.getUsername(), user.getEmail());
        return Result.success();
    }
}
