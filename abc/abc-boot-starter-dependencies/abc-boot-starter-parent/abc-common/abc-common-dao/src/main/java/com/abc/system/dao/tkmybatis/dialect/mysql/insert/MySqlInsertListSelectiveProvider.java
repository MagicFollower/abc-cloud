package com.abc.system.dao.tkmybatis.dialect.mysql.insert;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Iterator;
import java.util.Set;

@RegisterMapper
public class MySqlInsertListSelectiveProvider extends MapperTemplate {
    public MySqlInsertListSelectiveProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * selective模式批量插入
     *
     * @param ms MappedStatement实例
     */
    public String insertListSelective(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        // 开始拼sql
        StringBuilder batchSql = new StringBuilder();
        batchSql.append("<bind name=\"listNotEmptyCheck\" value=\"@tk.mybatis.mapper.util.OGNL@notEmptyCollectionCheck(list, '").append(ms.getId()).append(" 方法参数为空')\"/>");
        batchSql.append(SqlHelper.insertIntoTable(entityClass, this.tableName(entityClass), "list[0]"));
        batchSql.append("<foreach collection=\"list\" item=\"columnRecord\" separator=\",\" index=\"index\">");
        batchSql.append("<if test=\"index == 0\">");
        batchSql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Iterator<EntityColumn> columnIterator = columnList.iterator();

        EntityColumn column;
        while (columnIterator.hasNext()) {
            column = columnIterator.next();
            if (column.isInsertable()) {
                batchSql.append(SqlHelper.getIfNotNull("columnRecord", column, column.getColumn() + ",", this.isNotEmpty()));
            }
        }
        batchSql.append("</trim>");
        batchSql.append("</if>");
        batchSql.append("</foreach>");

        batchSql.append(" VALUES ");
        batchSql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        batchSql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn columnX : columnList) {
            if (!columnX.isId() && columnX.isInsertable()) {
                batchSql.append(SqlHelper.getIfNotNull("record", columnX, columnX.getColumnHolder("record") + ",", this.isNotEmpty()));
            }
        }
        batchSql.append("</trim>");
        batchSql.append("</foreach>");

        // 反射把MappedStatement中的设置主键名
        EntityHelper.setKeyProperties(EntityHelper.getPKColumns(entityClass), ms);

        return batchSql.toString();
    }
}
