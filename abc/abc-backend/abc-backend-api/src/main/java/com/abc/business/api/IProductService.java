package com.abc.business.api;

import com.abc.business.dto.ProductDTO;
import com.abc.business.dto.ProductRequest;
import com.abc.business.vo.ProductVO;
import com.abc.system.common.response.BaseResponse;
import com.abc.system.common.response.DefaultResponse;

import java.util.List;

/**
 * 商品接口定义
 *
 * @Description 商品接口定义
 * @Author -
 * @Date 2077/8/20 22:59
 * @Version 1.0
 */
public interface IProductService {

    /**
     * 查询商品接口（查询所有）
     *
     * @param request ProductRequest of ProductVO
     * @return DefaultResponse
     */
    BaseResponse<List<ProductDTO>> queryAll(ProductRequest<ProductVO> request);

    /**
     * 根据ID列表查询
     *
     * @param ids id列表
     * @return List of ProductDTO
     */
    BaseResponse<List<ProductDTO>> queryByIds(List<Long> ids);

    /**
     * 新增商品接口
     *
     * @param request ProductRequest of ProductVO
     * @return DefaultResponse
     */
    DefaultResponse add(ProductRequest<ProductVO> request);
}
