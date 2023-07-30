package com.abc.system.common.page;

import java.io.Serializable;

/**
 * Pageable
 *
 * @Description Pageable
 * @Author Rake
 * @Date 2023/7/30 12:01
 * @Version 1.0
 */
public interface Pageable extends Serializable {
    /**
     * 自定义分页实体（与其他框架无关）
     * 1. pageNum:  int
     * 2. pageSize: int
     */
    PageInfo pageInfo = new PageInfo();
}
