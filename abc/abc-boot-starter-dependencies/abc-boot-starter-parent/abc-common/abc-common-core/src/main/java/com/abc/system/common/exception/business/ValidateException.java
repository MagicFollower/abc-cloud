package com.abc.system.common.exception.business;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.base.BaseRuntimeException;

/**
 * 校验异常
 */
public class ValidateException extends BaseRuntimeException {
    public ValidateException() {
    }

    public ValidateException(SystemRetCodeConstants systemRetCodeConstants) {
        super();
        this.errorCode = systemRetCodeConstants.getCode();
        this.message = systemRetCodeConstants.getMessage();
    }

    public ValidateException(String errorCode) {
        this.errorCode = errorCode;
    }

    public ValidateException(Throwable cause) {
        super(cause);
    }

    public ValidateException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ValidateException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ValidateException(String errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }
}
