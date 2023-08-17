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
        super(systemRetCodeConstants);
    }

    public ValidateException(SystemRetCodeConstants systemRetCodeConstants, Throwable cause) {
        super(systemRetCodeConstants, cause);
    }

    public ValidateException(String errorCode) {
        super(errorCode);
    }

    public ValidateException(Throwable cause) {
        super(cause);
    }

    public ValidateException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ValidateException(String errorCode, String message) {
        super(errorCode, message);
    }

    public ValidateException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
