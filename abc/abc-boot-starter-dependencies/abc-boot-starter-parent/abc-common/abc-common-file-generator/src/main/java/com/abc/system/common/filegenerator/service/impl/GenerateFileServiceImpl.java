package com.abc.system.common.filegenerator.service.impl;

import com.abc.system.common.exception.base.BaseRuntimeException;
import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.filegenerator.constant.GenerateFileContentTypeEnum;
import com.abc.system.common.filegenerator.constant.GenerateFileRetCodeConstants;
import com.abc.system.common.filegenerator.service.GenerateFileService;
import com.abc.system.common.filegenerator.util.GeneratorFileUtils;
import com.abc.system.common.filegenerator.util.GeneratorPDFUtil;
import com.abc.system.common.filegenerator.vo.ExportFileRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.FastByteArrayOutputStream;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件生成业务实现
 *
 * @Description 文件生成业务实现，支持PDF+Excel
 * @Author Trivis
 * @Date 2023/5/10 19:51
 * @Version 1.0
 */
@Slf4j
public class GenerateFileServiceImpl implements GenerateFileService {
    @Override
    public void export(ExportFileRequest exportFileRequest, HttpServletResponse response) throws BaseRuntimeException {
        this.export(exportFileRequest, GenerateFileContentTypeEnum.GENERATOR_HEADER_TYPE_DOWNLOAD.getValue(), response);
    }

    @Override
    public void export(ExportFileRequest exportFileRequest, String headerType, HttpServletResponse response)
            throws BaseRuntimeException {
        OutputStream fastByteArrayOutputStream;
        String exportFileName = null;
        try {
            exportFileName = exportFileRequest.getExportFileName();
            log.info(">>>>>>>>|export file|START|headerType:{} → filename:{}|<<<<<<<<", headerType, exportFileName);
            if (".pdf".equalsIgnoreCase(getExtension(exportFileName))) {
                fastByteArrayOutputStream = GeneratorPDFUtil.createPDF(exportFileRequest);
            } else {
                fastByteArrayOutputStream = GeneratorFileUtils.createFile(exportFileRequest);
            }
            // 默认下载
            if (StringUtils.isEmpty(headerType)) {
                headerType = GenerateFileContentTypeEnum.GENERATOR_HEADER_TYPE_DOWNLOAD.getValue();
            }
            String encodeFileName = URLEncoder.encode(exportFileName, StandardCharsets.UTF_8.name());
            response.setContentType(GenerateFileContentTypeEnum.GENERATOR_CONTENT_TYPE_DOWNLOAD.getValue());
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerType + "filename=" + encodeFileName);
            // 写入HttpServletResponse
            ((FastByteArrayOutputStream) fastByteArrayOutputStream).writeTo(response.getOutputStream());
            fastByteArrayOutputStream.close();
            log.info(">>>>>>>>|export file|SUCCESS|headerType:{} → filename:{}|<<<<<<<<", headerType, exportFileName);
        } catch (Exception e) {
            log.info(">>>>>>>>|export file|EXCEPTION|headerType:{} → filename:{}|<<<<<<<<",
                    headerType, exportFileName, e);
            throw new BizException(GenerateFileRetCodeConstants.GENERATOR_TEMPLATE_EXPORT_FILE_ERROR.getCode(),
                    GenerateFileRetCodeConstants.GENERATOR_TEMPLATE_EXPORT_FILE_ERROR.getMessage());
        }
    }

    /**
     * 取得文件扩展名（不含.）
     *
     * @param fileName 文件名
     * @return 文件拓展名（不含.）
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
