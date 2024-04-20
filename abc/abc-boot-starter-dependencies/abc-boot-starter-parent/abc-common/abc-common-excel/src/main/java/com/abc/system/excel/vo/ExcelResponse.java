package com.abc.system.excel.vo;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * Excel响应体
 *
 * @Description Excel响应体
 * @Author [author_name]
 * @Date 2077/5/21 17:06
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class ExcelResponse {
    /**
     * 标题行每个字段的显示名称
     */
    private Collection<String> displayTitle;

    /**
     * 逗号分隔的单元格数据
     */
    private Collection<String> displayData;

    /**
     * 单元格真实数据（单元格真实字段名:单元格数据）
     */
    private Collection<JSONObject> realData;

    public ExcelResponse(Collection<String> displayTitle,
                         Collection<String> displayData, Collection<JSONObject> realData) {
        this.displayTitle = displayTitle;
        this.displayData = displayData;
        this.realData = realData;
    }
}
