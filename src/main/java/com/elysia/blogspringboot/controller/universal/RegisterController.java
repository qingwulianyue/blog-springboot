package com.elysia.blogspringboot.controller.universal;

import com.elysia.blogspringboot.domain.entity.User;
import com.elysia.blogspringboot.result.Result;
import com.elysia.blogspringboot.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@Slf4j
public class RegisterController {
    private final IUserService userService;
    @GetMapping("/checkUsername")
    public Result<String> checkUsername(@RequestParam String username){
        log.info("用户名校验：{}",username);
        return userService.checkUsername(username);
    }
    @GetMapping("/checkEmail")
    public Result<String> checkEmail(@RequestParam String email){
        log.info("邮箱校验：{}",email);
        return userService.checkEmail(email);
    }
    @PostMapping
    public Result<String> submit(@RequestBody User user){
        log.info("用户注册：{}",user);
        return userService.checkUserRegister(user);
    }
}
