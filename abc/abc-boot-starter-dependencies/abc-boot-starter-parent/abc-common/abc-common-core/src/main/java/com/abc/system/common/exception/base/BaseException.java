package com.abc.system.common.exception.base;

import com.abc.system.common.constant.SystemRetCodeConstants;

/**
 * <pre>
 * 系统内部自定义异常基类
 * 1.errorCode:String
 * 2.message:String
 *   2.1 使用message属性是为了与Throwable接口中message保持一致，从而与普通异常在调用获取异常消息API时保持一致性。
 * </pre>
 */
public class BaseException extends Exception {
    protected String errorCode;
    protected String message;

    public BaseException() {
    }

    public BaseException(SystemRetCodeConstants systemRetCodeConstants) {
        super();
        this.errorCode = systemRetCodeConstants.getCode();
        this.message = systemRetCodeConstants.getMessage();
    }

    public BaseException(SystemRetCodeConstants systemRetCodeConstants, Throwable cause) {
        super(cause);
        this.errorCode = systemRetCodeConstants.getCode();
        this.message = systemRetCodeConstants.getMessage();
    }

    public BaseException(String errorCode) {
        this.errorCode = errorCode;
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BaseException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public BaseException(String errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
