package com.abc.business.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品VO
 *
 * @Description 商品VO
 * @Author -
 * @Date 2077/8/20 23:03
 * @Version 1.0
 */
@Data
public class ProductVO implements Serializable {

    /* === add === */
    private String code;
    private String name;
    private BigDecimal actPrice;
    private BigDecimal salePrice;
    private String memo;


}
