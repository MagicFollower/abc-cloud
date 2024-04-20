package com.abc.system.common.minio.constant;

import lombok.Getter;

/**
 * ImageTypeMimeEnum
 *
 * @Description ImageTypeMimeEnum
 * @Author [author_name]
 * @Date 2077/8/12 16:23
 * @Version 1.0
 */
@Getter
public enum ImageTypeMimeEnum {

    JPG("jpg", "image/jpg"),
    JPEG("jpeg", "image/jpeg"),
    PNG("png", "image/png"),
    GIF("gif", "image/gif");

    private final String type;
    private final String mime;

    ImageTypeMimeEnum(String type, String mime) {
        this.type = type;
        this.mime = mime;
    }

    public static String getMimeByType(String type) {
        for (ImageTypeMimeEnum value : values()) {
            if (value.type.equals(type)) {
                return value.mime;
            }
        }
        return null;
    }
}
