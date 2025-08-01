package com.elysia.blogspringboot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("spring.jwt")
public class JwtProperties {
    private String adminSecretKey;
    private Long adminTtl;
    private String adminTokenName;
    private String userSecretKey;
    private Long userTtl;
    private String userTokenName;
}
