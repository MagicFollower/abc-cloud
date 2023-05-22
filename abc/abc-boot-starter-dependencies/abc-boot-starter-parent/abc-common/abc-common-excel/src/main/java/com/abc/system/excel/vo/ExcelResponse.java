package com.abc.system.excel.vo;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * Excel响应体
 *
 * @Description Excel响应体
 * @Author Trivis
 * @Date 2023/5/21 17:06
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
    private List<String> displayData;

    /**
     * 单元格真实数据（单元格真实字段名:单元格数据）
     */
    private List<JSONObject> realData;

    public ExcelResponse(Collection<String> displayTitle, List<String> displayData, List<JSONObject> realData) {
        this.displayTitle = displayTitle;
        this.displayData = displayData;
        this.realData = realData;
    }
}
