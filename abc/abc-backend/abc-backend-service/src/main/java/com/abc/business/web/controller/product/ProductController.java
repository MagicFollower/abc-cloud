package com.abc.business.web.controller.product;

import com.abc.business.api.IProductService;
import com.abc.business.dto.ProductDTO;
import com.abc.business.vo.ProductVO;
import com.abc.business.web.base.URL;
import com.abc.business.web.dto.ProductReq;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.page.PageResponse;
import com.abc.system.common.response.DefaultResponse;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping(URL.PRODUCT)
public class ProductController {

    private final IProductService productService;

    @PostMapping(URL.PRODUCT_ADD)
    public ResponseData<PageResponse<ProductDTO>> add(@RequestBody ProductReq<ProductVO> request) {
        final ResponseProcessor<PageResponse<ProductDTO>> rp = new ResponseProcessor<>();
        DefaultResponse addRes = productService.add(null);
        if (!SystemRetCodeConstants.OP_SUCCESS.getCode().equals(addRes.getCode())) {
            return rp.setErrorMsg(addRes.getCode(), addRes.getMsg());
        }
        return rp.setData(null, SystemRetCodeConstants.OP_SUCCESS.getMessage());
    }

}
