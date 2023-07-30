package com.abc.system.common.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * PageResponse
 * <pre>
 * 1.total默认值-1L，提示开发者务必手动填充该参数
 * </pre>
 *
 * @Description PageResponse 详细介绍
 * @Author Trivis
 * @Date 2023/5/4 20:38
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> implements Serializable {
    // 未分页结果集的大小
    private long total = -1L;

    // 根据分页参数限制后的分页数据集
    private T data;
}
