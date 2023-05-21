package com.abc.system.excel.vo;

import lombok.Data;

/**
 * Excel自定义配置规则
 *
 * @Description ExcelRule Excel自定义配置规则
 * @Author Trivis
 * @Date 2023/5/21 17:00
 * @Version 1.0
 */
@Data
public class ExcelRule {

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 列配置规则
     *
     * @see ExcelColumnRule
     */
    private String columns;

}
