package com.abc.system.dao.tkmybatis.dialect.mysql.update;

import org.apache.ibatis.mapping.MappedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.provider.ExampleProvider;

import java.util.Set;

public class MySqlBatchExampleProvider extends ExampleProvider implements MySqlIBatchExampleProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlBatchExampleProvider.class);

    public MySqlBatchExampleProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String batchUpdateByPrimaryKeySelective(MappedStatement mappedStatement) {
        LOGGER.info(">>>>>>>> batch selective update build sql|start <<<<<<<<");
        Class<?> entityClass = this.getEntityClass(mappedStatement);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(SqlHelper.updateTable(entityClass, this.tableName(entityClass)));
        sqlBuilder.append("<trim prefix=\"set\" suffixOverrides=\",\">");
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        if (CollectionUtils.isEmpty(columns)) {
            LOGGER.error(">>>>>>>> batch selective update|failed|columns empty|table: {} <<<<<<<<", this.tableName(entityClass));
            throw new IllegalArgumentException("columns empty");
        } else {
            columns.stream().forEach((entityColumn) -> {
                if (!entityColumn.isId() && entityColumn.isUpdatable()) {
                    sqlBuilder.append("  <trim prefix=\"" + entityColumn.getColumn() + " =case\" suffix=\"end,\">");
                    sqlBuilder.append("    <foreach collection=\"list\" item=\"item\" index=\"index\">");
                    sqlBuilder.append("      <if test=\"item." + entityColumn.getEntityField().getName() + "!=null\">");
                    sqlBuilder.append("         when id=#{item.id} then #{item." + entityColumn.getEntityField().getName() + "}");
                    sqlBuilder.append("      </if>");
                    sqlBuilder.append("    </foreach>");
                    sqlBuilder.append("  </trim>");
                }

            });
            sqlBuilder.append("</trim>");
            this.processWhere(sqlBuilder);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(">>>>>>>> batch selective update|start|sqlBuilder:{} <<<<<<<<", sqlBuilder.toString());
            }

            LOGGER.info(">>>>>>>> batch selective update build sql|end <<<<<<<<");
            return sqlBuilder.toString();
        }
    }

    public String batchUpdateByPrimaryKey(MappedStatement mappedStatement) {
        LOGGER.info(">>>>>>>> batch update build sql|start <<<<<<<<");
        Class<?> entityClass = this.getEntityClass(mappedStatement);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(SqlHelper.updateTable(entityClass, this.tableName(entityClass)));
        sqlBuilder.append("<trim prefix=\"set\" suffixOverrides=\",\">");
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        if (CollectionUtils.isEmpty(columns)) {
            LOGGER.error(">>>>>>>> batch update|failed|columns empty|table: {} <<<<<<<<", this.tableName(entityClass));
            throw new IllegalArgumentException("columns empty");
        } else {
            columns.stream().forEach((entityColumn) -> {
                if (!entityColumn.isId() && entityColumn.isUpdatable()) {
                    sqlBuilder.append("  <trim prefix=\"" + entityColumn.getColumn() + " =case\" suffix=\"end,\">");
                    sqlBuilder.append("    <foreach collection=\"list\" item=\"item\" index=\"index\">");
                    sqlBuilder.append("         when id=#{item.id} then #{item." + entityColumn.getEntityField().getName() + "}");
                    sqlBuilder.append("    </foreach>");
                    sqlBuilder.append("  </trim>");
                }

            });
            sqlBuilder.append("</trim>");
            this.processWhere(sqlBuilder);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(">>>>>>>> batch update|start|sqlBuilder:{} <<<<<<<<", sqlBuilder.toString());
            }

            LOGGER.info(">>>>>>>> batch update build sql|end <<<<<<<<");
            return sqlBuilder.toString();
        }
    }

    private void processWhere(StringBuilder sqlBuilder) {
        sqlBuilder.append(" WHERE ");
        sqlBuilder.append(" id IN ");
        sqlBuilder.append("<trim prefix=\"(\" suffix=\")\">");
        sqlBuilder.append("<foreach collection=\"list\" separator=\", \" item=\"item\" index=\"index\" >");
        sqlBuilder.append("#{item.id}");
        sqlBuilder.append("</foreach>");
        sqlBuilder.append("</trim>");
    }
}
