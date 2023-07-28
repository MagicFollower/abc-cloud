package com.abc.business.fastlink.portal.controller.order;

import com.abc.business.fastlink.order.api.IDemoOrderService;
import com.abc.business.fastlink.order.dto.OrderDTO;
import com.abc.business.fastlink.portal.base.BaseUrl;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void demo01() {
        OrderDTO orderDTO = iDemoOrderService.demo01();
        System.out.println("=========================CONTROLLER-BEGIN=========================");
        System.out.println("orderDTO = " + orderDTO);
        System.out.println(JSONObject.toJSONString(orderDTO, JSONWriter.Feature.PrettyFormat));
        System.out.println("=========================CONTROLLER-END=========================");
    }

}
