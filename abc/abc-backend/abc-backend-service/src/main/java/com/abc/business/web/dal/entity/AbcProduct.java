package com.abc.business.web.dal.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表名：abc_product
 * 表注释：商品信息
*/
@Data
@NoArgsConstructor
@Table(name = "`abc_product`")
public class AbcProduct {
    @Id
    @Column(name = "`id`")
    private Long id;

    /**
     * 类型id
     */
    @Column(name = "`type`")
    private Long type;

    /**
     * 商品编码
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 商品名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 实际价格
     */
    @Column(name = "`act_price`")
    private BigDecimal actPrice;

    /**
     * 销售价格
     */
    @Column(name = "`sale_price`")
    private BigDecimal salePrice;

    /**
     * 备注
     */
    @Column(name = "`memo`")
    private String memo;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 创建用户（账号）
     */
    @Column(name = "`create_user`")
    private String createUser;

    /**
     * 更新时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;

    /**
     * 更新用户（账号）
     */
    @Column(name = "`update_user`")
    private String updateUser;
}