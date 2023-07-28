package com.abc.business.fastlink.order.converter;

import com.abc.business.fastlink.order.dal.entity.Order;
import com.abc.business.fastlink.order.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * OrderConverter
 *
 * @Description OrderConverter
 * @Author Rake
 * @Date 2023/7/28 23:08
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface OrderConverter {

    @Mappings({})
    OrderDTO orderEntity2OrderDTO(Order entity);

    List<OrderDTO> orderEntity2OrderDTO(List<Order> entities);
}
