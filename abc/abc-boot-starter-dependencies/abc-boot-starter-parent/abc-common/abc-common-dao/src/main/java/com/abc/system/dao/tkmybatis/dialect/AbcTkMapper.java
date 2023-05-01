package com.abc.system.dao.tkmybatis.dialect;

import com.abc.system.dao.tkmybatis.dialect.mysql.insert.MySqlBatchInsertMapper;
import com.abc.system.dao.tkmybatis.dialect.mysql.update.MySqlBatchUpdateMapper;
import tk.mybatis.mapper.common.Mapper;

public interface AbcTkMapper<T> extends Mapper<T>,
        MySqlBatchInsertMapper<T>, MySqlBatchUpdateMapper<T> {
}