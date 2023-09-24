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
import com.abc.system.dao.base.AbcBaseEntity;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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
//@DubboService
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

    @Override
    public BaseResponse<List<ProductDTO>> queryByIds(List<Long> ids) {
        BaseResponse<List<ProductDTO>> response = new BaseResponse<>();
        List<ProductDTO> dtoList;
        if(CollectionUtils.isEmpty(ids)) {
            response.fill(SystemRetCodeConstants.OP_SUCCESS);
            return response;
        }

        Example example = new Example(AbcProduct.class)
                .selectProperties(AbcBaseEntity.Fields.id, AbcProduct.Fields.code, AbcProduct.Fields.name);
        example.createCriteria().andIn(AbcBaseEntity.Fields.id, ids);
        List<AbcProduct> productList = productMapper.selectByExample(example);
        dtoList = productConverter.productEntity2DTO(productList);

        // example.clear()是否会清空selectProperties属性？（会）
        example.clear();
        List<AbcProduct> productList1 = productMapper.selectByExample(example);
        log.info(JSONObject.toJSONString(productList1, JSONWriter.Feature.PrettyFormat));

        response.fill(SystemRetCodeConstants.OP_SUCCESS);
        response.setTotal(dtoList.size());
        response.setResult(dtoList);
        return response;
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
        productMapper.insert(abcProduct);
        // 原数据的ID会被自动填充
        System.out.println(JSONObject.toJSONString(abcProduct, JSONWriter.Feature.PrettyFormat));

        // TODO
        defaultResponse.fill(SystemRetCodeConstants.OP_SUCCESS);
        return defaultResponse;
    }
}
