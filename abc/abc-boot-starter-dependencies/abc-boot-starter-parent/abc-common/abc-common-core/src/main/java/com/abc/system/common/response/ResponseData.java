package com.abc.system.common.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统内部Controller层响应实体
 * 1.success:boolean
 *   1.1 前端根据响应体中的该参数判定请求是否正常响应（200/500）
 *   1.2 正常情况下封装的实体内部约定使用包装类，尤其是布尔类型值（但当前类的封装仅用于数据的响应，无影响）
 *       → boolean类型数据某些框架中对getter的生成是isXxx格式，而非getXxx格式；
 *       → Boolean包装类则统一为getXxx，避免业务复杂时出现难以排查的问题，约定统一使用包装类。
 * 2.code:int
 * 3.message:String
 * 4.result:T
 * 5.timestamp:long
 *   5.1 时间戳，自动填充，无需改动
 *
 * @param <T>
 */
@Data
@NoArgsConstructor
public class ResponseData<T> implements Serializable {
    private Boolean success;
    private int code;
    private String message;
    private T result;

    private long timestamp = System.currentTimeMillis();
}
