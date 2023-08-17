package com.abc.system.common.exception.redis;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.base.BaseRuntimeException;

/**
 * RedisException
 *
 * @Description RedisException
 * @Author Trivis
 * @Date 2023/5/15 23:27
 * @Version 1.0
 */
public class RedisException extends BaseRuntimeException {

    public RedisException() {
    }

    public RedisException(SystemRetCodeConstants systemRetCodeConstants) {
        super(systemRetCodeConstants);
    }

    public RedisException(SystemRetCodeConstants systemRetCodeConstants, Throwable cause) {
        super(systemRetCodeConstants, cause);
    }

    public RedisException(String errorCode) {
        super(errorCode);
    }

    public RedisException(Throwable cause) {
        super(cause);
    }

    public RedisException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public RedisException(String errorCode, String message) {
        super(errorCode, message);
    }

    public RedisException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
