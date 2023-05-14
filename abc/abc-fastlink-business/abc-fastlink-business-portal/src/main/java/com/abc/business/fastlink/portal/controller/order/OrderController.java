package com.abc.business.fastlink.portal.controller.order;

import com.abc.business.fastlink.order.api.FastlinkOrderService;
import com.abc.business.fastlink.portal.base.BaseUrl;
import com.abc.business.fastlink.portal.controller.order.constant.Url;
import com.abc.system.common.exception.base.BaseException;
import com.abc.system.common.page.PageResponse;
import com.abc.system.common.response.BaseResponse;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderController
 *
 * @Description OrderController 详细介绍
 * @Author Trivis
 * @Date 2023/5/14 21:39
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping(BaseUrl.BASE_URL)
public class OrderController {

    @DubboReference
    private FastlinkOrderService fastlinkOrderService;

    @PostMapping(Url.ORDER_BASE_QUERY)
    public ResponseData<PageResponse<String>> queryOrder() {
        try {
            BaseResponse<String> query = fastlinkOrderService.query();
            PageResponse<String> stringPageResponse = new PageResponse<>();
            stringPageResponse.setData(query.getResult());
            stringPageResponse.setTotal(query.getTotal());
            return new ResponseProcessor<PageResponse<String>>().setData(stringPageResponse, query.getMsg());
        } catch (BaseException be) {
            return new ResponseProcessor<PageResponse<String>>().setErrorMsg(be.getErrorCode(), be.getMessage());
        }
    }
}
