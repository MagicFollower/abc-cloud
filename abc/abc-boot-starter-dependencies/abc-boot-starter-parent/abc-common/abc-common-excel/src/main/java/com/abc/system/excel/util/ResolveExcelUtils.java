package com.abc.system.excel.util;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.business.ValidateException;
import com.abc.system.common.helper.SpringHelper;
import com.abc.system.excel.config.ExcelConfigProperties;
import com.abc.system.excel.vo.CellVerifyValue;
import com.abc.system.excel.vo.ExcelColumnRule;
import com.abc.system.excel.vo.ExcelRule;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ExcelHelper解析器
 *
 * @Description ResolveExcelHelper 详细介绍
 * @Author [author_name]
 * @Date 2077/5/21 17:10
 * @Version 1.0
 */
public class ResolveExcelUtils {

    private static final DataFormatter dataFormatter = new DataFormatter();

    private ResolveExcelUtils() {
    }

    /**
     * 取得文件上传配置信息
     *
     * @return ExcelConfigProperties
     */
    private static ExcelConfigProperties getExcelConfig() {
        return SpringHelper.getBean(ExcelConfigProperties.class);
    }

    // /**
    //  * 解析Excel列配置规则（所有）
    //  *
    //  * @return {@code Map<String, ExcelColumnRule>} 映射格式 → templateCode_stringCellValue: ExcelColumnRule实体
    //  * @throws ValidateException 校验异常
    //  */
    // public static Map<String, ExcelColumnRule> resolveCellColumnRule() throws ValidateException {
    //     Map<String, ExcelColumnRule> result = new HashMap<>();
    //     ExcelConfigProperties excelConfig = getExcelConfig();
    //     List<ExcelRule> rules = excelConfig.getRules();
    //     for (ExcelRule rule : rules) {
    //         for (String str : rule.getColumns().split(",")) {
    //             String[] split = str.split("\\|");
    //             if (split.length != 5) {
    //                 throw new ValidateException(SystemRetCodeConstants.EXCEL_RULE_ERROR);
    //             }
    //             List<String> data = new ArrayList<>(split.length);
    //             Collections.addAll(data, split);
    //             switch (data.get(1).toLowerCase()) {
    //                 case "string":
    //                 case "double":
    //                     result.put(rule.getTemplateCode() + "_" + data.get(0),
    //                             ExcelColumnRuleWrapper(data.get(0), null, data.get(2), data.get(3), data.get(4), CellType.STRING, CellType.NUMERIC));
    //                     break;
    //                 case "int":
    //                 case "integer":
    //                 case "long":
    //                     result.put(rule.getTemplateCode() + "_" + data.get(0),
    //                             ExcelColumnRuleWrapper(data.get(0), data.get(1).toLowerCase(), data.get(2), data.get(3), data.get(4), CellType.STRING, CellType.NUMERIC));
    //                     break;
    //                 default:
    //                     throw new ValidateException(SystemRetCodeConstants.EXCEL_RULE_ERROR);
    //             }
    //         }
    //     }
    //     return result;
    // }

