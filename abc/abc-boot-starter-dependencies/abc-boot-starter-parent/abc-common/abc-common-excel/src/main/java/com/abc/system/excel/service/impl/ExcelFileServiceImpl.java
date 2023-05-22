package com.abc.system.excel.service.impl;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.business.ValidateException;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import com.abc.system.excel.config.ExcelConfigProperties;
import com.abc.system.excel.helper.ResolveExcelHelper;
import com.abc.system.excel.service.ExcelFileService;
import com.abc.system.excel.vo.CellVerifyValue;
import com.abc.system.excel.vo.ExcelColumnRule;
import com.abc.system.excel.vo.ExcelResponse;
import com.abc.system.excel.vo.ExcelRule;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel上传解析服务实现
 *
 * @Description ExcelFileServiceImpl Excel上传解析服务实现
 * @Author Trivis
 * @Date 2023/5/21 17:07
 * @Version 1.0
 */
@Slf4j
public class ExcelFileServiceImpl implements ExcelFileService {
    private static final String TEMPLATE_CODE_KEY = "templateCode";

    /**
     * <pre>
     * 移除指定警告：“必须在有效 Spring Bean 中定义自动装配成员(@Component|@Service|…)”
     *   1.原因：该类已在com.abc.system.lock.config.DistributedLockAutoConfiguration中通过@Bean方式完成注入。
     * </pre>
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ExcelConfigProperties excelConfigProperties;

    @Override
    public ResponseData<ExcelResponse> dealWith(HttpServletRequest request) {
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

        List<Row> titleRows = new ArrayList<>();
        Map<Integer, String> realTitleMap = new HashMap<>();
        Map<Integer, String> displayTitleMap = new HashMap<>();
        // 判断 request 是否有文件上传,即多部分请求,实际将检测（enctype="multipart/form-data" method="POST"）
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            List<String> fileList = ResolveExcelHelper.copyIterator(multipartRequest.getFileNames());
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
            if (!StringUtils.endsWithIgnoreCase(originalFilename, ".xlsx")
                    || !StringUtils.endsWithIgnoreCase(originalFilename, ".xls")) {
                throw new ValidateException(SystemRetCodeConstants.EXCEL_NOT_SUPPORT_ERROR);
            }

            // 解析Excel列规则
            Map<String, ExcelColumnRule> excelRuleMap = ResolveExcelHelper.resolveCellColumnRule();

            try (InputStream inputStream = file.getInputStream()) {
                Workbook workbook = WorkbookFactory.create(inputStream);
                // 获取与templateCode唯一匹配的规则配置信息
                List<ExcelRule> excelRules = excelConfigProperties.getRules().stream()
                        .filter(e -> StringUtils.equalsIgnoreCase(e.getTemplateCode(), templateCode))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(excelRules) || excelRules.size() > 1) {
                    throw new ValidateException(SystemRetCodeConstants.EXCEL_RULE_ERROR);
                }
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet currentSheet = workbook.getSheetAt(i);
                    if (currentSheet.getLastRowNum() == 0 && currentSheet.getPhysicalNumberOfRows() == 0) {
                        continue;
                    }
                    for (Row row : currentSheet) {
                        JSONObject realResultObj = new JSONObject();
                        // 检测空行
                        if (ResolveExcelHelper.isRowEmpty(row)) continue;
                        // 检测标题行
                        if (row.getRowNum() == 0) {
                            ResolveExcelHelper.resolveExcelTitle(row, templateCode, excelRuleMap,
                                    realTitleMap, displayTitleMap);
                            continue;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        // Cell校验
                        for (Cell cell : row) {
                            if (StringUtils.isEmpty(displayTitleMap.get(cell.getColumnIndex()))
                                    || cell.getCellType() == CellType.BLANK) {
                                continue;
                            }
                            // 校验Cell类型
                            CellVerifyValue cellVerifyValue = ResolveExcelHelper.verifyCellType(templateCode,
                                    displayTitleMap, excelRuleMap, cell);
                            if (!cellVerifyValue.isVerify()) {
                                throw new ValidateException(SystemRetCodeConstants.EXCEL_TYPE_ERROR);
                            }
                            // 解析Cell数据
                            CellVerifyValue cellValue = ResolveExcelHelper.getCellValue(cell, cellVerifyValue.getRule());
                            if (!cellValue.isVerify()) {
                                throw new ValidateException(SystemRetCodeConstants.EXCEL_TYPE_ERROR);
                            }
                            realResultObj.put(realTitleMap.get(cell.getColumnIndex()), cellValue.getValue());
                            stringBuilder.append(",").append(cellValue.getValue());
                        }
                        displayData.add(stringBuilder.substring(1));
                        realExcelResultList.add(realResultObj);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return new ResponseProcessor<ExcelResponse>().setData(new ExcelResponse(displayTitleMap.values(),
                displayData, realExcelResultList, titleRows));
    }
}
