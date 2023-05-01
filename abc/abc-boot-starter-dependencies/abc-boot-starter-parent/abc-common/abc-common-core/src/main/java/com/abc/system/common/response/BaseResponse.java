package com.abc.system.common.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 系统内部Service层基本响应类（包含响应体数据，例如增加、查询）
 * 1.total:Long
 *   1.1 填充数据的总记录数，分页时
 *   1.2 填充数据的总记录数，不分页时
 * 2.result:T
 *
 * @param <T>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseResponse<T> extends AbstractResponse implements Serializable {
    private Long total;
    private T result;
}
