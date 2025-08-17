package com.elysia.blogspringboot.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate<String, String> redisTemplate (RedisConnectionFactory factory) {
        log.info("正在初始化字符串专用 RedisTemplate 连接");
        // 创建针对字符串的 RedisTemplate
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory (factory);
        // 使用 StringRedisSerializer 作为默认序列化器
        StringRedisSerializer stringSerializer = new StringRedisSerializer ();
        // 设置 key 的序列化方式
        template.setKeySerializer (stringSerializer);
        // 设置 value 的序列化方式（字符串）
        template.setValueSerializer (stringSerializer);
        // 设置 hash 结构的 key 和 value 序列化方式
        template.setHashKeySerializer (stringSerializer);
        template.setHashValueSerializer (stringSerializer);
        // 初始化模板
        template.afterPropertiesSet ();
        return template;
    }
}
