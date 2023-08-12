package com.abc.system.common.exception.file;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.base.BaseRuntimeException;

/**
 * 远程文件服务--文件下载异常
 * <pre>
 * 1.继承自BaseRuntimeException， 当前异常提供【远程文件服务下载】使用
 * 2.关于下载接口的使用示例 x2
 *   -> 你需要提前使用全局异常拦截器或手动包装异常响应给Client（用户不可见详细错误，他们只能看到"系统繁忙"，详细错误可以通过日志暴露给开发者）
 *   -> download接口已将HttpStatus设置为500在异常的情况下，前端仅需要根据HttpStatus决定是否下载/提示错误信息。
 * 2.1 使用全局异常拦截✨
 * {@code
 *     @GetMapping("/download")
 *     public void demo02(HttpServletResponse response) {
 *         final ResponseProcessor<String> rp = new ResponseProcessor<>();
 *         final String imgName = "img/X.png";
 *         minioService.download(imgName, response);
 *     }
 * }
 * 2.2 手动抛出异常✨
 * {@code
 *     @GetMapping("/download")
 *     public ResponseData<String> demo02(HttpServletResponse response) {
 *         final ResponseProcessor<String> rp = new ResponseProcessor<>();
 *         final String imgName = "img/Z.png";
 *         try {
 *             minioService.download(imgName, response);
 *             return null;
 *         } catch (RemoteFileDownloadException e) {
 *             log.error(">>>>>>>>|{}:{}|<<<<<<<<", e.getErrorCode(), e.getMessage());
 *             return new ResponseProcessor<String>().setErrorMsg(SystemRetCodeConstants.SYSTEM_BUSINESS);
 *         }
 *     }
 * }
 * </pre>
 *
 * @Description 远程文件服务--文件下载异常
 * @Author Rake
 * @Date 2023/8/12 0:32
 * @Version 1.0
 */
public class RemoteFileDownloadException extends BaseRuntimeException {
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
