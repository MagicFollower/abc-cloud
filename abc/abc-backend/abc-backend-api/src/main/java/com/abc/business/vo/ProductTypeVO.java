package com.abc.business.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品类型VO
 *
 * @Description 商品类型VO
 * @Author -
 * @Date 2023/8/20 23:03
 * @Version 1.0
 */
@Data
public class ProductTypeVO implements Serializable {
    /* === add === */
    private String code;
    private String name;
    private String memo;
}
