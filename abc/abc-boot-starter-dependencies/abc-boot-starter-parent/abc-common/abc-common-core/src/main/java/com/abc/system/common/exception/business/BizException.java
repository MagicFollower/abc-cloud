package com.abc.system.common.exception.business;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.base.BaseRuntimeException;

/**
 * 系统内部自定义业务异常（BusinessException->缩写BizException）
 */
public class BizException extends BaseRuntimeException {
    public BizException() {
    }

    public BizException(SystemRetCodeConstants systemRetCodeConstants) {
        super();
        this.errorCode = systemRetCodeConstants.getCode();
        this.message = systemRetCodeConstants.getMessage();
    }

    public BizException(String errorCode) {
        this.errorCode = errorCode;
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BizException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public BizException(String errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }
}
