package com.abc.system.dao.tkmybatis.dialect.mysql.update;

import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface MySqlBatchUpdateMapper<T> extends MySqlBatchUpdateByPrimaryKeyMapper<T>, MySqlBatchUpdateByPrimaryKeySelectiveMapper<T> {
}
