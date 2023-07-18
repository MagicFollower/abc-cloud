package com.abc.system.excel.helper;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.business.ValidateException;
import com.abc.system.common.helper.SpringHelper;
import com.abc.system.excel.config.ExcelConfigProperties;
import com.abc.system.excel.vo.CellVerifyValue;
import com.abc.system.excel.vo.ExcelColumnRule;
import com.abc.system.excel.vo.ExcelRule;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ResolveExcelHelper
 *
 * @Description ResolveExcelHelper 详细介绍
 * @Author Trivis
 * @Date 2023/5/21 17:10
 * @Version 1.0
 */
public class ResolveExcelHelper {

    private static final DataFormatter dataFormatter = new DataFormatter();

    private ResolveExcelHelper() {
    }

    /**
     * 取得文件上传配置信息
     *
     * @return ExcelConfigProperties
     */
    private static ExcelConfigProperties getExcelConfig() {
        return SpringHelper.getBean(ExcelConfigProperties.class);
    }

    /**
     * 解析Excel列配置规则
     *
     * @return {@code Map<String, ExcelColumnRule>} 映射格式 → templateCode_stringCellValue: ExcelColumnRule实体
     * @throws ValidateException 校验异常
     */
    public static Map<String, ExcelColumnRule> resolveCellColumnRule() throws ValidateException {
        Map<String, ExcelColumnRule> result = new HashMap<>();
        ExcelConfigProperties excelConfig = getExcelConfig();
        List<ExcelRule> rules = excelConfig.getRules();
        for (ExcelRule rule : rules) {
            for (String str : rule.getColumns().split(",")) {
                String[] split = str.split("\\|");
                if (split.length != 5) {
                    throw new ValidateException(SystemRetCodeConstants.EXCEL_RULE_ERROR);
                }
                List<String> data = new ArrayList<>(split.length);
                Collections.addAll(data, split);
                switch (data.get(1).toLowerCase()) {
                    case "string":
                    case "double":
                        result.put(rule.getTemplateCode() + "_" + data.get(0),
                                ExcelColumnRuleWrapper(null, data.get(2), data.get(3), data.get(4), CellType.STRING, CellType.NUMERIC));
                        break;
                    case "int":
                    case "integer":
                    case "long":
                        result.put(rule.getTemplateCode() + "_" + data.get(0),
                                ExcelColumnRuleWrapper(data.get(1).toLowerCase(), data.get(2), data.get(3), data.get(4), CellType.STRING, CellType.NUMERIC));
                        break;
                    default:
                        throw new ValidateException(SystemRetCodeConstants.EXCEL_RULE_ERROR);
                }

            }
        }
        return result;
    }

    /**
     * ExcelColumnRule列规则包装器，使用参数填充ExcelColumnRule
     *
     * @param configType 字段配置类型（string/double/int/integer/long）
     * @param length     字段长度
     * @param accuracy   精度
     * @param realName   真实字段名
     * @param cellTypes  CellType变长数组
     * @return {@code ExcelColumnRule} Excel列配置规则
     */
    public static ExcelColumnRule ExcelColumnRuleWrapper(String configType, String length, String accuracy, String realName, CellType... cellTypes) {
        if (StringUtils.isEmpty(length)) throw new ValidateException(SystemRetCodeConstants.EXCEL_RULE_ERROR);
        Integer len = Integer.parseInt(length);
        int accuracyInt = 0;
        if (StringUtils.isNotEmpty(accuracy)) {
            accuracyInt = Integer.parseInt(accuracy);
        }
        if (StringUtils.isEmpty(configType)) {
            return new ExcelColumnRule(Arrays.asList(cellTypes), len, accuracyInt, realName);
        } else {
            return new ExcelColumnRule(Arrays.asList(cellTypes), configType, len, accuracyInt, realName);
        }

    }

