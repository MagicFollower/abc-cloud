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
import com.abc.system.common.response.DefaultResponse;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.RequiredArgsConstructor;
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
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductConverter productConverter;

    private final AbcProductMapper productMapper;
    private final AbcProductTypeMapper productTypeMapper;

    @Override
    public DefaultResponse add(ProductRequest<ProductVO> request) {
        DefaultResponse defaultResponse = new DefaultResponse();
        Map<Long, String> productTypeIdToNameMap = productTypeMapper.selectAll()
                .stream().collect(Collectors.toMap(AbcProductType::getId, AbcProductType::getName));
        List<AbcProduct> abcProducts = productMapper.selectAll();
        List<ProductDTO> productDTOList = productConverter.productEntity2DTO(abcProducts);
        productDTOList.forEach(x -> x.setTypeName(productTypeIdToNameMap.get(x.getId())));

        System.out.println(JSONObject.toJSONString(abcProducts, JSONWriter.Feature.PrettyFormat));

        defaultResponse.fill(SystemRetCodeConstants.OP_SUCCESS);
        return defaultResponse;
    }
}
