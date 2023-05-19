package com.abc.business.fastlink.goods.service.impl;

import com.abc.business.fastlink.goods.api.FastLinkGoodsService;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.response.BaseResponse;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * FastlinkGoodsServiceImpl
 *
 * @Description FastlinkGoodsServiceImpl 详细介绍
 * @Author Trivis
 * @Date 2023/5/13 22:49
 * @Version 1.0
 */
@DubboService
public class FastlinkGoodsServiceImpl implements FastLinkGoodsService {


    @Override
    public BaseResponse<String> query() throws BizException {
        BaseResponse<String> response = new BaseResponse<>();
        // todo 模拟响应

        response.setCode(SystemRetCodeConstants.OP_SUCCESS.getCode());
        response.setMsg(SystemRetCodeConstants.OP_SUCCESS.getMessage());
        response.setResult("goods_data");
        response.setTotal(1L);
        return response;
    }
}
