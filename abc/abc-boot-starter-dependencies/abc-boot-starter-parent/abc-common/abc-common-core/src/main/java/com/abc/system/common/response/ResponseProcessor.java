package com.abc.system.common.response;

import com.abc.system.common.constant.SystemRetCodeConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 响应处理器工具，Controller层使用，提供2个统一的API，用于快速生成ResponseData。
 *
 * @param <T> 响应体中result数据类型（基本实体、实体集合）
 * @Description <pre>
 * 响应处理器工具，Controller层使用，提供统一的API，用于快速生成ResponseData。
 * → 成功：
 *     → new ResponseProcessor泛型().setData(response.getResult(), response.getMsg());
 * → 异常：
 *     → new ResponseProcessor空泛型().setErrorMessage(be.getErrorCode, be.getMessage);
 *     → new ResponseProcessor空泛型().setErrorMessage(SystemReturnCodeConstants.SYSTEM_ERROR.getCode(),
 *                                               SystemReturnCodeConstants.SYSTEM_ERROR.getMessage());
 * ⚠️提示：ResponseData在此处忽略泛型
 * 1.setErrorMsg在提供内部错误编号和信息时，将会封装为ResponseErrorData至result中，使controller层代码泛型统一为T
 * </pre>
 */
public class ResponseProcessor<T> {
    private final ResponseData responseData = new ResponseData();

    public ResponseProcessor() {
    }

    /**
     * 🔓统一成功响应消息
     *
     * @param t   t
     * @param msg msg
     * @return ResponseData
     */
    public ResponseData<T> setData(T t, String msg) {
        this.responseData.setSuccess(true);
        this.responseData.setCode(200);
        this.responseData.setMessage(msg == null ? SystemRetCodeConstants.OP_SUCCESS.getMessage() : msg);
        this.responseData.setResult(t);
        return this.responseData;
    }

    /**
     * 🔓捕获BizException时使用（捕获后直接使用errorCode+message填充返回）
     *
     * @param errorCode errorCode
     * @param msg       msg
     * @return ResponseData
     */
    public ResponseData<T> setErrorMsg(String errorCode, String msg) {
        this.responseData.setSuccess(false);
        this.responseData.setCode(500);
        this.responseData.setMessage(msg);
        if (StringUtils.isNotEmpty(errorCode)) {
            this.responseData.setResult(new ErrorResponse(errorCode, msg));
        }

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