package com.abc.business.fastlink.portal.dto;

import com.abc.system.common.page.Pageable;
import lombok.Data;

import java.io.Serializable;

/**
 * Controller层请求实体
 * <pre>
 * 1.命名不同于Service层，使用Req作为后缀
 * 2.如果存在分页需要，需要单独实现Pageable接口
 *
 * 对于业务开发，该Req需要遵循如下约定：
 * 1.vo属性使用业务侧提供的VO，用于查询和删除接口
 * 2.增加和更新操作分为两种情况考虑，相关信息见类中属性注释。
 * </pre>
 *
 * @Description OrderReq
 * @Author Rake
 * @Date 2023/7/30 12:32
 * @Version 1.0
 */
@Data
public class OrderReq<T> implements Pageable, Serializable {
    /* 用于控制查询条件、删除条件ids */
    private T item;

    /* ==========场景一：可编辑列表========== */
    /* 1.新增的数据和修改的数据，将会在点击【save】按钮时统一传递给服务器，服务器自行判断增加数据集和更新数据集，接口统一为save🍏 */
    /* 2.对于增加和更新逻辑，建议根据id判断 */
    /* 3.该场景下，删除操作将会立刻生效，需提供单独的API，此时需要额外分为两种情况考虑 */
    /*   -> 3.1 对于已存在的数据删除需要调用删除API */
    /*   -> 3.2 对于新增未保存的数据因为没有ID，需要由前端开发人员控制不调用API直接删除 */
    // private List<T> saveItems;


    /* ==========场景二：无可编辑列表========== */
    /* 1.新增数据和编辑数据将完全区分开来，需要使用单独的数据集存储，并需要调用单独的add和update接口🍏🍏 */
    /* 2.该场景下，删除操作将会立刻生效，需提供单独的API，此时只有一种调用API的情况 */
    //private List<T> addItems;
    //private List<T> updateItems;
}
