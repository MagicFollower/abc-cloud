package com.abc.system.common.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统内部Service层请求基类
 * 1.提供简单的分页参数（pageNum + pageSize）
 */
@Data
public abstract class AbstractRequest implements Serializable {
    protected PageInfo pageInfo;
}
