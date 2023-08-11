package com.abc.business.fastlink.order.service.impl;

import com.abc.business.fastlink.goods.api.FastLinkGoodsService;
import com.abc.business.fastlink.order.api.FastlinkOrderService;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.exception.base.BaseRuntimeException;
import com.abc.system.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * FastlinkGoodsServiceImpl
 *
 * @Description FastlinkGoodsServiceImpl 详细介绍
 * @Author Trivis
 * @Date 2023/5/13 22:49
 * @Version 1.0
 */
@Slf4j
@DubboService
public class FastlinkOrderServiceImpl implements FastlinkOrderService {

    @DubboReference
    private FastLinkGoodsService fastlinkGoodsService;


    @Override
    public void example02() {
        throw new BizException(SystemRetCodeConstants.ANONYMOUS);
    }


    @Override
    public BaseResponse<String> query() throws BizException {
        BaseResponse<String> orderResponse = new BaseResponse<>();
        try {
            BaseResponse<String> goodsResponse = fastlinkGoodsService.query();
            String goodsResult = goodsResponse.getResult();
            orderResponse.setCode(SystemRetCodeConstants.OP_SUCCESS.getCode());
            orderResponse.setMsg(SystemRetCodeConstants.OP_SUCCESS.getMessage());
            orderResponse.setResult(goodsResult + "→" + "order_data");
            orderResponse.setTotal(1L);
        } catch (BaseRuntimeException be) {
            throw new BizException(be.getErrorCode(), be.getMessage());
        }
        return orderResponse;
    }
}
