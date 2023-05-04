package com.abc.system.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统内部分页实体（供AbstractRequest使用）
 * 1.pageNum:  int  页数
 * 2.pageSize: int 页大小
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
    private int pageNum = 1;
    private int pageSize = 10;
}
