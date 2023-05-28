package com.abc.system.common.constant;

/**
 * 系统响应编码
 *
 * @Description <pre>
 * 系统响应编码
 * 1. 6位编码(******)
 *   1.1 000 操作编码
 *   1.2 001 核心模块响应编码
 *   1.3 002 Redis缓存模块响应编码
 * </pre>
 * @Author Trivis
 * @Date 2023/5/1 10:53
 * @Version 1.0
 */
public enum SystemRetCodeConstants {
    //##############################################
    //#####操作编码 000
    //##############################################
    OP_SUCCESS("000000", "操作成功"),
    OP_FAILED("000001", "操作失败"),

    //##############################################
    //#####核心模块响应编码 001
    //##############################################
    SYSTEM_ERROR("001001", "系统错误"),
    SYSTEM_TIMEOUT("001001", "系统超时"),
    SYSTEM_BUSINESS("001002", "系统繁忙"),

    //##############################################
    //#####Redis缓存模块响应编码 002
    //##############################################


    //##############################################
    //#####文件上传模块响应编码 003
    //##############################################


    //##############################################
    //#####文件下载模块响应编码 004
    //##############################################


    //##############################################
    //#####Excel模块响应编码 005
    //##############################################
    EXCEL_TEMPLATE_CODE_LOST("005001", "缺少templateCode"),
    EXCEL_NOT_SUPPORT_ERROR("005002", "文件格式不支持"),
    EXCEL_NUM_ERROR("005003", "上传文件数量非法"),
    EXCEL_IS_NULL("005004", "上传Excel无法解析"),
    EXCEL_RULE_ERROR("005005", "Excel规则解析出错"),
    EXCEL_TYPE_ERROR("005006", "Excel数据类型解析异常"),


    //##############################################
    //#####JWT（security）响应编码 006
    //##############################################
    JWT_GENERATE_ERROR("006001", "TOKEN生成异常"),
    JWT_PARSE_ERROR("006002", "TOKEN解析异常"),





    
    ANONYMOUS("999999", "ANONYMOUS-MESSAGE");

    private String code;
    private String message;

    SystemRetCodeConstants(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(String code) {
        SystemRetCodeConstants[] values = values();

        for (SystemRetCodeConstants codeConstants : values) {
            if (null == code) {
                return null;
            }

            if (codeConstants.getCode().equals(code)) {
                return codeConstants.message;
            }
        }

        return null;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}