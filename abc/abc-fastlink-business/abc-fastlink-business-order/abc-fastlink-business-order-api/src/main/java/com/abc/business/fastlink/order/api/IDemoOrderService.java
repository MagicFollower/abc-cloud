package com.abc.business.fastlink.order.api;

import com.abc.business.fastlink.order.dto.OrderDTO;

/**
 * IDemoOrderService
 *
 * @Description IDemoOrderService
 * @Author Rake
 * @Date 2023/7/28 23:03
 * @Version 1.0
 */
public interface IDemoOrderService {

    /**
     * 测试@JsonIgnore的生效场景
     *
     * @return OrderDTO
     */
    OrderDTO demo01();
}
