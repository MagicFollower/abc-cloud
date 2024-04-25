package com.abc.system.web.config;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.base.BaseRuntimeException;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局异常拦截器
 *
 * @Description 全局异常拦截器
 * @Author [author_name]
 * @Date 2077/8/1 21:42
 * @Version 1.0
 */
@ControllerAdvice
@ConditionalOnClass({Controller.class, RestController.class})
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseRuntimeException.class)
    @ResponseBody
    public ResponseData<?> handleException(BaseRuntimeException e) {
        log.error(String.format(">>>>>>>>|全局异常拦截生效中(BaseRuntimeException): %s|<<<<<<<<", e.getMessage()));
        return new ResponseProcessor<>().setErrorMsg(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseData<?> handleException(Exception e) {
        log.error(String.format(">>>>>>>>|全局异常拦截生效中(Exception): %s|<<<<<<<<", e.getMessage()));
        return new ResponseProcessor<>().setErrorMsg(SystemRetCodeConstants.SYSTEM_BUSINESS);
    }
}
