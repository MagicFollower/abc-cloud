package com.abc.system.common.request;

import com.abc.system.common.page.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 系统内部Controller层请求抽象基类（业务Controller请求实体直接继承该抽象类）
 * <pre>
 * 1.part1: 与特定业务系统关联的Controller层需要的基础属性
 * 2.part2: 增删改查数据接收属性 + 分页对象PageInfo（与Service层抽象基类属性保持一致）
 * </pre>
 *
 * @Description AbstractReq
 * @Author Rake
 * @Date 2023/7/30 15:46
 * @Version 1.0
 */
@Data
public abstract class AbstractReq<T> implements Serializable {

    /* 与特定业务系统关联的Service层需要的基础属性 */
    // private String controller_level_tag1;
    // private String controller_level_tag2;
    // private String controller_level_tag3;

    /* 基础属性，同AbstractRequest属性保持一致 */
    protected T item;
    protected List<T> saveItems;
    protected List<T> addItems;
    protected List<T> updateItems;
    protected PageInfo pageInfo;
}
