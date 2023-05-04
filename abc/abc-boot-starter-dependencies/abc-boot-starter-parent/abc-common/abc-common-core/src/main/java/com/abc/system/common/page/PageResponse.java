package com.abc.system.common.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * PageResponse
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
    private long total;
    private T data;
}
