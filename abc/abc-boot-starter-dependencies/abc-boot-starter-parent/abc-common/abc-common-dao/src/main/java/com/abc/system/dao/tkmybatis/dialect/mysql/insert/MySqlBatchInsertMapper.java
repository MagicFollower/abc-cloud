package com.abc.system.dao.tkmybatis.dialect.mysql.insert;

/**
 * <pre>
 * 这里自定义的三个API均不支持主键ID回填
 * 1.insertListSelective
 * 2.batchInsertGenId
 * 3.batchInsertGenIdSelective
 * </pre>
 *
 * @param <T>
 */
public interface MySqlBatchInsertMapper<T> extends MySqlInsertListSelectiveMapper<T>, MySqlBatchInsertGenIdMapper<T>, MySqlBatchInsertGenIdSelectiveMapper<T> {
}
