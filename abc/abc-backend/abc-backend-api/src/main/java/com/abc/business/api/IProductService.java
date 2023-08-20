package com.abc.business.api;

import com.abc.business.dto.ProductRequest;
import com.abc.business.vo.ProductVO;
import com.abc.system.common.response.DefaultResponse;

/**
 * 商品接口定义
 *
 * @Description 商品接口定义
 * @Author -
 * @Date 2023/8/20 22:59
 * @Version 1.0
 */
public interface IProductService {

    /**
     * 新增商品接口
     *
     * @param request ProductRequest of ProductVO
     * @return DefaultResponse
     */
    DefaultResponse add(ProductRequest<ProductVO> request);
}
