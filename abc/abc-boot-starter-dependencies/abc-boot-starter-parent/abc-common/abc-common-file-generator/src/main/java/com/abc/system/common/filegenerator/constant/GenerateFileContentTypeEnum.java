package com.abc.system.common.filegenerator.constant;

/**
 * GenerateFileContentTypeEnum
 *
 * @Description GenerateFileContentTypeEnum
 * @Author Trivis
 * @Date 2023/5/10 19:44
 * @Version 1.0
 */
public enum GenerateFileContentTypeEnum {
    // application/x-msdownload(Microsoft) != application/octet-stream(通用)
    GENERATOR_CONTENT_TYPE_DOWNLOAD("CONTENT_TYPE", "application/octet-stream"),
    GENERATOR_HEADER_TYPE_DOWNLOAD("DOWNLOAD", "attachment;"),
    GENERATOR_HEADER_TYPE_PREVIEW("PREVIEW", "inline;");

    private String code;

    private String value;

    GenerateFileContentTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
