package com.abc.system.excel.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;

import java.util.List;

/**
 * Excel自定义配置列规则
 *
 * @Description ExcelColumnRule Excel自定义配置列规则
 * @Author Trivis
 * @Date 2023/5/21 17:02
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class ExcelColumnRule {

    /**
     * CellType列表，apache.poi中类型（CellType.STRING, CellType.NUMERIC）
     */
    private List<CellType> cellTypeList;

    /**
     * 字段配置类型（string/double/int/integer/long）
     */
    private String configType;

    /**
     * 数据
     */
    private Integer length;

    /**
     * 数据精度
     */
    private Integer accuracy;

    /**
     * 真实字段名
     */
    private String realName;

    public ExcelColumnRule(List<CellType> cellTypeList, Integer length, Integer accuracy) {
        this.cellTypeList = cellTypeList;
        this.length = length;
        this.accuracy = accuracy;
    }

    public ExcelColumnRule(List<CellType> cellTypeList, String configType, Integer length, Integer accuracy) {
        this.cellTypeList = cellTypeList;
        this.configType = configType;
        this.length = length;
        this.accuracy = accuracy;
    }

    public ExcelColumnRule(List<CellType> cellTypeList, String configType, Integer length, Integer accuracy, String realName) {
        this.cellTypeList = cellTypeList;
        this.configType = configType;
        this.length = length;
        this.accuracy = accuracy;
        this.realName = realName;
    }

    public ExcelColumnRule(List<CellType> cellTypeList, Integer length, Integer accuracy, String realName) {
        this.cellTypeList = cellTypeList;
        this.length = length;
        this.accuracy = accuracy;
        this.realName = realName;
    }
}
