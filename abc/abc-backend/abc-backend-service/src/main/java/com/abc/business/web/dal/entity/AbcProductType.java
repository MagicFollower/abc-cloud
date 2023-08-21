package com.abc.business.web.dal.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表名：abc_product_type
 * 表注释：商品类型
*/
@Data
@NoArgsConstructor
@Table(name = "`abc_product_type`")
public class AbcProductType {
    @Column(name = "`id`")
    private Long id;

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
     * 更新用户
     */
    @Column(name = "`update_user`")
    private String updateUser;
}