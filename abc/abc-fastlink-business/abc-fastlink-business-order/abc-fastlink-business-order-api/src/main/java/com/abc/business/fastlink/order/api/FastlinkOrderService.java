package com.abc.business.fastlink.order.api;

import com.abc.system.common.exception.BizException;
import com.abc.system.common.response.BaseResponse;

/**
 * FastlinkOrderService
 *
 * @Description FastlinkOrderService 详细介绍
 * @Author Trivis
 * @Date 2023/5/14 21:13
 * @Version 1.0
 */
public interface FastlinkOrderService {

    BaseResponse<String> query() throws BizException;
}
