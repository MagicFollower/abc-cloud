package com.abc.system.excel.config;

import com.abc.system.excel.vo.ExcelRule;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Excel上传自定义配置
 *
 * @Description ExcelConfigProperties Excel上传自定义配置
 * @Author Trivis
 * @Date 2023/5/21 16:14
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "abc.excel")
@Data
public class ExcelConfigProperties {

    /**
     * 字段规则（列表）
     */
    private List<ExcelRule> rules;
}
