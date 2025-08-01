package com.elysia.blogspringboot.controller.universal;

import com.elysia.blogspringboot.domain.entity.User;
import com.elysia.blogspringboot.result.Result;
import com.elysia.blogspringboot.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final IUserService userService;
    @PostMapping
    public Result<String> login(@RequestBody User user) {
        log.info("用户登录：{}",user);
        return userService.checkUserLogin(user);
    }
}
