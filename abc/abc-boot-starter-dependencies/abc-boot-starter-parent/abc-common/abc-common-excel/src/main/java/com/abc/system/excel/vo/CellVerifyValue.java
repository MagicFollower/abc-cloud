package com.abc.system.excel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CellVerifyValue
 *
 * @Description CellVerifyValue 详细介绍
 * @Author [author_name]
 * @Date 2077/5/21 20:20
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellVerifyValue {

    private boolean verify;

    private Object value;

    private ExcelColumnRule rule;

    private String errorMsg;


    public CellVerifyValue(boolean verify, Object value, ExcelColumnRule rule) {
        this.verify = verify;
        this.value = value;
        this.rule = rule;
    }
}
