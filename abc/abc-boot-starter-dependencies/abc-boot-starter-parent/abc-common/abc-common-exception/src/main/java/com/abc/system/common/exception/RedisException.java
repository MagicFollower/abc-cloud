package com.abc.system.common.exception;

import com.abc.system.common.exception.base.BaseException;

/**
 * RedisException
 *
 * @Description RedisException
 * @Author Trivis
 * @Date 2023/5/15 23:27
 * @Version 1.0
 */
public class RedisException extends BaseException {

    public RedisException() {
        super();
    }

    public RedisException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public RedisException(Throwable cause) {
        super(cause);
    }

    public RedisException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public RedisException(String errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }

    public RedisException(String errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }
}
