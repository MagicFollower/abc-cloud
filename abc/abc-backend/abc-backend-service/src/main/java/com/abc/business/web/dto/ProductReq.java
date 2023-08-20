package com.abc.business.web.dto;

import com.abc.system.common.request.AbstractReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品Controller请求实体
 *
 * @Description 商品Controller请求实体
 * @Author -
 * @Date 2023/8/20 23:09
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductReq<T> extends AbstractReq<T> {

}
