package com.abc.business.api;

import com.abc.business.dto.ProductRequest;
import com.abc.business.dto.ProductTypeDTO;
import com.abc.business.vo.ProductTypeVO;
import com.abc.system.common.response.BaseResponse;

import java.util.List;

/**
 * 商品接口定义
 *
 * @Description 商品接口定义
 * @Author -
 * @Date 2077/8/20 22:59
 * @Version 1.0
 */
public interface IProductTypeService {

    /**
     * 查询商品类型接口（查询所有）
     *
     * @param request ProductRequest of ProductTypeVO
     * @return DefaultResponse
     */
    BaseResponse<List<ProductTypeDTO>> queryAll(ProductRequest<ProductTypeVO> request);
}