    /**
     * 解析Excel列配置规则（指定templateCode）
     *
     * @return {@code Map<String, ExcelColumnRule>} 映射格式 → templateCode_stringCellValue: ExcelColumnRule实体
     * @throws ValidateException 校验异常
     */
    public static Map<String, ExcelColumnRule> resolveCellColumnRule(String templateCode) throws ValidateException {
        Map<String, ExcelColumnRule> result = new HashMap<>();
        ExcelConfigProperties excelConfig = getExcelConfig();
        List<ExcelRule> rules = excelConfig.getRules();
        rules = rules.stream().filter(rule -> templateCode.equals(rule.getTemplateCode())).collect(Collectors.toList());
        if (rules.isEmpty()) {
            throw new ValidateException(SystemRetCodeConstants.EXCEL_TEMPLATE_CODE_NOT_CONFIG);
        } else if (rules.size() > 1) {
            throw new ValidateException(SystemRetCodeConstants.EXCEL_TEMPLATE_CODE_DUPLICATE_CONFIG);
        }
        ExcelRule rule = rules.get(0);
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
                case "int":
                case "integer":
                case "long":
                case "date":
                    result.put(rule.getTemplateCode() + "_" + data.get(0),
                            ExcelColumnRuleWrapper(data.get(0), data.get(1).toLowerCase(), data.get(2), data.get(3), data.get(4), CellType.STRING, CellType.NUMERIC));
                    break;
                default:
                    throw new ValidateException(SystemRetCodeConstants.EXCEL_RULE_ERROR);
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
    public static ExcelColumnRule ExcelColumnRuleWrapper(String columnName, String configType, String length, String accuracy, String realName, CellType... cellTypes) {
        if (StringUtils.isEmpty(length)) throw new ValidateException(SystemRetCodeConstants.EXCEL_RULE_ERROR);
        Integer len = Integer.parseInt(length);
        int accuracyInt = 0;
        if (StringUtils.isNotEmpty(accuracy)) {
            accuracyInt = Integer.parseInt(accuracy);
        }
        if (StringUtils.isEmpty(configType)) {
            return new ExcelColumnRule(columnName, Arrays.asList(cellTypes), len, accuracyInt, realName);
        } else {
            return new ExcelColumnRule(columnName, Arrays.asList(cellTypes), configType, len, accuracyInt, realName);
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
     * @param ruleMap         规则Map
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
        ExcelColumnRule excelColumnRule = excelColumnRuleMap.get(templateCode + "_" + displayTitleMap.get(cell.getColumnIndex()).toLowerCase());
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
            // 数值类型单元格
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 时间解析
                    // 1.这里对导入excel的时间格式进行硬性限制：可自定义但必须被excel中解析为时间（yyyy/mm/dd hh:mm:ss.000）=> 暂不支持文本格式的时间解析
                    // 2.时区将按照东8区解析。
                    // 获取单元格格式
                    // String dataFormatString = cell.getCellStyle().getDataFormatString();
                    final ZoneId EAST_8 = ZoneOffset.ofHours(8);
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(DateUtil.getJavaDate(cell.getNumericCellValue()).toInstant(), EAST_8);
                    result = new CellVerifyValue(true, localDateTime, rule);
                } else {
                    double doubleCellValue = cell.getNumericCellValue();
                    BigDecimal data = new BigDecimal(String.valueOf(doubleCellValue)).stripTrailingZeros();
                    int scale = data.scale();
                    int length = data.toPlainString().length();
                    if (scale > rule.getAccuracy()) {
                        result = new CellVerifyValue(false, doubleCellValue, rule, String.format("[%s列]部分数据精度超出[%d]阈值", rule.getColumnName(), rule.getAccuracy()));
                    } else if (length > rule.getLength()) {
                        result = new CellVerifyValue(false, doubleCellValue, rule, String.format("[%s列]部分数据长度超出[%d]阈值", rule.getColumnName(), rule.getLength()));
                    } else {
                        String configType = rule.getConfigType();
                        if ((StringUtils.containsAny(configType, "long", "int", "integer"))) {
                            // 使用long解析配置的整型/长整型数据
                            result = new CellVerifyValue(true, data.longValue(), rule);
                        } else if ("double".equals(configType)) {
                            // 浮点型数据
                            result = new CellVerifyValue(true, doubleCellValue, rule);
                        } else {
                            // 将数值转换为字符串
                            // 使用BigDecimal避免尾部的".0"
                            result = new CellVerifyValue(true, data.toPlainString(), rule);
                        }
                    }
                }
                break;
            // 文本类型单元格
            case STRING:
                String data = cell.getStringCellValue();
                String configType = rule.getConfigType();
                if (StringUtils.equals(configType, "string")) {
                    // 正常的字符串
                    if (data.length() > rule.getLength()) {
                        result = new CellVerifyValue(false, data, rule, String.format("[%s列]部分数据长度超出[%d]阈值", rule.getColumnName(), rule.getLength()));
                    } else {
                        result = new CellVerifyValue(true, data, rule);
                    }
                } else if ((StringUtils.containsAny(configType, "long", "int", "integer", "double"))) {
                    // 数值型字符串
                    BigDecimal bigDecimalData = new BigDecimal(data.trim()).stripTrailingZeros();
                    int scale = bigDecimalData.scale();
                    int length = bigDecimalData.toPlainString().length();
                    if (scale > rule.getAccuracy()) {
                        result = new CellVerifyValue(false, data, rule, String.format("[%s列]部分数据精度超出[%d]阈值", rule.getColumnName(), rule.getAccuracy()));
                    } else if (length > rule.getLength()) {
                        result = new CellVerifyValue(false, data, rule, String.format("[%s列]部分数据长度超出[%d]阈值", rule.getColumnName(), rule.getLength()));
                    } else {
                        if ("double".equals(rule.getConfigType())) {
                            result = new CellVerifyValue(true, bigDecimalData.doubleValue(), rule);
                        } else {
                            // 使用long解析配置的整型/长整型数据
                            result = new CellVerifyValue(true, bigDecimalData.longValue(), rule);
                        }
                    }
                } else if (StringUtils.equals(configType, "date")) {
                    // 时间型字符串，暂不支持
                    result = new CellVerifyValue(false, data, rule, String.format("[%s列]部分数据非日期时间格式", rule.getColumnName()));
                } else {
                    // 配置的格式不支持
                    result = new CellVerifyValue(false, data, rule, String.format("[%s列]配置的数据格式不支持", rule.getColumnName()));
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

