package org.example.dal.entity;

import com.abc.system.dao.tkmybatis.idgenerator.AbcTkGlobalIdGen;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 表名：user
 */
@Data
@FieldNameConstants
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @Column(
            name = "id"
    )
    @KeySql(
            genId = AbcTkGlobalIdGen.class
    )
    private Long id;

    /**
     * 姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 年龄
     */
    @Column(name = "age")
    private Integer age;

    /**
     * 性别（通用选项取值）
     */
    @Column(name = "sex")
    private Byte sex;

    /**
     * 是否停用(0否1是)
     */
    @Column(name = "is_stop")
    private Boolean isStop;
}