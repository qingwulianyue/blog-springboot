package com.elysia.blogspringboot.handler;

import com.elysia.blogspringboot.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<String> handleOtherExceptions(Exception ex) {
        log.error("其他异常：", ex);
        return Result.error("系统异常，请稍后重试");
    }
}
