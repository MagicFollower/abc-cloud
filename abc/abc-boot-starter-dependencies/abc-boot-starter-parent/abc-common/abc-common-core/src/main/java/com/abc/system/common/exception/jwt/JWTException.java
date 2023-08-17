package com.abc.system.common.exception.jwt;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.base.BaseRuntimeException;

/**
 * JWTException
 *
 * @Description JWTException
 * @Author Trivis
 * @Date 2023/5/28 8:50
 * @Version 1.0
 */
public class JWTException extends BaseRuntimeException {
    public JWTException() {
    }

    public JWTException(SystemRetCodeConstants systemRetCodeConstants) {
        super(systemRetCodeConstants);
    }

    public JWTException(SystemRetCodeConstants systemRetCodeConstants, Throwable cause) {
        super(systemRetCodeConstants, cause);
    }

    public JWTException(String errorCode) {
        super(errorCode);
    }

    public JWTException(Throwable cause) {
        super(cause);
    }

    public JWTException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public JWTException(String errorCode, String message) {
        super(errorCode, message);
    }

    public JWTException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
