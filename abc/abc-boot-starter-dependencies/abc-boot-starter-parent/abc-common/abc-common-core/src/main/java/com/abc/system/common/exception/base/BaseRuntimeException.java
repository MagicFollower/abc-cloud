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
public class BaseRuntimeException extends RuntimeException {
    protected String errorCode;
    protected String message;

    public BaseRuntimeException() {
    }

    public BaseRuntimeException(SystemRetCodeConstants systemRetCodeConstants) {
        super();
        this.errorCode = systemRetCodeConstants.getCode();
        this.message = systemRetCodeConstants.getMessage();
    }

    public BaseRuntimeException(SystemRetCodeConstants systemRetCodeConstants, Throwable cause) {
        super(cause);
        this.errorCode = systemRetCodeConstants.getCode();
        this.message = systemRetCodeConstants.getMessage();
    }

    public BaseRuntimeException(String errorCode) {
        this.errorCode = errorCode;
    }

    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }

    public BaseRuntimeException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BaseRuntimeException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public BaseRuntimeException(String errorCode, String message, Throwable cause) {
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
