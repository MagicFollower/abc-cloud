package com.abc.system.common.exception;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.base.BaseRuntimeException;
import com.abc.system.common.response.AbstractResponse;

/**
 * 异常处理器工具
 *
 * @Description <pre>
 *   1.用于微服务Service层对异常（服务层可预测异常BaseException的子类）信息的包装，将异常编码+信息写入到AbstractResponse中；
 *   2.命名风格和ResponseProcessor保持一致。
 * </pre>
 * @Author [author_name]
 * @Date 2077/7/3 13:10
 * @Version 1.0
 */
public class ExceptionProcessor {

    private ExceptionProcessor() {
    }

    public static void wrapAndHandleException(AbstractResponse abstractResponse, Throwable e) {
        if (e instanceof BaseRuntimeException) {
            abstractResponse.setCode(((BaseRuntimeException) e).getErrorCode());
            abstractResponse.setMsg(e.getMessage());
        } else {
            // 未预测异常，不处理
            abstractResponse.fill(SystemRetCodeConstants.SYSTEM_ERROR);
        }
    }
}
