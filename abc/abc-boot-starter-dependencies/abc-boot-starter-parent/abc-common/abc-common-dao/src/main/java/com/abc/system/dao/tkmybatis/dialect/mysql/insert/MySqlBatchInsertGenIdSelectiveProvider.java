package com.abc.system.dao.tkmybatis.dialect.mysql.insert;

import org.apache.ibatis.mapping.MappedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.*;

import java.util.Iterator;
import java.util.Set;

public class MySqlBatchInsertGenIdSelectiveProvider extends MapperTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlBatchInsertGenIdSelectiveProvider.class);

    public MySqlBatchInsertGenIdSelectiveProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 这里将拼接并生成下方格式的MyBatis配置 ↓
     * <pre>
     * {@code
     *         <bind name="listNotEmptyCheck"
     *               value="@tk.mybatis.mapper.util.OGNL@notEmptyCollectionCheck(list, 'org.example.dal.persistence.UserMapper.batchInsertGenIdSelective 方法参数为空')"/>
     *                 INSERT INTO user
     *         <foreach collection="list" item="columnRecord" separator="," index="index">
     *         <if test="index == 0">
     *             <trim prefix="(" suffix=")" suffixOverrides=",">
     *                 <if test="columnRecord.id != null">id,</if>
     *                 <if test="columnRecord.name != null">name,</if>
     *                 <if test="columnRecord.age != null">age,</if>
     *                 <if test="columnRecord.sex != null">sex,</if>
     *                 <if test="columnRecord.isStop != null">is_stop,</if>
     *             </trim>
     *         </if>
     *         </foreach>
     *                 VALUES
     *         <foreach collection="list" item="it" separator=",">
     *         <trim prefix="(" suffix=")" suffixOverrides=",">
     *             <if test="it.id != null">#{it.id},</if>
     *             <if test="it.name != null">#{it.name},</if>
     *             <if test="it.age != null">#{it.age},</if>
     *             <if test="it.sex != null">#{it.sex},</if>
     *             <if test="it.isStop != null">#{it.isStop},</if>
     *         </trim>
     *         </foreach>
     * }
     * </pre>
     * @param ms MappedStatement实例
     * @return mybatisXML表示的SQL字符串（String）
     */
    public String batchInsertGenIdSelective(MappedStatement ms) {
        LOGGER.info(">>>>>>>> batch insert selective build sql|start <<<<<<<<");
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder batchSql = new StringBuilder();
        batchSql.append("<bind name=\"listNotEmptyCheck\" value=\"@tk.mybatis.mapper.util.OGNL@notEmptyCollectionCheck(list, '" + ms.getId() + " 方法参数为空')\"/>");
        batchSql.append(SqlHelper.insertIntoTable(entityClass, this.tableName(entityClass), "list[0]"));
        batchSql.append("<foreach collection=\"list\" item=\"columnRecord\" separator=\",\" index=\"index\">");
        batchSql.append("<if test=\"index == 0\">");
        batchSql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Iterator var5 = columnList.iterator();

        EntityColumn column;
        while (var5.hasNext()) {
            column = (EntityColumn) var5.next();
            if (column.getGenIdClass() != null) {
                batchSql.append("<bind name=\"").append(column.getColumn()).append("GenIdBind\" value=\"@tk.mybatis.mapper.genid.GenIdUtil@genId(");
                batchSql.append("columnRecord").append(", '").append(column.getProperty()).append("'");
                batchSql.append(", @").append(column.getGenIdClass().getCanonicalName()).append("@class");
                batchSql.append(", '").append(this.tableName(entityClass)).append("'");
                batchSql.append(", '").append(column.getColumn()).append("')");
                batchSql.append("\"/>");
            }

            if (column.isInsertable()) {
                batchSql.append(SqlHelper.getIfNotNull("columnRecord", column, column.getColumn() + ",", this.isNotEmpty()));
            }
        }

        batchSql.append("</trim>");
        batchSql.append("</if>");
        batchSql.append("</foreach>");
        batchSql.append(" VALUES ");
        batchSql.append("<foreach collection=\"list\" item=\"it\" separator=\",\" >");
        batchSql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        var5 = columnList.iterator();

        while (var5.hasNext()) {
            column = (EntityColumn) var5.next();
            if (column.getGenIdClass() != null) {
                batchSql.append("<bind name=\"").append(column.getColumn()).append("GenIdBind\" value=\"@tk.mybatis.mapper.genid.GenIdUtil@genId(");
                batchSql.append("it").append(", '").append(column.getProperty()).append("'");
                batchSql.append(", @").append(column.getGenIdClass().getCanonicalName()).append("@class");
                batchSql.append(", '").append(this.tableName(entityClass)).append("'");
                batchSql.append(", '").append(column.getColumn()).append("')");
                batchSql.append("\"/>");
            }

            if (column.isInsertable()) {
                batchSql.append(SqlHelper.getIfNotNull("it", column, column.getColumnHolder("it") + ",", this.isNotEmpty()));
            }
        }

        batchSql.append("</trim>");
        batchSql.append("</foreach>");
        LOGGER.info(">>>>>>>> batch insert selective build sql|end <<<<<<<<");
        return batchSql.toString();
    }

    private void processKey(StringBuilder sql, Class<?> entityClass, MappedStatement ms, Set<EntityColumn> columnList) {
        Boolean hasIdentityKey = false;
        Iterator var6 = columnList.iterator();

        EntityColumn column;
        label34:
        do {
            while (true) {
                while (var6.hasNext()) {
                    column = (EntityColumn) var6.next();
                    if (column.isIdentity()) {
                        sql.append(SqlHelper.getBindCache(column));
                        if (hasIdentityKey) {
                            continue label34;
                        }

                        SelectKeyHelper.newSelectKeyMappedStatement(ms, column, entityClass, this.isBEFORE(), this.getIDENTITY(column));
                        hasIdentityKey = true;
                    } else if (column.getGenIdClass() != null) {
                        sql.append("<bind name=\"").append(column.getColumn()).append("GenIdBind\" value=\"@tk.mybatis.mapper.genid.GenIdUtil@genId(");
                        sql.append("_parameter").append(", '").append(column.getProperty()).append("'");
                        sql.append(", @").append(column.getGenIdClass().getCanonicalName()).append("@class");
                        sql.append(", '").append(this.tableName(entityClass)).append("'");
                        sql.append(", '").append(column.getColumn()).append("')");
                        sql.append("\"/>");
                    }
                }

                return;
            }
        } while (column.getGenerator() != null && "JDBC".equals(column.getGenerator()));

        throw new MapperException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
    }
}
