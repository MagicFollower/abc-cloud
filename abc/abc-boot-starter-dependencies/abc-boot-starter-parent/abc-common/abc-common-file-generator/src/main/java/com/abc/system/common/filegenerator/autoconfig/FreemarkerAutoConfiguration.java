package com.abc.system.common.filegenerator.autoconfig;

import com.abc.system.common.filegenerator.config.FreemarkerProperties;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.util.Locale;

/**
 * Freemarker自动配置类
 *
 * @Description Freemarker自动配置类
 * @Author Trivis
 * @Date 2023/5/10 20:20
 * @Version 1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(FreemarkerProperties.class)
@ConditionalOnClass(Template.class)
@RequiredArgsConstructor
public class FreemarkerAutoConfiguration {

    private final FreemarkerProperties freemarkerProperties;

    /**
     * Freemarker配置初始化
     *
     * @return freemarker.template.Configuration
     */
    @Bean
    public freemarker.template.Configuration configuration() {
        freemarker.template.Configuration configuration =
                new freemarker.template.Configuration(freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        // FTL模板位置
        configuration.setClassForTemplateLoading(FreemarkerAutoConfiguration.class, freemarkerProperties.getTemplatePath());
        // FTL字符集/编码
        configuration.setEncoding(Locale.CHINA, freemarkerProperties.getTemplateCharset());
        return configuration;
    }

    /**
     * 初始化IText实例（PDF渲染）
     *
     * @return ITextRenderer
     */
    @Bean
    public ITextRenderer iTextRenderer() {
        return new ITextRenderer();
    }
}
