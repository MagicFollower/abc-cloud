package com.abc.system.common.request;

import com.abc.system.common.exception.BizException;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统内部Service层请求基类
 * 1.Controller层请继承并抽象出AbstractBaseRequest,你可以把一些模板参数封装在内部，例如公司编号）
 * 2.Service层直接继承该抽象类
 */
@Data
public abstract class AbstractRequest implements Serializable {
    /**
     * 自定义分页实体（与Mybatis无关）
     * 1. pageNum:  int
     * 2. pageSize: int
     */
    protected PageInfo pageInfo;

    /**
     * 查询请求参数校验，这是一个tip，它会提醒你要在每一个服务中对参数进行校验
     * 1. 你可以为CRUD+Stop都设置单独的校验方法，和这里一样；
     * 2. 这个方法约定用于查询参数的请求校验。
     *
     * @throws BizException 校验失败时，抛出BizException
     */
    abstract void requestCheck() throws BizException;
}
