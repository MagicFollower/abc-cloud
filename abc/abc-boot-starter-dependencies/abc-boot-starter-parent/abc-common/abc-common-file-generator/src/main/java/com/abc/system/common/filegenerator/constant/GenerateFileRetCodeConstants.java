package com.abc.system.common.filegenerator.constant;

/**
 * 文件导出异常枚举类
 *
 * @Description 文件导出异常枚举类（加载模板失败、创建文件失败、导出文件失败）
 * @Author Trivis
 * @Date 2023/5/10 19:59
 * @Version 1.0
 */
public enum GenerateFileRetCodeConstants {
    GENERATOR_TEMPLATE_LOAD_TEMPLATE_ERROR("007001", "加载模板失败"),
    GENERATOR_TEMPLATE_CREATE_FILE_ERROR("007003", "创建文件失败"),
    GENERATOR_TEMPLATE_EXPORT_FILE_ERROR("007002", "导出文件失败");

    private String code;

    private String message;

    GenerateFileRetCodeConstants(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
