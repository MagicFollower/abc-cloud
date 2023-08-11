package com.abc.system.common.filegenerator.service;

import com.abc.system.common.exception.base.BaseRuntimeException;
import com.abc.system.common.filegenerator.vo.ExportFileRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件生成业务接口
 *
 * @Description 文件生成业务接口
 * @Author Trivis
 * @Date 2023/5/10 19:32
 * @Version 1.0
 */
public interface GenerateFileService {
    /**
     * 导出文件
     * <pre>
     *     默认执行，直接导出
     * </pre>
     *
     * @param response          响应（HttpServletResponse）
     * @param exportFileRequest 请求参数实体
     * @throws BaseRuntimeException 系统异常基类
     */
    void export(HttpServletResponse response, ExportFileRequest exportFileRequest) throws BaseRuntimeException;

    /**
     * 导出文件
     * <pre>
     *     1.根据generatorFileContentType决定是导出，还是预览;
     *     2.这里导出的Excel-FTL模板使用Excel2003中的XML格式文件，虽然被XLSX取代，但是该XML文件结构清晰可读。
     * </pre>
     *
     * @param response          响应（HttpServletResponse）
     * @param headerType        com.abc.system.common.filegenerator.constant.GenerateFileContentTypeEnum#GENERATOR_HEADER_TYPE_DOWNLOAD:下载,
     *                          com.abc.system.common.filegenerator.constant.GenerateFileContentTypeEnum#GENERATOR_HEADER_TYPE_PREVIEW:预览
     * @param exportFileRequest 请求参数实体
     * @throws BaseRuntimeException 系统异常基类
     */
    void export(HttpServletResponse response, String headerType, ExportFileRequest exportFileRequest) throws BaseRuntimeException;
}
