package com.abc.system.common.filegenerator.constant;

import lombok.Getter;

/**
 * GenerateFileContentTypeEnum
 *
 * @Description GenerateFileContentTypeEnum
 * @Author [author_name]
 * @Date 2077/5/10 19:44
 * @Version 1.0
 */
@Getter
public enum GenerateFileContentTypeEnum {
    // application/x-msdownload(Microsoft) != application/octet-stream(通用)
    GENERATOR_CONTENT_TYPE_DOWNLOAD("CONTENT_TYPE", "application/octet-stream"),
    GENERATOR_HEADER_TYPE_DOWNLOAD("DOWNLOAD", "attachment;"),
    GENERATOR_HEADER_TYPE_PREVIEW("PREVIEW", "inline;");

    private final String code;

    private final String value;

    GenerateFileContentTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
