package com.abc.business.fastlink.order.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order
 *
 * @Description Order
 * @Author [author_name]
 * @Date 2077/7/28 23:04
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private String id;

    private String name;

    private Integer age;

    @JsonIgnore
    private String memo;
}
