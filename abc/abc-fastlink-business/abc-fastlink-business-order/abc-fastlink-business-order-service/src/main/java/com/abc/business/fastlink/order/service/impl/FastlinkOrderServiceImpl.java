package com.abc.business.fastlink.order.service.impl;

import com.abc.business.fastlink.goods.api.FastLinkGoodsService;
import com.abc.business.fastlink.order.api.FastlinkOrderService;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.base.BaseRuntimeException;
import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * FastlinkGoodsServiceImpl
 *
 * @Description FastlinkGoodsServiceImpl 详细介绍
 * @Author [author_name]
 * @Date 2077/5/13 22:49
 * @Version 1.0
 */
@Slf4j
@DubboService
public class FastlinkOrderServiceImpl implements FastlinkOrderService {

    @DubboReference(check = false, timeout = -1)
    private FastLinkGoodsService fastlinkGoodsService;


    @Override
    public void example02() {
        throw new BizException(SystemRetCodeConstants.ANONYMOUS);
    }


    @Override
    public BaseResponse<String> query() throws BizException {
        log.info(">>>>>>>>|query|start|<<<<<<<<");
        BaseResponse<String> orderResponse = new BaseResponse<>();
        try {
            BaseResponse<String> goodsResponse = fastlinkGoodsService.query();
            String goodsResult = goodsResponse.getResult();
            orderResponse.setCode(SystemRetCodeConstants.OP_SUCCESS.getCode());
            orderResponse.setMsg(SystemRetCodeConstants.OP_SUCCESS.getMessage());
            orderResponse.setResult(String.format("→ order_data:%s", goodsResult));
            orderResponse.setTotal(1L);
        } catch (BaseRuntimeException be) {
            log.info(">>>>>>>>|query|error|exception:", be);
            throw new BizException(be.getErrorCode(), be.getMessage());
        }
        log.info(">>>>>>>>|query|end|success|<<<<<<<<");
        return orderResponse;
    }
}
