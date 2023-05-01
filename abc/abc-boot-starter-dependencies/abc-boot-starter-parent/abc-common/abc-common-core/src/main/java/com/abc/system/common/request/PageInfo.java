package com.abc.system.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统内部分页实体（供AbstractRequest使用）
 * 1.pageNum:Integer  页数
 * 2.pageSize:Integer 页大小
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
