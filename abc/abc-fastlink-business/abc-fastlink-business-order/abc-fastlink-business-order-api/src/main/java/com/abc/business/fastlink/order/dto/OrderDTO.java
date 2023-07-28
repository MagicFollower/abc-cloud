package com.abc.business.fastlink.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * OrderDTO
 *
 * @Description OrderDTO
 * @Author Rake
 * @Date 2023/7/28 23:04
 * @Version 1.0
 */
@Data
public class OrderDTO implements Serializable {
    private String id;

    private String name;

    private Integer age;

    @JsonIgnore
    private String memo;
}
