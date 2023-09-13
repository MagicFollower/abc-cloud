package com.abc.business.web.service.impl;

import com.abc.business.api.IProductTypeService;
import com.abc.business.dto.ProductRequest;
import com.abc.business.dto.ProductTypeDTO;
import com.abc.business.vo.ProductTypeVO;
import com.abc.business.web.converter.ProductTypeConverter;
import com.abc.business.web.dal.entity.AbcProductType;
import com.abc.business.web.dal.persistence.AbcProductTypeMapper;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.ExceptionProcessor;
import com.abc.system.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品类型接口实现
 *
 * @Description 商品类型接口实现
 * @Author -
 * @Date 2023/8/20 23:12
 * @Version 1.0
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements IProductTypeService {

    private final ProductTypeConverter productTypeConverter;

    private final AbcProductTypeMapper productTypeMapper;

    @Override
    public BaseResponse<List<ProductTypeDTO>> queryAll(ProductRequest<ProductTypeVO> request) {
        BaseResponse<List<ProductTypeDTO>> baseResponse = new BaseResponse<>();
        List<ProductTypeDTO> dtoList;

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            List<AbcProductType> productTypeList = productTypeMapper.selectAll();
            dtoList = productTypeConverter.productEntity2DTO(productTypeList);
        } catch (Exception e) {
            log.error(">>>>>>>>|queryAll|error|exception:{}|<<<<<<<<", e.getMessage(), e);
            ExceptionProcessor.wrapAndHandleException(baseResponse, e);
            return baseResponse;
        }

        baseResponse.fill(SystemRetCodeConstants.OP_SUCCESS);
        baseResponse.setResult(dtoList);
        baseResponse.setTotal(dtoList.size());
        return baseResponse;
    }
}
