package com.abc.system.dao.tkmybatis.dialect.mysql.insert;

public interface MySqlBatchInsertMapper<T> extends MySqlInsertListSelectiveMapper<T>, MySqlBatchInsertGenIdMapper<T>, MySqlBatchInsertGenIdSelectiveMapper<T> {
}
