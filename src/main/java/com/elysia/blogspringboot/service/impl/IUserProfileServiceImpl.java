package com.elysia.blogspringboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elysia.blogspringboot.context.BaseContext;
import com.elysia.blogspringboot.domain.entity.User;
import com.elysia.blogspringboot.domain.entity.UserProfile;
import com.elysia.blogspringboot.mapper.UserProfileMapper;
import com.elysia.blogspringboot.result.Result;
import com.elysia.blogspringboot.service.IUserProfileService;
import com.elysia.blogspringboot.service.IUserService;
import com.elysia.blogspringboot.untils.AliyunOssUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IUserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile> implements IUserProfileService {
    private final AliyunOssUtils ossUtils;
    @Override
    public void init(Long userId, String username, String email) {
        save(UserProfile.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .build()
        );
    }

    @Override
    public Result<UserProfile> updateUserAvatar(byte[] bytes, String objectName) {
        String fileUrl = ossUtils.upload(bytes, objectName);
        lambdaUpdate()
                .eq(UserProfile::getUserId, BaseContext.getCurrentId())
                .set(UserProfile::getAvatarUrl, fileUrl)
                .update();
        return Result.success(getById(BaseContext.getCurrentId()));
    }

}
