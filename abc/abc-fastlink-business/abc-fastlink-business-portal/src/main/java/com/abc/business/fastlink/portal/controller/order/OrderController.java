package com.abc.business.fastlink.portal.controller.order;

import com.abc.business.fastlink.order.api.IDemoOrderService;
import com.abc.business.fastlink.order.dto.OrderDTO;
import com.abc.business.fastlink.portal.base.BaseUrl;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.page.PageResponse;
import com.abc.system.common.response.BaseResponse;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * OrderController
 *
 * @Description OrderController 测试使用
 * @Author Trivis
 * @Date 2023/5/14 21:39
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping(BaseUrl.BASE_URL)
@RequiredArgsConstructor
public class OrderController {

    @DubboReference(check = false, timeout = 5000)
    private IDemoOrderService iDemoOrderService;


    @GetMapping("/demo01")
    public ResponseData<PageResponse<List<OrderDTO>>> demo01() {
        final ResponseProcessor<PageResponse<List<OrderDTO>>> rp = new ResponseProcessor<>();
        final BaseResponse<List<OrderDTO>> baseResponse = iDemoOrderService.demo01();
        if (!SystemRetCodeConstants.OP_SUCCESS.getCode().equals(baseResponse.getCode())) {
            return rp.setErrorMsg(baseResponse.getCode(), baseResponse.getMsg());
        }
        System.out.println("=========================CONTROLLER-BEGIN=========================");
        System.out.println("baseResponse.getResult() = " + baseResponse.getResult());
        System.out.println(JSONObject.toJSONString(baseResponse.getResult(), JSONWriter.Feature.PrettyFormat));
        System.out.println("=========================CONTROLLER-END=========================");

        final PageResponse<List<OrderDTO>> pageResponse = new PageResponse<>();
        pageResponse.setTotal(baseResponse.getTotal());
        pageResponse.setData(baseResponse.getResult());
        return rp.setData(pageResponse);
    }

}
