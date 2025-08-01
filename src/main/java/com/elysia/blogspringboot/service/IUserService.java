package com.elysia.blogspringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elysia.blogspringboot.domain.entity.User;
import com.elysia.blogspringboot.result.Result;

public interface IUserService extends IService<User> {
    Result<String> checkUsername(String username);
    Result<String> checkEmail(String email);
    Result<String> checkUserLogin(User user);
    Result<String> checkUserRegister(User user);
}
