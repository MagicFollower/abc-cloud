package com.abc.system.common.exception;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.base.BaseRuntimeException;

/**
 * RocketMQException
 *
 * @Description RocketMQException
 * @Author -
 * @Date 2023/8/18 13:28
 * @Version 1.0
 */
public class RocketMQException extends BaseRuntimeException {
    public RocketMQException(SystemRetCodeConstants systemRetCodeConstants) {
        this.errorCode = systemRetCodeConstants.getCode();
        this.message = systemRetCodeConstants.getMessage();
    }

    public RocketMQException(SystemRetCodeConstants systemRetCodeConstants, Throwable cause) {
        super(cause);
        this.errorCode = systemRetCodeConstants.getCode();
        this.message = systemRetCodeConstants.getMessage();
    }

    public RocketMQException(String errorCode) {
        this.errorCode = errorCode;
    }

    public RocketMQException(Throwable throwable) {
        super(throwable);
    }

    public RocketMQException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public RocketMQException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public RocketMQException(String errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }
}

