package com.abc.system.common.filegenerator.service;

import com.abc.system.common.exception.base.BaseRuntimeException;
import com.abc.system.common.filegenerator.vo.ExportFileRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件生成业务接口(支持Excel（xls）+ pdf)
 * <pre>
 * 1.PDF导出示例
 * {@code
 *    @GetMapping("/02")
 *     public void demo02(HttpServletResponse response) {
 *         ExportFileRequest exportFileRequest = new ExportFileRequest();
 *         final String TEMPLATE_NAME = "pdf.ftl";
 *
 *         HashMap<String, Object> dataSet = Maps.newHashMap();
 *         AaaDtl aaaDtl = new AaaDtl();
 *         aaaDtl.setAaaName("AaaName");
 *         aaaDtl.setAaaCode("AaaCode");
 *         aaaDtl.setMemo("测试备注，2023年8月14日08:49:55");
 *         dataSet.put("aaaDtlList", Lists.newArrayList(aaaDtl,aaaDtl,aaaDtl));
 *         exportFileRequest.setTemplateFile(TEMPLATE_NAME);
 *         exportFileRequest.setData(dataSet);
 *         exportFileRequest.setExportFileName("pdf.pdf");
 *
 *         generateFileService.export(exportFileRequest, response);
 *     }
 * }
 * </pre>
 * @Description 文件生成业务接口
 * @Author [author_name]
 * @Date 2077/5/10 19:32
 * @Version 1.0
 */
public interface GenerateFileService {
    /**
     * 导出文件
     *
     * <pre>
     *     默认执行，直接导出
     * </pre>
     *
     * @param response          响应（HttpServletResponse）
     * @param exportFileRequest 请求参数实体
     * @throws BaseRuntimeException 系统异常基类
     */
    void export(ExportFileRequest exportFileRequest, HttpServletResponse response) throws BaseRuntimeException;

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
    void export(ExportFileRequest exportFileRequest, String headerType, HttpServletResponse response) throws BaseRuntimeException;
}
