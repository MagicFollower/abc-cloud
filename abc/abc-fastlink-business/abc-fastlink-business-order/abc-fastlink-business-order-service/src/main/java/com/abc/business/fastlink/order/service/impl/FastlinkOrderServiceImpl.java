package com.abc.business.fastlink.order.service.impl;

import com.abc.business.fastlink.goods.api.FastLinkGoodsService;
import com.abc.business.fastlink.order.api.FastlinkOrderService;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.ExceptionProcessor;
import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.exception.business.XxxException;
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
    public void example01() throws XxxException {
        throw new XxxException();
    }

    @Override
    public BaseResponse<String> query() {
        BaseResponse<String> orderResponse = new BaseResponse<>();
        try {
            BaseResponse<String> goodsResponse = fastlinkGoodsService.query();
            String goodsResult = goodsResponse.getResult();
            orderResponse.setCode(SystemRetCodeConstants.OP_SUCCESS.getCode());
            orderResponse.setMsg(SystemRetCodeConstants.OP_SUCCESS.getMessage());
            orderResponse.setResult(goodsResult + "→" + "order_data");
            log.info(">>>>>>>> 数据当前在orderService处理中...[goodsService → orderService]");
            orderResponse.setTotal(1L);
        } catch (Exception e) {
            ExceptionProcessor.wrapAndHandleException(orderResponse, e);
        }
        return orderResponse;
    }
}
