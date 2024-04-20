package com.abc.business.fastlink.order.api;

import com.abc.business.fastlink.order.dto.OrderDTO;
import com.abc.system.common.response.BaseResponse;

import java.util.List;

/**
 * IDemoOrderService
 *
 * @Description IDemoOrderService
 * @Author [author_name]
 * @Date 2077/7/28 23:03
 * @Version 1.0
 */
public interface IDemoOrderService {

    /**
     * 测试@JsonIgnore的生效场景
     *
     * @return OrderDTO
     */
    BaseResponse<List<OrderDTO>> demo01();
}
