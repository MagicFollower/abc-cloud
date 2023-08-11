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

    /**
     * <pre>
     * 移除指定警告：“必须在有效 Spring Bean 中定义自动装配成员(@Component|@Service|…)”
     *   1.原因：该类已在com.abc.system.common.minio.config.MinioConfig中通过@Bean方式完成注入。
     * </pre>
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final MinioClient minioClient;

    @Bean
    MinioService minioService() {
        return new MinioService(minioConfig, minioClient);
    }
}
