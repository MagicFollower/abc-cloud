package com.abc.business.web.controller.product;

import com.abc.business.api.IProductService;
import com.abc.business.dto.ProductDTO;
import com.abc.business.vo.ProductVO;
import com.abc.business.web.base.URL;
import com.abc.business.web.dto.ProductReq;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.page.PageResponse;
import com.abc.system.common.response.BaseResponse;
import com.abc.system.common.response.DefaultResponse;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品Controller
 *
 * @Description 商品Controller
 * @Author -
 * @Date 2023/8/20 22:57
 * @Version 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    // @DubboReference(check = true, retries = 0, timeout = 5000)
    private final IProductService productService;

    /**
     * 查询所有数据
     *
     * @param request ProductReq of ProductVO
     * @return ResponseData with List of ProductDTO
     */
    @PostMapping(URL.PRODUCT_QUERY_ALL)
    public ResponseData<List<ProductDTO>> queryAll(@RequestBody ProductReq<ProductVO> request) {
        final ResponseProcessor<List<ProductDTO>> rp = new ResponseProcessor<>();
        // TODO
        BaseResponse<List<ProductDTO>> queryAllRes = productService.queryAll(null);
        if (!SystemRetCodeConstants.OP_SUCCESS.getCode().equals(queryAllRes.getCode())) {
            return rp.setErrorMsg(queryAllRes.getCode(), queryAllRes.getMsg());
        }
        return rp.setData(queryAllRes.getResult(), SystemRetCodeConstants.OP_SUCCESS.getMessage());
    }

    @PostMapping(URL.PRODUCT_ADD)
    public ResponseData<Void> add(@RequestBody ProductReq<ProductVO> request) {
        final ResponseProcessor<Void> rp = new ResponseProcessor<>();
        // TODO
        DefaultResponse addRes = productService.add(null);
        if (!SystemRetCodeConstants.OP_SUCCESS.getCode().equals(addRes.getCode())) {
            return rp.setErrorMsg(addRes.getCode(), addRes.getMsg());
        }
        return rp.setData(null, SystemRetCodeConstants.OP_SUCCESS.getMessage());
    }

}
