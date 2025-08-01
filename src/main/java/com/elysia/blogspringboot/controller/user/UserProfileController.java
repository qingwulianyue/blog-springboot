package com.elysia.blogspringboot.controller.user;

import com.elysia.blogspringboot.context.BaseContext;
import com.elysia.blogspringboot.domain.entity.UserProfile;
import com.elysia.blogspringboot.result.Result;
import com.elysia.blogspringboot.service.IUserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/user/profile")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {
    private final IUserProfileService userProfileService;
    @GetMapping
    public Result<UserProfile> getProfile() {
        log.info("获取用户信息");
        return Result.success(userProfileService.getById(BaseContext.getCurrentId()));
    }
    @PutMapping
    public Result<UserProfile> updateProfile(@RequestBody UserProfile userProfile) {
        log.info("更新用户信息", userProfile);
        userProfile.setUserId(BaseContext.getCurrentId());
        return Result.success(userProfileService.updateById(userProfile) ? userProfile : null);
    }
    @PostMapping("avatar")
    public Result<UserProfile> updateAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("更新用户头像");
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀
        String extension = null;
        if (originalFilename != null)
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + extension;
        return userProfileService.updateUserAvatar(file.getBytes(), fileName);
    }
}
