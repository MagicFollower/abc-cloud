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
 *     excel:
 *       rules:
 *         - templateCode: abc001
 *           columns: id|long|20|0|id,备注|string|200|0|memo
 *         - templateCode: abc002
 *           columns: id|string|20|0|id,备注|string|200|0|memo
 *       title-num: 2
 * </pre>
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
     * 标题行号
     * 1.默认=1, 如果设置为3，会解析第三行为标题行，前两行将被忽略；
     * 2.如果设置为3，但是第3行为空行，将会自动向下寻找第一个非空行作为标题行。
     */
    private int titleNum = 1;

    /**
     * 字段规则（列表）
     */
    private List<ExcelRule> rules;
}
