package com.abc.system.common.exception;

import com.abc.system.common.exception.base.BaseException;

/**
 * 校验异常类
 *
 * @Description 校验异常类
 * @Author Trivis
 * @Date 2023/5/12 18:43
 * @Version 1.0
 */
public class ValidateException extends BaseException {

    public ValidateException() {
        super();
    }

    public ValidateException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public ValidateException(Throwable t) {
        super(t);
    }

    public ValidateException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ValidateException(String errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }

    public ValidateException(String errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }
}
