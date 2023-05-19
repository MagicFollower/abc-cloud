package com.abc.business.fastlink.goods.api;

import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.response.BaseResponse;

/**
 * FastLinkGoodsService
 *
 * @Description FastLinkGoodsService 详细介绍
 * @Author Trivis
 * @Date 2023/5/13 22:50
 * @Version 1.0
 */
public interface FastLinkGoodsService {


    BaseResponse<String> query() throws BizException;
}
