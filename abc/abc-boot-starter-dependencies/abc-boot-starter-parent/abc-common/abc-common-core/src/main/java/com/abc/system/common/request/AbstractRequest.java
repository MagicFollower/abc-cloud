package com.abc.system.common.request;

import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.page.Pageable;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 系统内部Service层请求基类
 * 1.Controller层请继承并抽象出AbstractBaseRequest,你可以把一些模板参数封装在内部，例如公司编号）
 * 2.Service层直接继承该抽象类
 */
@Data
public abstract class AbstractRequest<T> implements Pageable, Serializable {

    /* 与特定业务系统关联的Service层需要的基础属性 */
    // private String tag1;
    // private String tag2;
    // private String tag3;


    /* 用于控制查询条件、删除条件ids */
    protected T item;

    /* ==========场景一：可编辑列表========== */
    /* 1.新增的数据和修改的数据，将会在点击【save】按钮时统一传递给服务器，服务器自行判断增加数据集和更新数据集，接口统一为save🍏 */
    /* 2.对于增加和更新逻辑，建议根据id判断 */
    /* 3.该场景下，删除操作将会立刻生效，需提供单独的API，此时需要额外分为两种情况考虑 */
    /*   -> 3.1 对于已存在的数据删除需要调用删除API */
    /*   -> 3.2 对于新增未保存的数据因为没有ID，需要由前端开发人员控制不调用API直接删除 */
    protected List<T> saveItems;

    /* ==========场景二：无可编辑列表========== */
    /* 1.新增数据和编辑数据将完全区分开来，需要使用单独的数据集存储，并需要调用单独的add和update接口🍏🍏 */
    /* 2.该场景下，删除操作将会立刻生效，需提供单独的API，此时只有一种调用API的情况 */
    protected List<T> addItems;
    protected List<T> updateItems;


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
