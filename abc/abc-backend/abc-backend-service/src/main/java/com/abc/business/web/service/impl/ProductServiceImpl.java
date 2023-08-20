package com.abc.business.web.service.impl;

import com.abc.business.dto.ProductRequest;
import com.abc.business.vo.ProductVO;
import com.abc.business.web.dal.entity.AbcProduct;
import com.abc.business.web.dal.persistence.AbcProductMapper;
import com.abc.business.api.IProductService;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.response.DefaultResponse;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private final AbcProductMapper productMapper;

    @Override
    public DefaultResponse add(ProductRequest<ProductVO> request) {
        DefaultResponse defaultResponse = new DefaultResponse();
        List<AbcProduct> abcProducts = productMapper.selectAll();
        System.out.println(JSONObject.toJSONString(abcProducts, JSONWriter.Feature.PrettyFormat));



        defaultResponse.fill(SystemRetCodeConstants.OP_SUCCESS);
        return defaultResponse;
    }
}
