package com.abc.business.fastlink.order.service.impl;

import com.abc.business.fastlink.order.api.IDemoOrderService;
import com.abc.business.fastlink.order.converter.OrderConverter;
import com.abc.business.fastlink.order.dal.entity.Order;
import com.abc.business.fastlink.order.dto.OrderDTO;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * DemoOrderServiceImpl
 *
 * @Description DemoOrderServiceImpl
 * @Author Rake
 * @Date 2023/7/28 23:07
 * @Version 1.0
 */
@DubboService
@RequiredArgsConstructor
public class DemoOrderServiceImpl implements IDemoOrderService {

    private final OrderConverter orderConverter;

    @Override
    public OrderDTO demo01() {
        Order order = new Order();
        order.setId("id-0");
        order.setName("name-0");
        order.setAge(99);
        order.setMemo(RandomStringUtils.randomAlphanumeric(128));

        OrderDTO orderDTO = orderConverter.orderEntity2OrderDTO(order);
        System.out.println("orderDTO = " + orderDTO);
        System.out.println(JSONObject.toJSONString(orderDTO, JSONWriter.Feature.PrettyFormat));

        return orderDTO;
    }
}
