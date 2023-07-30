package com.abc.system.common.request;

import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.page.Pageable;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统内部Service层请求基类
 * 1.Controller层请继承并抽象出AbstractBaseRequest,你可以把一些模板参数封装在内部，例如公司编号）
 * 2.Service层直接继承该抽象类
 */
@Data
public abstract class AbstractRequest implements Pageable, Serializable {

    /* 与特定业务系统关联的基础属性 */
    // private String brandCode;
    // private String tagId;

    /**
     * 查询请求参数校验，这是一个tip，它会提醒你要在每一个服务中对参数进行校验
     * <pre>
     * 1. 你可以为CRUD+Stop都设置单独的校验方法，和这里一样；
     * 2. 这个方法约定用于查询参数的请求校验；
     * 3. 将该方法用于Controller层，在进行服务RPC之前校验会有效减少无效服务调用。
     * </pre>
     *
     * @throws BizException 校验失败时，抛出BizException
     */
    protected abstract void requestCheck() throws BizException;
}
