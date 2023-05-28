package com.abc.system.common.exception.jwt;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.base.BaseException;

/**
 * JWTException
 *
 * @Description JWTException
 * @Author Trivis
 * @Date 2023/5/28 8:50
 * @Version 1.0
 */
public class JWTException extends BaseException {
    public JWTException() {
        super();
    }

    public JWTException(SystemRetCodeConstants systemRetCodeConstants) {
        super();
        this.errorCode = systemRetCodeConstants.getCode();
        this.message = systemRetCodeConstants.getMessage();
    }

    public JWTException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public JWTException(Throwable cause) {
        super(cause);
    }

    public JWTException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public JWTException(String errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }

    public JWTException(String errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }
}
