package com.abc.business.web.service.impl;

import com.abc.business.api.IProductService;
import com.abc.business.dto.ProductDTO;
import com.abc.business.dto.ProductRequest;
import com.abc.business.vo.ProductVO;
import com.abc.business.web.converter.ProductConverter;
import com.abc.business.web.dal.entity.AbcProduct;
import com.abc.business.web.dal.entity.AbcProductType;
import com.abc.business.web.dal.persistence.AbcProductMapper;
import com.abc.business.web.dal.persistence.AbcProductTypeMapper;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.ExceptionProcessor;
import com.abc.system.common.response.BaseResponse;
import com.abc.system.common.response.DefaultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品接口实现
 *
 * @Description 商品接口实现
 * @Author -
 * @Date 2023/8/20 23:12
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductConverter productConverter;

    private final AbcProductMapper productMapper;
    private final AbcProductTypeMapper productTypeMapper;

    @Override
    public BaseResponse<List<ProductDTO>> queryAll(ProductRequest<ProductVO> request) {
        BaseResponse<List<ProductDTO>> baseResponse = new BaseResponse<>();
        List<ProductDTO> dtoList;

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            Map<Long, String> productTypeIdToNameMap = productTypeMapper.selectAll()
                    .stream().collect(Collectors.toMap(AbcProductType::getId, AbcProductType::getName));
            List<AbcProduct> abcProducts = productMapper.selectAll();
            dtoList = productConverter.productEntity2DTO(abcProducts);
            dtoList.forEach(x -> x.setTypeName(productTypeIdToNameMap.get(x.getId())));
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

    /**
     * developing...
     *
     * @param request ProductRequest of ProductVO
     * @return DefaultResponse
     */
    @Override
    public DefaultResponse add(ProductRequest<ProductVO> request) {
        DefaultResponse defaultResponse = new DefaultResponse();

        AbcProduct abcProduct = new AbcProduct();
        productMapper.insertSelective(abcProduct);

        // TODO
        defaultResponse.fill(SystemRetCodeConstants.OP_SUCCESS);
        return defaultResponse;
    }
}
