package com.elysia.blogspringboot.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.elysia.blogspringboot.properties.AliyunOssProperties;
import com.elysia.blogspringboot.untils.AliyunOssUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AliyunOssConfiguration{
    @Bean
    @ConditionalOnMissingBean
    public AliyunOssUtils aliOssUtil(AliyunOssProperties aliyunOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象：{}", aliyunOssProperties);
        log.info("获取AccessKeyId：{}", aliyunOssProperties.getAccessKeyId());
        log.info("获取AccessKeySecret：{}", aliyunOssProperties.getAccessKeySecret());
        return new AliyunOssUtils(aliyunOssProperties.getEndpoint(),
                aliyunOssProperties.getAccessKeyId(),
                aliyunOssProperties.getAccessKeySecret(),
                aliyunOssProperties.getBucketName());
    }
    @Bean
    public OSS ossClient(AliyunOssProperties aliyunOssProperties) {
        return new OSSClientBuilder().build(aliyunOssProperties.getEndpoint(),
                aliyunOssProperties.getAccessKeyId(),
                aliyunOssProperties.getAccessKeySecret());
    }
}
