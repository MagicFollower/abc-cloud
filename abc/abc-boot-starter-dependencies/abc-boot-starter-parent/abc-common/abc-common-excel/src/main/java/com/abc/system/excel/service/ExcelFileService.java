package com.abc.system.excel.service;

import com.abc.system.common.response.ResponseData;
import com.abc.system.excel.vo.ExcelResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * Excel上传解析服务
 *
 * @Description ExcelFileService Excel上传解析服务
 * @Author [author_name]
 * @Date 2077/5/21 17:04
 * @Version 1.0
 */
public interface ExcelFileService {
    /**
     * 从HttpServletRequest中解析Excel
     * <pre>
     * 1.限制单个文件解析；
     * 2.模板编码默认从Header中解析名为"templateCode"的属性值。
     * </pre>
     *
     * @param request HttpServletRequest
     * @return ExcelResponse
     */
    ResponseData<ExcelResponse> parseExcel(HttpServletRequest request);

    /**
     * 从InputStream中解析Excel
     *
     * @param inputStream  输入流
     * @param templateCode 模板编码
     * @return ExcelResponse
     */
    ResponseData<ExcelResponse> parseExcel(InputStream inputStream, String templateCode);
}
