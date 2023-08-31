package com.abc.system.dao.base;

import com.abc.system.dao.tkmybatis.idgenerator.AbcTkGlobalIdGen;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体通用数据属性
 *
 * @Description 实体通用数据属性
 * @Author -
 * @Date 2023/8/31 21:39
 * @Version 1.0
 */
@Data
public class AbcBaseEntity implements Serializable {
    /**
     * 主键ID
     */
    @Id
    @Column(name = "id")
    @KeySql(genId = AbcTkGlobalIdGen.class)
    private Long id;
    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
    /**
     * 创建用户
     */
    @Column(name = "create_user")
    private String createUser;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 更新用户
     */
    @Column(name = "update_user")
    private String updateUser;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}
