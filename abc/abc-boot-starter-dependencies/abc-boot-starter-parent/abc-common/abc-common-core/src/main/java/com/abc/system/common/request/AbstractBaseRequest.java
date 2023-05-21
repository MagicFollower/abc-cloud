package com.abc.system.common.request;

import java.io.Serializable;
import java.util.List;

/**
 * 抽象基础请求，抽象请求类的上层抽象
 *
 * @Description
 * <pre>
 *     抽象基础请求，抽象请求类的上层抽象，面向Controller，规范CRUD接口统一入参
 *     1.addItems
 *     2.deleteItems
 *     3.updateItems
 *     4.item
 * </pre>
 * @Author Trivis
 * @Date 2023/5/21 10:58
 * @Version 1.0
 */
public abstract class AbstractBaseRequest<T> extends AbstractRequest implements Serializable {
    /**
     * 待增加数据集
     */
    protected List<T> addItems;
    /**
     * 待删除数据集
     */
    protected List<T> deleteItems;
    /**
     * 待更新数据集
     */
    protected List<T> updateItems;
    /**
     * 查询条件数据
     */
    protected T item;
}
