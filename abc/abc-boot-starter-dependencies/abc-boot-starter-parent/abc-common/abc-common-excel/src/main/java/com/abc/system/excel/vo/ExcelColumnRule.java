package com.abc.system.excel.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;

import java.util.List;

/**
 * Excel自定义配置列规则
 * <pre>
 *     唯一编号|long|20|0|id,备注|string|200|0|memo
 * </pre>
 *
 * @Description ExcelColumnRule Excel自定义配置列规则
 * @Author [author_name]
 * @Date 2077/5/21 17:02
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class ExcelColumnRule {
    /**
     * 配置列名
     */
    private String columnName;

    /**
     * 字段配置类型（string/double/int/integer/long）
     */
    private String configType;

    /**
     * 数据长度
     */
    private Integer length;

    /**
     * 数据精度
     * <pre>
     * 1.EXCEL中单元格有两个基本类型：数值+文本，这里配置的精度将在excel单元格为数值格式时生效，与上方configType参数无关；
     *   1.1 也就是说，如果单元格没有被手动设置为文本类型，一旦单元格出现了浮点型数据，当前参数都将立刻生效触发检测；
     *   1.2 这也意味着，在大部分场景下，你可以单独为string精度指定为字符串长度（跳过检测），double指定自定义精度，int/long精度指定为0。
     * 2.例如：excel有一列name列用于表示用户姓名，但是该列某个单元格为数值型且内容为100.0123，此时精度参数会生效/触发检测。
     * </pre>
     */
    private Integer accuracy;

    /**
     * 真实字段名
     */
    private String realName;

    /**
     * CellType列表，apache.poi中类型（CellType.STRING, CellType.NUMERIC）
     */
    private List<CellType> cellTypeList;

    public ExcelColumnRule(String columnName, List<CellType> cellTypeList, String configType, Integer length, Integer accuracy, String realName) {
        this.columnName = columnName;
        this.cellTypeList = cellTypeList;
        this.configType = configType;
        this.length = length;
        this.accuracy = accuracy;
        this.realName = realName;
    }

    public ExcelColumnRule(String columnName, List<CellType> cellTypeList, Integer length, Integer accuracy, String realName) {
        this.columnName = columnName;
        this.cellTypeList = cellTypeList;
        this.length = length;
        this.accuracy = accuracy;
        this.realName = realName;
    }
}
