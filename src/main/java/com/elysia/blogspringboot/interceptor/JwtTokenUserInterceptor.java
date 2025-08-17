package com.elysia.blogspringboot.interceptor;

import com.elysia.blogspringboot.context.BaseContext;
import com.elysia.blogspringboot.enumeration.JwtClaimsEnum;
import com.elysia.blogspringboot.properties.JwtProperties;
import com.elysia.blogspringboot.untils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenUserInterceptor implements HandlerInterceptor {
    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, String> redisTemplate;
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        BaseContext.removeCurrentId();
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod))
            //当前拦截到的不是动态方法，直接放行
            return true;
        //从Cookie中获取令牌
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                //匹配存储JWT的Cookie名称
                if (jwtProperties.getUserTokenName().equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        //校验令牌
        try {
            log.info("jwt校验:{}", token);
            // 解析令牌并判断是否过期
            Claims claims = JwtUtils.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long id = Long.valueOf(claims.get(JwtClaimsEnum.USER_ID.getValue()).toString());
            // 检查该用户令牌是否存在于黑名单
            String key = "user:blacklist:" + token;
            if (redisTemplate.hasKey(key)) {
                log.warn("用户令牌已存在黑名单中:{}", key);
                response.setStatus(444);
                return false;
            }
            BaseContext.setCurrentId(id);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // 令牌过期
            log.warn("JWT令牌已过期:{}", e.getMessage());
            response.setStatus(444);
            return false;
        }
    }
}
