package com.abc.system.common.exception;

/**
 * 系统内部自定义业务异常（BusinessException->缩写BizException）
 */
public class BizException extends BaseException {
    public BizException() {
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
