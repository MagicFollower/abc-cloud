package com.abc.system.excel.vo;

import lombok.Data;

/**
 * Excel自定义配置规则
 *
 * @Description ExcelRule Excel自定义配置规则
 * @Author [author_name]
 * @Date 2077/5/21 17:00
 * @Version 1.0
 */
@Data
public class ExcelRule {

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 标题行号(模板配置项，未配置时将使用默认值1，第一行为标题行)
     * 1.默认=1, 如果设置为3，会解析第三行为标题行，前两行将被忽略；
     * 2.如果设置为3，但是第3行为空行，将会自动向下寻找第一个非空行作为标题行。
     */
    private int titleNum = 1;

    /**
     * 列配置规则
     *
     * @see ExcelColumnRule
     */
    private String columns;

}
