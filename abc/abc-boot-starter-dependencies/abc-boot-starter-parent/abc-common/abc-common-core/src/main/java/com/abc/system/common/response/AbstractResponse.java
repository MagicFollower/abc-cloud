package com.abc.system.common.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统内部Service层响应基类
 *   → 常用在Service层方法正常响应时，手动填充成功编码+成功消息
 * 1.code:String → 命名不同，区别于异常基类中的errorCode
 * 2.msg:String  → 命名不同，区别于异常基类中的message
 */
@Data
public abstract class AbstractResponse implements Serializable {
    protected String code;
    protected String msg;
}
