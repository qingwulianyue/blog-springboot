package com.elysia.blogspringboot.config;

import com.elysia.blogspringboot.interceptor.JwtTokenUserInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    private final JwtTokenUserInterceptor jwtTokenUserInterceptor;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //是否发送Cookie
                .allowCredentials(false)
                //放行哪些原始域
                .allowedOrigins("http://localhost:5175")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("*");
    }
    /**
     * 注册自定义拦截器
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**");
    }
}
