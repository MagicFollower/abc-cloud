package com.abc.system.common.qrcode.constant;

import lombok.Getter;

/**
 * ImageTypeEnum
 *
 * @Description ImageTypeEnum
 * @Author Trivis
 * @Date 2023/5/12 18:40
 * @Version 1.0
 */
@Getter
public enum ImageTypeEnum {

    JPEG("JPG"),
    PNG("PNG");

    private final String code;

    ImageTypeEnum(String code) {
        this.code = code;
    }

    /**
     * 根据code获取对应的枚举
     *
     * @param code CODE
     * @return ImageTypeEnum
     */
    public static ImageTypeEnum getImageTypeEnum(String code) {
        for (ImageTypeEnum value : ImageTypeEnum.values()) {
            if (value.getCode().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }
}
