package com.elysia.blogspringboot.enumeration;

import lombok.Getter;

/**
 * 用户id map的key枚举
 */
@Getter
public enum JwtClaimsEnum {
    USER_ID("user_id","用户id"),
    ADMIN_ID("admin_id","管理员id");

    private final String value;
    private String desc;

    JwtClaimsEnum(String value, String desc) {
        this.value = value;
    }

}
