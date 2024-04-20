package org.example.dal.two.entity;

import com.abc.system.dao.base.AbcBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 表名：abc_product
 * 表注释：商品信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@FieldNameConstants
@Table(name = "`abc_product`")
public class TwoAbcProduct extends AbcBaseEntity {
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
}
