package com.abc.system.common.filegenerator.autoconfig;

import com.abc.system.common.filegenerator.config.FreemarkerProperties;
import com.abc.system.common.filegenerator.service.GenerateFileService;
import com.abc.system.common.filegenerator.service.impl.GenerateFileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 生成文件服务自动配置类
 *
 * @Description 生成文件服务自动配置类（将GeneratorFileService注入到SpringIOC容器中）
 * @Author [author_name]
 * @Date 2077/5/10 21:03
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(FreemarkerProperties.class)
@ConditionalOnClass(GenerateFileService.class)
public class GenerateFileAutoConfiguration {

    @Bean("generateFileService")
    public GenerateFileService generatorFileService() {
        return new GenerateFileServiceImpl();
    }
}
