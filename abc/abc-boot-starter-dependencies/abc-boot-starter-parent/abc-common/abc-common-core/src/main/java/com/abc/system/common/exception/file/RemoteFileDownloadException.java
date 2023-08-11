package com.abc.system.common.exception.file;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.base.BaseException;

/**
 * 远程文件服务--文件下载异常
 *
 * @Description 远程文件服务--文件下载异常
 * @Author Rake
 * @Date 2023/8/12 0:32
 * @Version 1.0
 */
public class RemoteFileDownloadException extends BaseException {
    public RemoteFileDownloadException() {
    }

    public RemoteFileDownloadException(SystemRetCodeConstants systemRetCodeConstants) {
        super();
        this.errorCode = systemRetCodeConstants.getCode();
        this.message = systemRetCodeConstants.getMessage();
    }

    public RemoteFileDownloadException(String errorCode) {
        this.errorCode = errorCode;
    }

    public RemoteFileDownloadException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public RemoteFileDownloadException(Throwable cause) {
        super(cause);
    }

    public RemoteFileDownloadException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public RemoteFileDownloadException(String errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }
}
