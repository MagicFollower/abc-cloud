package com.abc.system.common.request;

import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.page.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 系统内部Service层请求抽象基类（业务Service请求实体直接继承该抽象类）
 * <pre>
 * 1.part1: 与特定业务系统关联的Service层需要的基础属性
 * 2.part2: 增删改查数据接收属性 + 分页对象PageInfo
 * 3.part3: requestCheck查询参数校验抽象方法
 * </pre>
 */
@Data
public abstract class AbstractRequest<T> implements Serializable {

    /* 与特定业务系统关联的Service层需要的基础属性 */
    // private String service_level_tag1;
    // private String service_level_tag2;
    // private String service_level_tag3;

    protected T item;
    protected List<T> items;
    protected PageInfo pageInfo;  // 自定义分页对象

    /**
     * 分页对象初始化
     * <pre>
     * 1.分页对象在查询接口中使用，推荐在Service层检测/初始化而不是在Controller层，
     *   因为Service中的查询接口不仅仅为Controller提供服务，也会对Service提供服务，统一在Service层检测/初始化可以降低Service使用成本。
     * </pre>
     *
     * @return PageInfo实例
     */
    protected PageInfo initPageInfo() {
        if (pageInfo == null || pageInfo.getPageNum() <= 0 || pageInfo.getPageSize() <= 0) {
            pageInfo = new PageInfo();
        }
        return pageInfo;
    }

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
