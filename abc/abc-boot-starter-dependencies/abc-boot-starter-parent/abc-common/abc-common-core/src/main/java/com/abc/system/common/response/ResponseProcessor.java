package com.abc.system.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 响应处理器工具，Controller层使用，提供统一的API，用于快速生成ResponseData。
 *
 * @param <T> 响应体中result数据类型（基本实体、实体集合）
 * @Description 响应处理器工具，Controller层使用，提供统一的API，用于快速生成ResponseData。<br>
 * → 成功：<br>
 * → new ResponseData泛型().setData(response.getResult());<br>
 * → new ResponseData泛型().setData(response.getResult(), "操作成功");<br>
 * → 异常：<br>
 * → new ResponseData泛型().setErrorMessage(be.getMessage);<br>
 * → new ResponseData泛型().setErrorMessage(be.getErrorCode, be.getMessage);<br>
 * → new ResponseData泛型().setErrorMessage(SystemReturnCodeConstants.SYSTEM_ERROR.getMessage());<br>
 * → new ResponseData泛型().setErrorMessage(SystemReturnCodeConstants.SYSTEM_ERROR.getCode(),SystemReturnCodeConstants.SYSTEM_ERROR.getMessage());<br>
 * ⚠️提示：ResponseData在此处忽略泛型<br>
 * 1.setErrorMsg在提供内部错误编号和信息时，将会封装为ResponseErrorData至result中，使controller层代码泛型统一为T<br>
 */
public class ResponseProcessor<T> {
    private final ResponseData responseData = new ResponseData();

    public ResponseProcessor() {
        this.responseData.setSuccess(true);
        this.responseData.setMessage("success");
        this.responseData.setCode(200);
    }

    public ResponseData<T> setData(T t) {
        this.responseData.setResult(t);
        this.responseData.setSuccess(true);
        this.responseData.setCode(200);
        return this.responseData;
    }

    public ResponseData<T> setData(T t, String msg) {
        this.responseData.setResult(t);
        this.responseData.setSuccess(true);
        this.responseData.setMessage(msg);
        this.responseData.setCode(200);
        return this.responseData;
    }

    public ResponseData<Object> setData(int code, String errorCode, String msg) {
        this.responseData.setSuccess(true);
        this.responseData.setMessage(msg);
        this.responseData.setCode(code);
        if (StringUtils.isNotEmpty(errorCode)) {
            this.responseData.setResult(new ErrorResponse(errorCode, msg));
        }

        return this.responseData;
    }

    public ResponseData<T> setErrorMsg(String msg) {
        this.responseData.setMessage(msg);
        this.responseData.setCode(500);
        this.responseData.setSuccess(false);
        return this.responseData;
    }

    public ResponseData<T> setErrorMsg(String errorCode, String msg) {
        this.responseData.setMessage(msg);
        this.responseData.setCode(500);
        this.responseData.setSuccess(false);
        if (StringUtils.isNotEmpty(errorCode)) {
            this.responseData.setResult(new ErrorResponse(errorCode, msg));
        }

        return this.responseData;
    }

    public ResponseData<T> setErrorMsg(Integer code, String msg) {
        this.responseData.setMessage(msg);
        this.responseData.setCode(null == code ? 500 : code);
        this.responseData.setSuccess(false);
        return this.responseData;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ErrorResponse implements Serializable {
        private String errorCode;
        private String message;
    }
}