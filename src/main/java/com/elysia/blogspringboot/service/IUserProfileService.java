package com.elysia.blogspringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elysia.blogspringboot.domain.entity.UserProfile;
import com.elysia.blogspringboot.result.Result;

public interface IUserProfileService extends IService<UserProfile> {
    void init(Long userId, String username, String email);
    Result<UserProfile> updateUserAvatar(byte[] bytes, String objectName);
}
