package com.abc.system.excel.vo;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Row;

import java.util.Collection;
import java.util.List;

/**
 * eXCELrESPONSE
 *
 * @Description eXCELrESPONSE 详细介绍
 * @Author Trivis
 * @Date 2023/5/21 17:06
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class ExcelResponse {
    private Collection<String> displayTitle;

    private List<String> displayData;

    private List<JSONObject> instore;

    private List<Row> titleRows;

    public ExcelResponse(Collection<String> displayTitle, List<String> displayData, List<JSONObject> instore, List<Row> titleRows) {
        this.displayTitle = displayTitle;
        this.displayData = displayData;
        this.instore = instore;
        this.titleRows = titleRows;
    }
}
