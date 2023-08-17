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
        super(systemRetCodeConstants);
    }

    public BizException(SystemRetCodeConstants systemRetCodeConstants, Throwable cause) {
        super(systemRetCodeConstants, cause);
    }

    public BizException(String errorCode) {
        super(errorCode);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BizException(String errorCode, String message) {
        super(errorCode, message);
    }

    public BizException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
