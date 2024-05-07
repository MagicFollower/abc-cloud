package com.abc.system.excel.util;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.business.BizException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Excel数据导出工具类
 * <pre>
 * 1 功能说明
 *   1.1 支持多sheet数据导出，sheet名可自定义
 *   1.2 支持API链式调用
 *   1.3 支持[导出Excel模板]、[多出Excel数据]，见使用案例2.1、2.2
 *   1.4 支持自定义数据行的行高和列宽，见使用案例2.4
 *   1.5 支持配置默认单元格样式、默认行高和默认列宽，见使用案例2.5
 *
 * 2 使用案例
 *   2.1 使用当前工具类导出有数据的Excel（Attention: 如果要保证Sheet的有序，请使用LinkedHashMap）
 *   {@code
 *       List<MikSdbCosPsnExportDTO> exportDataList = psnResponse.getResult();
 *       Map<String, List<MikSdbCosPsnExportDTO>> sheetNameToSheetDataMap = new LinkedHashMap<>(3);
 *       sheetNameToSheetDataMap.put("Sheet1", exportDataList);
 *       sheetNameToSheetDataMap.put("Sheet2", exportDataList);
 *       sheetNameToSheetDataMap.put("Sheet3", exportDataList);
 *       List<String> titleList = Arrays.asList(
 *               "顾客编号",
 *               "顾客姓名"
 *       );
 *       ExcelExportUtils.init(titleList, sheetNameToSheetDataMap).fillData((dataMap, sheetMap) -> {
 *           dataMap.forEach((sheetName, dataList) -> {
 *               HSSFSheet sheet = sheetMap.get(sheetName);
 *               for (int i = 0; i < dataList.size(); i++) {
 *                   MikSdbCosPsnExportDTO dto = dataList.get(i);
 *                   HSSFRow sheetRow = sheet.createRow(i + 1);
 *                   // 正常情况下你无需对每一个元素手动进行null处理，setCellValue会自动将null元素所在的单元格设置为BLANK
 *                   sheetRow.createCell(0).setCellValue(dto.getCustPsnCode());
 *                   sheetRow.createCell(1).setCellValue(Optional.ofNullable(dto.getCustPsnName()).orElse(""));
 *                   // ...
 *               }
 *           });
 *       }).export(response, "测试文件.xls");
 *   }
 *
 *   2.2 使用当前工具类导出Excel模板
 *   {@code
 *       final String DEFAULT_FILE_NAME = "销售发货申请-人员明细-导入.xls";
 *       List<String> titleList = Arrays.asList(
 *               "顾客编号",
 *               "顾客姓名"
 *       );
 *       Map<String, List<String>> sheetNameToTitleListMap = new LinkedHashMap<>(1);
 *       sheetNameToTitleListMap.put("Sheet1", titleList);
 *       ExcelExportUtils.init(sheetNameToTitleListMap).export(response, DEFAULT_FILE_NAME);
 *   }
 *
 *   2.3 直接将Excel写入OutputStream
 *   {@code
 *       try (FastByteArrayOutputStream bos = new FastByteArrayOutputStream()) {
 *           List<String> titleList = Arrays.asList(
 *                   "款号",
 *                   "款式序号",
 *                   "尺码",
 *                   "预投数量",
 *                   "备注"
 *           );
 *           Map<String, List<MikSdbPipGoodsImportVO>> sheetNameToSheetDataMap = new LinkedHashMap<>(3);
 *           sheetNameToSheetDataMap.put("原始数据", excelDataThreeSheets.get("allData"));
 *           sheetNameToSheetDataMap.put("成功数据", excelDataThreeSheets.get("succeedData"));
 *           sheetNameToSheetDataMap.put("失败数据", excelDataThreeSheets.get("failedData"));
 *           ExcelExportUtils.init(titleList, sheetNameToSheetDataMap).fillData((dataMap, sheetMap) -> {
 *               dataMap.forEach((sheetName, dataList) -> {
 *                   HSSFSheet sheet = sheetMap.get(sheetName);
 *                   for (int i = 0; i < dataList.size(); i++) {
 *                       MikSdbPipGoodsImportVO dto = dataList.get(i);
 *                       HSSFRow sheetRow = sheet.createRow(i + 1);
 *                       sheetRow.createCell(0).setCellValue(Optional.ofNullable(dto.getStyleCode()).orElse(""));
 *                       sheetRow.createCell(1).setCellValue(Optional.ofNullable(dto.getStyleXh()).orElse(""));
 *                       sheetRow.createCell(2).setCellValue(Optional.ofNullable(dto.getStdSizeCode()).orElse(""));
 *                       sheetRow.createCell(3).setCellValue(Optional.ofNullable(dto.getPipQty()).orElse(""));
 *                       sheetRow.createCell(4).setCellValue(Optional.ofNullable(dto.getMemo()).orElse(""));
 *                   }
 *               });
 *           }).export(bos);
 *           // ...
 *       }
 *   }
 *
 *   2.4 自定义数据区域的[行高]和[列宽]
 *   {@code
 *       ExcelExportUtils.init(titleList, sheetNameToSheetDataMap).fillData((dataMap, sheetMap) -> {
 *           dataMap.forEach((sheetName, dataList) -> {
 *               HSSFSheet sheet = sheetMap.get(sheetName);
 *               // 👉为[收货全地址]、[收货人手机号]、[款号]三列设置自定义宽度(N*256表示N个字符宽度)
 *               // 10/12一般情况下是默认宽度
 *               sheet.setColumnWidth(9, 20 * 256);
 *               sheet.setColumnWidth(11, 10 * 256);
 *               sheet.setColumnWidth(12, 10 * 256);
 *               for (int i = 0; i < dataList.size(); i++) {
 *                   MikSdbCosPsnExportDTO dto = dataList.get(i);
 *                   HSSFRow sheetRow = sheet.createRow(i + 1);
 *                   // 👉为第一行设置指定高度（24磅）
 *                   if (i == 0) {
 *                       sheetRow.setHeight((short) (24 * 20));
 *                   }
 *                   sheetRow.createCell(0).setCellValue(Optional.ofNullable(dto.getCustPsnCode()).orElse(""));
 *                   sheetRow.createCell(1).setCellValue(Optional.ofNullable(dto.getCustPsnName()).orElse(""));
 *                   sheetRow.createCell(2).setCellValue(Optional.ofNullable(dto.getCustDeptCode()).orElse(""));
 *                   // ...
 *               }
 *           });
 *       }).export(response, DEFAULT_FILE_NAME);
 *   }
 *
 *   2.5 配置默认单元格样式、默认行高和默认列宽
 *   {@code
 *       ExcelExportUtils.init(titleList, sheetNameToSheetDataMap, workbook->{
 *           HSSFCellStyle cellStyle = workbook.createCellStyle();
 *           // 设置样式
 *           // HSSFFont font = workbook.createFont();
 *           // font.setFontHeightInPoints((short) 5);
 *           // font.setColor(IndexedColors.BLUE.getIndex());
 *           // cellStyle.setFont(font);
 *           return cellStyle;
 *       }, 50, 50 * 20).fillData((dataMap, sheetMap) -> {
 *           dataMap.forEach((sheetName, dataList) -> {
 *               HSSFSheet sheet = sheetMap.get(sheetName);
 *               for (int i = 0; i < dataList.size(); i++) {
 *                   MikSdbCosPsnExportDTO dto = dataList.get(i);
 *                   HSSFRow sheetRow = sheet.createRow(i + 1);
 *                   sheetRow.createCell(0).setCellValue(Optional.ofNullable(dto.getCustPsnCode()).orElse(""));
 *                   sheetRow.createCell(1).setCellValue(Optional.ofNullable(dto.getCustPsnName()).orElse(""));
 *               }
 *           });
 *       }).export(response, DEFAULT_FILE_NAME);
 *   }
 * </pre>
 *
 * @Description ExcelExportUtils
 * @Author [author_name]
 * @Date 2023/11/11 23:55
 * @Version 1.0
 */
