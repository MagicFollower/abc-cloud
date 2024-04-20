package com.abc.system.excel.config;

import com.abc.system.excel.vo.ExcelRule;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Excel上传自定义配置
 * <pre>
 * 配置示例：
 *   abc:
 *      excel:
 *        rules:
 *          - templateCode: abc001
 *            titleNum: 1
 *            columns: id|long|20|0|id,备注|string|200|0|memo
 *          - templateCode: abc002
 *            titleNum: 2
 *            columns: id|string|20|0|id,备注|string|200|0|memo
 * </pre>
 *
 * @Description ExcelConfigProperties Excel上传自定义配置
 * @Author [author_name]
 * @Date 2077/5/21 16:14
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
