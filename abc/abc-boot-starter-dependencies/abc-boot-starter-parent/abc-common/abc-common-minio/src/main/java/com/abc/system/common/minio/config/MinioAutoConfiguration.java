package com.abc.system.common.minio.config;

import com.abc.system.common.minio.util.MinioService;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinioAutoConfiguration
 *
 * @Description MinioAutoConfiguration 详细介绍
 * @Author Trivis
 * @Date 2023/5/2 12:25
 * @Version 1.0
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(MinioService.class)
@EnableConfigurationProperties(MinioConfig.class)
public class MinioAutoConfiguration {
    private final MinioConfig minioConfig;
    private final MinioClient minioClient;

    @Bean
    MinioService minioService() {
        return new MinioService(minioConfig, minioClient);
    }
}
