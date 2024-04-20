package com.abc.business.web.dal.entity;

import com.abc.system.dao.base.AbcBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 表名：abc_product_type
 * 表注释：商品类型
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "`abc_product_type`")
public class AbcProductType extends AbcBaseEntity {
    /**
     * 商品类型编码
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 商品类型名称
     */
    @Column(name = "`name`")
    private String name;
}
