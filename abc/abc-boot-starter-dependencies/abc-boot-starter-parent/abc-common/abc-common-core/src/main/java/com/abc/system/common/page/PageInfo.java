package com.abc.system.common.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统内部分页实体（供AbstractRequest使用）
 * <pre>
 * ❤️属性
 * 1.pageNum:  int  页数
 * 2.pageSize: int 页大小
 *
 * ❤️关于手动分页
 * 待分页列表：dtoList
 * 1.int total = dtoList.size()
 * 2.int pageNum = pageInfo.getPageNum();
 *   int pageSize = pageInfo.getPageSize();
 * 3.if(pageNum <= 0) pageNum = PageInfo.DEFAULT_PAGE_NUM;
 *   if(pageSize <= 0) pageSize = PageInfo.DEFAULT_PAGE_SIZE;
 * 4.int si = Math.min((pageNum-1)*pageSize, total);
 *   int ei = Math.min(si+pageSize, total)
 * 5.dtoList.subList(si, ei);  ->  subList接口两参数均支持<=dtoList.size()
 *
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo implements Serializable {
    public static final int DEFAULT_PAGE_NUM = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;

    private int pageNum = DEFAULT_PAGE_NUM;
    private int pageSize = DEFAULT_PAGE_SIZE;
}
