package com.abc.business.web.controller.product;

import com.abc.business.api.IProductService;
import com.abc.business.dto.ProductDTO;
import com.abc.business.vo.ProductVO;
import com.abc.business.web.base.URL;
import com.abc.business.web.dal.entity.AbcProduct;
import com.abc.business.web.dal.persistence.AbcProductMapper;
import com.abc.business.web.dto.ProductReq;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.response.BaseResponse;
import com.abc.system.common.response.DefaultResponse;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final AbcProductMapper abcProductMapper;

    private final PlatformTransactionManager transactionManager;

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
        // BaseResponse<List<ProductDTO>> queryAllRes = productService.queryAll(null);
        // if (!SystemRetCodeConstants.OP_SUCCESS.getCode().equals(queryAllRes.getCode())) {
        //     return rp.setErrorMsg(queryAllRes.getCode(), queryAllRes.getMsg());
        // }
        // return rp.setData(queryAllRes.getResult(), SystemRetCodeConstants.OP_SUCCESS.getMessage());

        BaseResponse<List<ProductDTO>> baseResponse = productService.queryByIds(Lists.newArrayList(1L, 2L, 3L));
        if (!SystemRetCodeConstants.OP_SUCCESS.getCode().equals(baseResponse.getCode())) {
            return rp.setErrorMsg(baseResponse.getCode(), baseResponse.getMsg());
        }
        return rp.setData(baseResponse.getResult(), SystemRetCodeConstants.OP_SUCCESS.getMessage());
    }

    @PostMapping(URL.PRODUCT_ADD)
    public ResponseData<Void> add(@RequestBody ProductReq<ProductVO> request) {
        final ResponseProcessor<Void> rp = new ResponseProcessor<>();
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        // TODO
        List<AbcProduct> abcProducts1 = abcProductMapper.selectAll();
        System.out.println(">>>>>>>>>>>>>>>>>>>1");
        System.out.println(JSONObject.toJSONString(abcProducts1, JSONWriter.Feature.PrettyFormat));
        DefaultResponse addRes1 = productService.add(null);
        DefaultResponse addRes2 = productService.add(null);
        DefaultResponse addRes3 = productService.add(null);
        if (!SystemRetCodeConstants.OP_SUCCESS.getCode().equals(addRes1.getCode())) {
            return rp.setErrorMsg(addRes1.getCode(), addRes1.getMsg());
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>2");
        List<AbcProduct> abcProducts2 = abcProductMapper.selectAll();
        System.out.println(JSONObject.toJSONString(abcProducts2, JSONWriter.Feature.PrettyFormat));

        transactionManager.commit(transactionStatus);
        return rp.setData(null, SystemRetCodeConstants.OP_SUCCESS.getMessage());
    }

}
