package com.abc.business.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品类型DTO
 *
 * @Description 商品类型DTO
 * @Author -
 * @Date 2023/8/20 23:05
 * @Version 1.0
 */
@Data
public class ProductTypeDTO implements Serializable {
    private Long id;
    private String code;
    private String name;
    private String memo;
    private Date createTime;
    private String createUser;
    private Date updateTime;
    private String updateUser;
}
