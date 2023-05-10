package com.abc.system.common.filegenerator.vo;

import lombok.Data;

import java.util.Map;

/**
 * ExportFileRequest
 *
 * @Description ExportFileRequest 文件导出参数请求体
 * @Author Trivis
 * @Date 2023/5/10 19:14
 * @Version 1.0
 */
@Data
public class ExportFileRequest {
    /**
     * 生成文件的数据集合
     */
    private Map<String, Object> data;

    /**
     * 导出的文件名
     */
    private String exportFileName;

    /**
     * 模板文件名
     */
    private String templateFile;
}