    /**
     * 判断Row是否为空
     *
     * @param row Row
     * @return true为空；false不为空
     */
    public static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                // 5.0将移除这个API，不再推荐你使用String类型统一获取任何类型数据, 请使用new DataFormatter().formatCellValue(cell);
                // cell.setCellType(CellType.STRING);
                // cell.getStringCellValue()
                if (!StringUtils.isEmpty(dataFormatter.formatCellValue(cell))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 解析Excel标题，填充两个Map（真实字段Map+显示字段Map）
     *
     * @param row             标题行
     * @param templateCode    模板编码
     * @param ruleMap         <pre>
     *                                                                                                                                           规则Map，用于解析字段真实名称，填充displayFieldMap;
     *                                                                                                                                             → Map中数据格式：templateCode_stringCellValue: ExcelColumnRule实体
     *                                                                                                                                           </pre>
     * @param realFieldMap    realFieldMap 真实字段Map
     * @param displayFieldMap displayFieldMap 显示字段Map
     */
    public static void resolveExcelTitle(Row row, String templateCode, Map<String, ExcelColumnRule> ruleMap,
                                         Map<Integer, String> realFieldMap, Map<Integer, String> displayFieldMap) {
        for (Cell cell : row) {
            int columnIndex = cell.getColumnIndex();
            String stringCellValue = dataFormatter.formatCellValue(cell);
            if (StringUtils.isNotEmpty(stringCellValue)) {
                ExcelColumnRule excelColumnRule = ruleMap.get(templateCode + "_" + stringCellValue);
                if (excelColumnRule == null) {
                    // Excel中字段与配置字段不匹配
                    throw new ValidateException(SystemRetCodeConstants.EXCEL_COLUMN_MISMATCH);
                }
                realFieldMap.put(columnIndex, excelColumnRule.getRealName());
                displayFieldMap.put(columnIndex, stringCellValue);
            }
        }
    }

    /**
     * Cell类型检测
     *
     * @param templateCode       模板编码
     * @param displayTitleMap    显示标题Map
     * @param excelColumnRuleMap excelColumnRuleMap
     * @param cell               Cell
     * @return CellVerifyValue Cell数据验证实体
     */
    public static CellVerifyValue verifyCellType(String templateCode, Map<Integer, String> displayTitleMap,
                                                 Map<String, ExcelColumnRule> excelColumnRuleMap, Cell cell) {
        ExcelColumnRule excelColumnRule = excelColumnRuleMap.get(templateCode + "_" +
                displayTitleMap.get(cell.getColumnIndex()).toLowerCase());
        if (!excelColumnRule.getCellTypeList().contains(cell.getCellType())) {
            return new CellVerifyValue(false, null, excelColumnRule);
        }
        return new CellVerifyValue(true, null, excelColumnRule);
    }

    /**
     * @param cell Cell
     * @param rule ExcelColumnRule
     * @return CellVerifyValue Cell数据验证实体
     */
    public static CellVerifyValue getCellValue(Cell cell, ExcelColumnRule rule) {
        CellVerifyValue result;
        switch (cell.getCellType()) {
            // 数字
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    String dataFormatString = cell.getCellStyle().getDataFormatString();
                    result = new CellVerifyValue(true, new SimpleDateFormat(dataFormatString)
                            .format(DateUtil.getJavaDate(cell.getNumericCellValue())), rule);
                } else {
                    BigDecimal data = new BigDecimal(String.valueOf(cell.getNumericCellValue())).stripTrailingZeros();
                    int scale = data.scale();
                    int length = data.toPlainString().length();
                    if (scale > rule.getAccuracy() || length > rule.getLength()) {
                        result = new CellVerifyValue(false, cell.getNumericCellValue(), rule, "精度或长度异常");
                    } else {
                        if ("int".equals(rule.getConfigType()) || "long".equals(rule.getConfigType()) ||
                                "integer".equals(rule.getConfigType())) {
                            result = new CellVerifyValue(true, Long.parseLong(dataFormatter.formatCellValue(cell)), rule);
                        } else {
                            result = new CellVerifyValue(true, cell.getNumericCellValue(), rule);
                        }
                    }

                }
                break;
            // 字符串
            case STRING:
                String data = cell.getStringCellValue();
                if (StringUtils.isNotEmpty(data) && data.length() > rule.getLength()) {
                    result = new CellVerifyValue(false, data, rule, "精度出错");
                } else {
                    if (!StringUtils.isEmpty(rule.getConfigType()) &&
                            "long".equalsIgnoreCase(rule.getConfigType())) {
                        try {
                            result = new CellVerifyValue(true, Long.parseLong(data), rule);
                        } catch (NumberFormatException e) {
                            result = new CellVerifyValue(true, data, rule);
                        }
                    } else {
                        result = new CellVerifyValue(true, data, rule);
                    }
                }
                break;
            // 空值
            case BLANK:
                result = new CellVerifyValue(true, null, rule);
                break;
            default:
                result = new CellVerifyValue(false, null, rule, "未知类型");
                break;
        }
        return result;
    }


    /**
     * 将iterator转换为List
     *
     * @param iterator 迭代器
     * @param <T>      T
     * @return {@code List<T>}
     */
    public static <T> List<T> copyIterator(Iterator<T> iterator) {
        List<T> copyList = new ArrayList<>(16);
        while (iterator.hasNext()) {
            copyList.add(iterator.next());
        }

        return copyList;
    }

}

