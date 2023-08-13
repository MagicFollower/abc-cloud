package com.abc.system.common.filegenerator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;

/**
 * Freemarker属性配置
 *
 * @Description Freemarker属性配置
 * @Author Trivis
 * @Date 2023/5/10 20:18
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "abc.file.generator")
public class FreemarkerProperties {

    /**
     * FTL模板字符集/编码，默认UTF-8
     */
    private String templateCharset = StandardCharsets.UTF_8.name();

    /**
     * FTL模板路径，默认/templates
     */
    private String templatePath = "/templates";

    /**
     * 静态资源路径（PDF模板使用）
     */
    private String staticSourcePath;
}
