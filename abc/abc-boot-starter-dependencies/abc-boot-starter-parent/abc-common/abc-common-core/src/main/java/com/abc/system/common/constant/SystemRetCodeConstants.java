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
 * @Author [author_name]
 * @Date 2077/5/1 10:53
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
    //#####文件下载、预览模块相关响应编码 004
    //##############################################
    REMOTE_FILE_NOT_FOUND("004001", "文件不存在"),
    REMOTE_FILE_SERVICE_ERROR("004002", "文件服务异常"),


    //##############################################
    //#####Excel模块响应编码 005
    //##############################################
    EXCEL_TEMPLATE_CODE_LOST("005001", "缺少templateCode"),
    EXCEL_NOT_SUPPORT_ERROR("005002", "文件格式不支持"),
    EXCEL_NUM_ERROR("005003", "上传文件数量非法"),
    EXCEL_IS_NULL("005004", "上传Excel无法解析"),
    EXCEL_RULE_ERROR("005005", "Excel规则解析出错"),
    EXCEL_TYPE_ERROR("005006", "Excel数据类型解析校验异常"),
    EXCEL_COLUMN_MISMATCH("005007", "Excel字段与配置字段不匹配"),


    //##############################################
    //#####JWT（security）响应编码 006
    //##############################################
    JWT_GENERATE_ERROR("006001", "TOKEN生成异常"),
    JWT_PARSE_ERROR("006002", "TOKEN解析异常"),
    JWT_EXPIRED("006003", "TOKEN过期"),

    //##############################################
    //#####数据校验 响应编码 007
    //##############################################
    PARAMETER_EXISTS_ERROR("007001", "必要参数不存在"),




    //##############################################
    //#####MQ消息队列异常状态码 008
    //##############################################
    MQ_INIT_ERROR("008001", "MQ初始化异常"),
    MQ_MESSAGE_EMPTY("008002", "RocketMQ消息为空"),
    MQ_MESSAGE_LIST_EMPTY("008003", "RocketMQ消息列表为空"),
    MQ_MESSAGE_LOST_TOPIC("008004", "RocketMQ消息缺失Topic"),
    MQ_MESSAGE_LOST_GROUP("008005", "RocketMQ消息缺失Group"),
    MQ_MESSAGE_FORMAT_ERROR("008006", "RocketMQ消息格式异常"),
    MQ_MESSAGE_SEND_ERROR("008007", "RocketMQ消息发送失败"),




    //##############################################
    //#####登录异常状态码 009
    //##############################################
    LOGIN_USERNAME_PASSWORD_ERROR("009001", "用户名或密码错误"),
    LOGIN_UNAUTHORIZED("009401", "未授权访问"),








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
