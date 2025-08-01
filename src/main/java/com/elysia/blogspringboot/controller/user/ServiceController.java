package com.elysia.blogspringboot.controller.user;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

// 后端改进代码
@RestController
@RequestMapping("/user/service")
@RequiredArgsConstructor
@Slf4j
public class ServiceController {
    private final OSS ossClient;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response, HttpServletRequest request) {
        log.info("下载文件请求: {}", fileName);
        try {
            // 安全检查：防止路径遍历攻击
            if (!isFileNameValid(fileName)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("非法文件名");
                return;
            }

            // 解码URL中的文件名
            fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);


            // 检查文件是否存在
            if (!ossClient.doesObjectExist(bucketName, fileName)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // 获取OSS对象
            OSSObject ossObject = ossClient.getObject(bucketName, fileName);

            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + URLEncoder.encode(getFileName(fileName), StandardCharsets.UTF_8));

            // 复制文件内容到响应输出流
            try (InputStream in = ossObject.getObjectContent();
                 OutputStream out = response.getOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            log.info("文件下载成功: {}", fileName);
        } catch (Exception e) {
            log.error("文件下载失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            try {
                // 返回友好的错误信息
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"文件下载失败，请重试\"}");
            } catch (IOException ioException) {
                log.error("设置错误响应失败", ioException);
            }
        }
    }

    // 安全检查：验证文件名是否合法
    private boolean isFileNameValid(String fileName) {
        // 简单检查：防止路径遍历攻击
        return !fileName.contains("..") && !fileName.startsWith("/") && !fileName.startsWith("\\");
    }

    private String getFileName(String objectName) {
        int lastIndex = objectName.lastIndexOf("/");
        return lastIndex != -1 ? objectName.substring(lastIndex + 1) : objectName;
    }
}
