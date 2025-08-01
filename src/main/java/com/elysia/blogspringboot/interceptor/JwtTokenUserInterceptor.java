package com.elysia.blogspringboot.interceptor;

import com.elysia.blogspringboot.context.BaseContext;
import com.elysia.blogspringboot.enumeration.JwtClaimsEnum;
import com.elysia.blogspringboot.properties.JwtProperties;
import com.elysia.blogspringboot.untils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenUserInterceptor implements HandlerInterceptor {
    private final JwtProperties jwtProperties;
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        BaseContext.removeCurrentId();
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }
        //从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());
        //校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtils.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long id = Long.valueOf(claims.get(JwtClaimsEnum.USER_ID.getValue()).toString());
            BaseContext.setCurrentId(id);
            return true;
        } catch (Exception ex) {
            response.setStatus(444);
            return false;
        }
    }
}