@Slf4j
public class ExcelExportUtils<T> {
    /**
     * sheetName到HSSFSheet实例的映射，内部使用
     */
    public Map<String, HSSFSheet> sheetNameToSheetMap;
    /**
     * 统一的单元格Cell样式，内部使用
     */
    private HSSFCellStyle defaultCellStyle;
    /**
     * HSSFWorkbook实例，内部使用
     */
    private HSSFWorkbook workbook;
    /**
     * sheetName到数据集的映射，内部使用
     */
    private Map<String, List<T>> sheetNameToSheetDataMap;

    private ExcelExportUtils() {
    }

    /**
     * Step 1-1 初始化多Sheet及每个Sheet中的Header
     *
     * @param titles                  标题列表
     * @param sheetNameToSheetDataMap sheetName到数据集的映射
     * @return ExcelExportUtils实例，支持链式调用
     */
    public static <T> ExcelExportUtils<T> init(@NonNull List<String> titles,
                                               @NonNull Map<String, List<T>> sheetNameToSheetDataMap) {
        return init(titles, sheetNameToSheetDataMap, null, null, null);
    }

    /**
     * Step 1-1-FULL 初始化多Sheet及每个Sheet中的Header
     *
     * @param titles                   标题列表
     * @param sheetNameToSheetDataMap  sheetName到数据集的映射
     * @param defaultCellStyleFunction 单元格样式回调Function，开放配置默认单元格样式
     * @param defaultColumnWidth       默认列宽（单位：字符宽度）
     * @param defaultRowHeight         默认行高（单位：磅）
     * @param <T>                      泛型T，数据集实体类型
     * @return ExcelExportUtils实例，支持链式调用
     */
    public static <T> ExcelExportUtils<T> init(@NonNull List<String> titles,
                                               @NonNull Map<String, List<T>> sheetNameToSheetDataMap,
                                               Function<HSSFWorkbook, HSSFCellStyle> defaultCellStyleFunction,
                                               Integer defaultColumnWidth,
                                               Integer defaultRowHeight
    ) {
        List<String> headers = new ArrayList<>(titles);
        ExcelExportUtils<T> thisUtil = new ExcelExportUtils<>();
        thisUtil.sheetNameToSheetMap = new HashMap<>(sheetNameToSheetDataMap.size());
        thisUtil.workbook = new HSSFWorkbook();
        if (defaultCellStyleFunction == null) {
            // 默认单元格样式（数据部分）
            thisUtil.defaultCellStyle = thisUtil.getDefaultDataCellStyle();
        } else {
            thisUtil.defaultCellStyle = defaultCellStyleFunction.apply(thisUtil.workbook);
        }
        thisUtil.sheetNameToSheetDataMap = sheetNameToSheetDataMap;
        // 默认单元格样式（标题部分）
        HSSFCellStyle headerCellStyle = thisUtil.getDefaultHeaderCellStyle();
        sheetNameToSheetDataMap.keySet().forEach(sheetName -> {
            HSSFSheet sheet = thisUtil.workbook.createSheet(sheetName);
            // 设置列宽（默认12个字符宽度）
            sheet.setDefaultColumnWidth(Optional.ofNullable(defaultColumnWidth).orElse(9));
            // 设置行高（默认17.25磅）
            sheet.setDefaultRowHeight(Optional.ofNullable(defaultRowHeight).orElse((int) (17.25 * 20)).shortValue());
            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerCellStyle);
            }
            // 数据填充阶段使用
            thisUtil.sheetNameToSheetMap.put(sheetName, sheet);
        });

        return thisUtil;
    }

    /**
     * Step 2-1 初始化多Sheet的Excel模板，之后直接调用Step 2-2导出
     *
     * @param sheetNameToHeaderList sheetName到表头列表
     * @return ExcelExportUtils实例，支持链式调用
     */
    public static <T> ExcelExportUtils<T> init(@NonNull Map<String, List<String>> sheetNameToHeaderList) {
        return init(sheetNameToHeaderList, null, null, null);
    }

    /**
     * Step 2-1-FULL 初始化多Sheet的Excel模板，之后直接调用Step 2-2导出
     *
     * @param sheetNameToHeaderList    sheetName到表头列表
     * @param defaultCellStyleFunction 单元格样式回调Function，开放配置默认单元格样式
     * @param defaultColumnWidth       默认列宽（单位：字符宽度）
     * @param defaultRowHeight         默认行高（单位：磅）
     * @param <T>                      泛型T，数据集实体类型
     * @return ExcelExportUtils实例，支持链式调用
     */
    public static <T> ExcelExportUtils<T> init(@NonNull Map<String, List<String>> sheetNameToHeaderList,
                                               Function<HSSFWorkbook, HSSFCellStyle> defaultCellStyleFunction,
                                               Integer defaultColumnWidth,
                                               Integer defaultRowHeight) {
        ExcelExportUtils<T> thisUtil = new ExcelExportUtils<>();
        thisUtil.sheetNameToSheetMap = new HashMap<>(sheetNameToHeaderList.size());
        thisUtil.workbook = new HSSFWorkbook();
        if (defaultCellStyleFunction == null) {
            // 默认单元格样式（数据部分）
            thisUtil.defaultCellStyle = thisUtil.getDefaultDataCellStyle();
        } else {
            thisUtil.defaultCellStyle = defaultCellStyleFunction.apply(thisUtil.workbook);
        }
        // 默认单元格样式（标题部分）
        HSSFCellStyle headerCellStyle = thisUtil.getDefaultHeaderCellStyle();
        sheetNameToHeaderList.forEach((sheetName, titles) -> {
            List<String> headers = new ArrayList<>(titles);
            HSSFSheet sheet = thisUtil.workbook.createSheet(sheetName);
            // 设置列宽（默认12个字符宽度）
            sheet.setDefaultColumnWidth(Optional.ofNullable(defaultColumnWidth).orElse(9));
            // 设置行高（默认17.25磅）
            sheet.setDefaultRowHeight(Optional.ofNullable(defaultRowHeight).orElse((int) (17.25 * 20)).shortValue());
            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerCellStyle);
            }
            thisUtil.sheetNameToSheetMap.put(sheetName, sheet);
        });

        return thisUtil;
    }

    /**
     * step 1-2 填充每个Sheet的数据
     *
     * @param sheetDataConsumer 两参数消费者，参数一（sheetName->数据集列表），参数二（sheetName->HSSFSheet实例）
     * @return ExcelExportUtils实例，支持链式调用
     */
    public ExcelExportUtils<T> fillData(@NonNull BiConsumer<Map<String, List<T>>, Map<String, HSSFSheet>> sheetDataConsumer) {
        // 确保Step1-2在Step1-1阶段后执行，此时sheetNameToSheetDataMap表单对应的数据集Map应该已经初始化(非null)
        if (sheetNameToSheetDataMap == null) {
            throw new IllegalStateException("[sheetNameToSheetDataMap] without init, maybe you use a wrong init()?");
        }

        sheetDataConsumer.accept(sheetNameToSheetDataMap, sheetNameToSheetMap);
        // 数据填充后，遍历所有Sheet中第二行开始的单元格，统一样式（仅设置字体类型、颜色与大小）
        // 1.该阶段不会重新赋值行高与列宽，所以可以在sheetDataConsumer中重新设置指定列的宽度与指定行的高度（参考类注释文档2.5）
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                HSSFRow row = sheet.getRow(rowIndex);
                if (row != null) {
                    for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                        HSSFCell cell = row.getCell(cellIndex);
                        if (cell != null) {
                            cell.setCellStyle(defaultCellStyle);
                        }
                    }
                }
            }
        }
        return this;
    }


    /**
     * step 1-3 写入HttpServletResponse
     * step 2-2 写入HttpServletResponse
     * <pre>
     * 该方法已废弃，请使用不含 encodeFileName 参数的版本，这个参数会导致中文文件名无法被response正常处理
     * </pre>
     *
     * @param servletResponse HttpServletResponse
     * @param fileName        文件名（默认：template.xls），参数需要指定文件后缀
     * @param encodeFileName  文件名是否需要使用URLEncoder编码 (❌)
     */
    @Deprecated
    public void export(@NonNull HttpServletResponse servletResponse,
                       String fileName,
                       boolean encodeFileName) {
        if (StringUtils.isEmpty(fileName)) {
            fileName = "template.xls";
        }
        try {
            if (encodeFileName) {
                String[] split = fileName.split("\\.");
                if (split.length == 1) return;
                fileName = URLEncoder.encode(split[0], StandardCharsets.UTF_8.name()) + "." + split[1];
            }
            OutputStream outputStream = servletResponse.getOutputStream();
            servletResponse.reset();
            servletResponse.setContentType("application/vnd.ms-excel");
            servletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            this.workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error(">>>>>>>>|export to HttpServletResponse|failed|exception:{}|<<<<<<<<", e.getMessage(), e);
            throw new BizException(SystemRetCodeConstants.OP_FAILED.getCode(), e.getMessage());
        }
    }

    /**
     * step 1-3 写入HttpServletResponse
     * step 2-2 写入HttpServletResponse
     * <pre>
     * 默认对文件名进行URL编码，文件名为空时使用默认文件名 template.xls
     * </pre>
     *
     * @param servletResponse HttpServletResponse
     * @param fileName        文件名（默认：template.xls），参数需要指定文件后缀
     */
    public void export(@NonNull HttpServletResponse servletResponse, String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            fileName = "template.xls";
        }
        try {
            String[] split = fileName.split("\\.");
            if (split.length == 1) return;
            fileName = URLEncoder.encode(split[0], StandardCharsets.UTF_8.name()) + "." + split[1];
            OutputStream outputStream = servletResponse.getOutputStream();
            servletResponse.reset();
            servletResponse.setContentType("application/vnd.ms-excel");
            servletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            this.workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error(">>>>>>>>|export to HttpServletResponse|failed|exception:{}|<<<<<<<<", e.getMessage(), e);
            throw new BizException(SystemRetCodeConstants.OP_FAILED.getCode(), e.getMessage());
        }
    }

    /**
     * 写入OutputStream
     * <pre>
     * 1 输出流在方法调用后不会关闭
     * </pre>
     *
     * @param outputStream 输出流
     */
    public void export(@NonNull OutputStream outputStream) {
        try {
            this.workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.error(">>>>>>>>|export to outputStream|failed|exception:{}|<<<<<<<<", e.getMessage(), e);
            throw new BizException(SystemRetCodeConstants.OP_FAILED.getCode(), e.getMessage());
        }
    }


    private HSSFCellStyle getDefaultDataCellStyle() {
        HSSFCellStyle dataCellStyle = this.workbook.createCellStyle();
        dataCellStyle.setBorderTop(BorderStyle.THIN);
        dataCellStyle.setBorderBottom(BorderStyle.THIN);
        dataCellStyle.setBorderLeft(BorderStyle.THIN);
        dataCellStyle.setBorderRight(BorderStyle.THIN);
        dataCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        dataCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        dataCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        dataCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        // font
        HSSFFont font = this.workbook.createFont();
        font.setFontName("微软雅黑");
        font.setColor(IndexedColors.BLACK.getIndex());
        dataCellStyle.setFont(font);
        return dataCellStyle;
    }

    private HSSFCellStyle getDefaultHeaderCellStyle() {
        HSSFCellStyle headerCellStyle = this.workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headerCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headerCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        // font
        HSSFFont font = this.workbook.createFont();
        font.setFontName("微软雅黑");
        font.setColor(IndexedColors.BLACK.getIndex());
        headerCellStyle.setFont(font);
        return headerCellStyle;
    }
}
