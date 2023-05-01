package com.abc.system.common.constant;

/**
 * SystemRetConstantCode
 *
 * @Description SystemRetConstantCode 详细介绍
 * @Author Trivis
 * @Date 2023/5/1 10:53
 * @Version 1.0
 */
public enum SystemRetCodeConstants {
    SUCCESS("000000", "成功"),
    FAILED("000001", "处理失败"),
    USER_OR_PASSWORD_ERROR("003001", "用户名或密码不正确"),
    TOKEN_VALID_FAILED("003002", "token校验失败"),
    TOKEN_CREATE_FAILED("003003", "创建token失败"),
    USER_REGISTER_FAILED("003004", "注册失败，请联系管理员"),
    USER_IS_VERIFIED_ERROR("003000", "用户名尚未激活"),
    USER_HAS_ACTIVATED_ERROR("003001", "用户已经激活"),
    USER_INFO_INVALID("003005", "用户信息不合法"),
    USER_INFO_NOT_EXIST("003006", "用户信息不存在"),
    USER_INFO_EXIST("0030061", "用户已经存在"),
    USER_GET_SESSION_ERROR("003007", "获取用户登录状态异常"),
    USER_SAVE_SESSION_ERROR("0030071", "保存用户登录状态异常"),
    USER_NOT_LOGIN_ERROR("0030072", "用户未登录"),
    CSRF_TOKEN_ILLEGAL("003008", "csrf token校验失败"),
    CORS_REFERER_ILLEGAL("003009", "CORS Refer 校验失败"),
    REQUEST_FORMAT_ILLEGAL("003060", "请求数据格式非法"),
    REQUEST_IP_ILLEGAL("003061", "请求IP非法"),
    REQUEST_CHECK_FAILURE("003062", "请求数据校验失败"),
    DATA_NOT_EXIST("003070", "数据不存在"),
    DATA_REPEATED("003071", "数据重复"),
    REQUEST_DATA_NOT_EXIST("003072", "传入对象不能为空"),
    REQUEST_DATA_ERROR("003074", "必要的参数不正确"),
    REQUISITE_PARAMETER_NOT_EXIST("003073", "必要的参数不能为空"),
    PERMISSION_DENIED("003091", "权限不足"),
    DB_EXCEPTION("003097", "数据库异常"),
    SYSTEM_TIMEOUT("003098", "系统超时"),
    SYSTEM_ERROR("003099", "系统错误"),
    SYSTEM_BUSINESS("003100", "系统繁忙"),
    SYSTEM_IDENTIFY_URL_EMPTY("004000", "客户端回调地址为空"),
    SYSTEM_IDENTIFY_URL_ILLEGAL("004001", "客户端回调地址非法"),
    CREATE_GLOBAL_SERIAL_NUMBER_FAILED("005000", "生成全局唯一自增长流水失败"),
    SYSTEM_LOAD_DB_CONFIG_EMPTY("005000", "加载系统业务配置失败，请确认业务配置是否完整");

    private String code;
    private String message;

    private SystemRetCodeConstants(String code, String message) {
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