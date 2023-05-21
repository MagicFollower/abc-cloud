package com.abc.system.excel.service;

import com.abc.system.common.response.ResponseData;
import com.abc.system.excel.vo.ExcelResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * Excel上传解析服务
 *
 * @Description ExcelFileService Excel上传解析服务
 * @Author Trivis
 * @Date 2023/5/21 17:04
 * @Version 1.0
 */
public interface ExcelFileService {

    ResponseData<ExcelResponse> dealWith(HttpServletRequest request);

}