package com.abc.business.web.controller.product;

import com.abc.business.api.IProductTypeService;
import com.abc.business.dto.ProductTypeDTO;
import com.abc.business.vo.ProductTypeVO;
import com.abc.business.web.base.URL;
import com.abc.business.web.dto.ProductTypeReq;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.response.BaseResponse;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品类型Controller
 *
 * @Description 商品类型Controller
 * @Author -
 * @Date 2023/8/20 22:57
 * @Version 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductTypeController {

    // @DubboReference(check = true, retries = 0, timeout = 5000)
    private final IProductTypeService productTypeService;

    /**
     * 查询所有数据
     *
     * @param request ProductReq of ProductTypeVO
     * @return ResponseData
     */
    @PostMapping(URL.PRODUCT_TYPE_QUERY_ALL)
    public ResponseData<List<ProductTypeDTO>> queryAll(@RequestBody ProductTypeReq<ProductTypeVO> request) {
        final ResponseProcessor<List<ProductTypeDTO>> rp = new ResponseProcessor<>();
        // TODO
        BaseResponse<List<ProductTypeDTO>> queryAllRes = productTypeService.queryAll(null);
        if (!SystemRetCodeConstants.OP_SUCCESS.getCode().equals(queryAllRes.getCode())) {
            return rp.setErrorMsg(queryAllRes.getCode(), queryAllRes.getMsg());
        }
        return rp.setData(queryAllRes.getResult(), SystemRetCodeConstants.OP_SUCCESS.getMessage());
    }

}
