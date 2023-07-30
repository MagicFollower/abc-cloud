package com.abc.system.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统内部Service层基本响应类（包含响应体数据，例如增加、查询）
 * <pre>
 * 1.total:long
 * 1.1 填充数据的总记录数
 * 1.2 默认值-1L，提示开发者务必手动填充该参数
 * 2.result:T
 * </pre>
 *
 * @param <T>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> extends AbstractResponse implements Serializable {
    private long total = -1L;
    private T result;
}
