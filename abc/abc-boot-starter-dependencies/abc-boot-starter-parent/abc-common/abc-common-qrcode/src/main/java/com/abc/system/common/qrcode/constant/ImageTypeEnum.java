package com.abc.system.common.qrcode.constant;

/**
 * AAA
 *
 * @Description AAA 详细介绍
 * @Author Trivis
 * @Date 2023/5/12 18:40
 * @Version 1.0
 */
public enum ImageTypeEnum {

    JPEG("JPG", "JPEG格式 "),
    PNG("PNG", "PNG格式");

    private String code;


    private String message;

    ImageTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据编码获取对应名称
     *
     * @param code code
     * @return message
     */
    public static String getImageTypeEnumByCode(String code) {
        for (ImageTypeEnum value : ImageTypeEnum.values()) {
            if (value.getCode().equalsIgnoreCase(code)) {
                return value.getMessage();
            }
        }
        return "";
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
