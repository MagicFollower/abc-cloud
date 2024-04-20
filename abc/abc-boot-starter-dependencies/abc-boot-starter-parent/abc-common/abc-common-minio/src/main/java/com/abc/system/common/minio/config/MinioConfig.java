package com.abc.system.common.minio.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * MinioConfig
 *
 * @Description MinioConfig 详细介绍
 * @Author [author_name]
 * @Date 2077/5/2 12:15
 * @Version 1.0
 */
@Data
@ConfigurationProperties("minio")
public class MinioConfig {
    /**
     * Minio单节点URL
     */
    private String endpoint;
    /**
     * Minio AccessKey
     */
    private String accessKey;
    /**
     * Minio SecretKey
     */
    private String secretKey;
    /**
     * Minio BucketName
     */
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
