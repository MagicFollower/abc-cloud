package com.abc.system.excel.autoconfig;

import com.abc.system.excel.config.ExcelConfigProperties;
import com.abc.system.excel.service.ExcelFileService;
import com.abc.system.excel.service.impl.ExcelFileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

/**
 * ExcelConfig自动配置类
 * <pre>
 * 1.自动注入名称为excelFileService的ExcelFileService实例
 * </pre>
 *
 * @Description ExcelConfigAutoConfiguration 自动配置类
 * @Author [author_name]
 * @Date 2077/5/21 17:03
 * @Version 1.0
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ExcelConfigProperties.class)
@ConditionalOnClass(MultipartFile.class)
public class ExcelConfigAutoConfiguration {

    private final ExcelConfigProperties excelConfigProperties;

    /**
     * 实例化Excel上传解析服务
     *
     * @return ExcelFileService
     */
    @Bean("excelFileService")
    public ExcelFileService excelFileService() {
        return new ExcelFileServiceImpl(excelConfigProperties);
    }
}
