package com.abc.system.excel.service.impl;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.base.BaseRuntimeException;
import com.abc.system.common.exception.business.ValidateException;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import com.abc.system.excel.config.ExcelConfigProperties;
import com.abc.system.excel.service.ExcelFileService;
import com.abc.system.excel.vo.CellVerifyValue;
import com.abc.system.excel.vo.ExcelColumnRule;
import com.abc.system.excel.vo.ExcelResponse;
import com.abc.system.excel.vo.ExcelRule;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel上传解析服务实现
 *
 * @Description ExcelFileServiceImpl Excel上传解析服务实现
 * @Author [author_name]
 * @Date 2077/5/21 17:07
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class ExcelFileServiceImpl implements ExcelFileService {
    private static final String TEMPLATE_CODE_KEY = "templateCode";

    private final ExcelConfigProperties excelConfigProperties;

    @Override
    public ResponseData<ExcelResponse> parseExcel(HttpServletRequest request) {
        // 从请求头中解析templateCode
        String templateCode = request.getHeader(TEMPLATE_CODE_KEY);
        if (StringUtils.isEmpty(templateCode)) {
            throw new ValidateException(SystemRetCodeConstants.EXCEL_TEMPLATE_CODE_LOST);
        }

        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 保存excel处理结果
        // 逗号分隔的数据
        List<String> displayData = new ArrayList<>();
        // List#字段名:值
        List<JSONObject> realExcelResultList = new ArrayList<>();

        // 真实Title字段Map、Title显示数据Map
        Map<Integer, String> realTitleMap = new HashMap<>();
        Map<Integer, String> displayTitleMap = new HashMap<>();
        // 判断 request 是否有文件上传,即多部分请求,实际将检测（enctype="multipart/form-data" method="POST"）
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            List<String> fileList = ExcelResolveUtils.copyIterator(multipartRequest.getFileNames());
            // 文件数量是否超过限制数量（只支持单文件上传）
            if (fileList.size() != 1) {
                throw new ValidateException(SystemRetCodeConstants.EXCEL_NUM_ERROR);
            }
            // 取得上传文件(第一个)
            List<MultipartFile> files = multipartRequest.getFiles(fileList.get(0));
            if (files.size() != 1) {
                throw new ValidateException(SystemRetCodeConstants.EXCEL_NUM_ERROR);
            }
            MultipartFile file = files.get(0);
            if (file == null || file.isEmpty()) {
                throw new ValidateException(SystemRetCodeConstants.EXCEL_IS_NULL);
            }

            // 文件后缀校验
            // 仅支持.xlsx、.xls
            String originalFilename = file.getOriginalFilename();
            if (!StringUtils.endsWithIgnoreCase(originalFilename, ".xlsx") && !StringUtils.endsWithIgnoreCase(originalFilename, ".xls")) {
                throw new ValidateException(SystemRetCodeConstants.EXCEL_NOT_SUPPORT_ERROR);
            }

            // 解析Excel列规则
            // 1.resolveCellColumnRule提供两个重载，无参数会解析所有数据，支持一个参数指定templateCode
            // 2.这里使用指定templateCode的重载
            Map<String, ExcelColumnRule> excelRuleMap = ExcelResolveUtils.resolveCellColumnRule(templateCode);

            try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
                // 获取与templateCode唯一匹配的规则配置信息
                List<ExcelRule> excelRules = excelConfigProperties.getRules().stream()
                        .filter(e -> StringUtils.equalsIgnoreCase(e.getTemplateCode(), templateCode))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(excelRules) || excelRules.size() > 1) {
                    throw new ValidateException(SystemRetCodeConstants.EXCEL_RULE_ERROR);
                }
                // 获取标题所在的行数
                int titleNum = excelRules.get(0).getTitleNum();
                if (titleNum <= 0) titleNum = 1;
                // 解析Excel数据
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet currentSheet = workbook.getSheetAt(i);
                    if (currentSheet.getLastRowNum() == 0 && currentSheet.getPhysicalNumberOfRows() == 0) {
                        continue;
                    }
                    RowTypeEnum rowTypeEnum = RowTypeEnum.TITLE;
                    for (Row row : currentSheet) {
                        if (rowTypeEnum.equals(RowTypeEnum.TITLE)) {
                            // 定位至标题行
                            if (row.getRowNum() + 1 < titleNum) continue;
                            // 跳过空行
                            if (ExcelResolveUtils.isRowEmpty(row)) continue;
                            // 解析标题行
                            ExcelResolveUtils.resolveExcelTitle(row, templateCode, excelRuleMap, realTitleMap, displayTitleMap);
                            // 标记之后的所有数据为记录行
                            rowTypeEnum = RowTypeEnum.DATA;
                            continue;
                        }
                        // 跳过空行
                        if (ExcelResolveUtils.isRowEmpty(row)) continue;
                        JSONObject realResultObj = new JSONObject();
                        StringBuilder stringBuilder = new StringBuilder();
                        // Cell校验
                        for (Cell cell : row) {
                            if (StringUtils.isEmpty(displayTitleMap.get(cell.getColumnIndex())) || cell.getCellType() == CellType.BLANK) {
                                continue;
                            }
                            // 校验Cell类型
                            // 1.根据模板编码_配置的列名获取指定配置规则，首先校验单元格类型是否为指定类型（当前仅支持数值类型和字符串类型）
                            CellVerifyValue cellVerifyValue = ExcelResolveUtils.verifyCellType(templateCode, displayTitleMap, excelRuleMap, cell);
                            if (!cellVerifyValue.isVerify()) {
                                throw new ValidateException(SystemRetCodeConstants.EXCEL_TYPE_ERROR);
                            }
                            // 解析Cell数据
                            CellVerifyValue cellValue = ExcelResolveUtils.getCellValue(cell, cellVerifyValue.getRule());
                            if (!cellValue.isVerify()) {
                                throw new ValidateException(SystemRetCodeConstants.EXCEL_TYPE_ERROR.getCode(), cellValue.getErrorMsg());
                            }
                            realResultObj.put(realTitleMap.get(cell.getColumnIndex()), cellValue.getValue());
                            stringBuilder.append(",").append(cellValue.getValue().toString().trim());
                        }
                        displayData.add(stringBuilder.substring(1));
                        realExcelResultList.add(realResultObj);
                    }
                }
            } catch (BaseRuntimeException e) {
                throw e;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new ValidateException(SystemRetCodeConstants.EXCEL_PARSE_TERMINATED);
            }
        }
        return new ResponseProcessor<ExcelResponse>().setData(new ExcelResponse(displayTitleMap.values(), displayData, realExcelResultList));
    }

    enum RowTypeEnum {
        TITLE, DATA
    }
}
