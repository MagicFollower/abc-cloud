package com.abc.system.dao.tkmybatis.dialect;

import com.abc.system.dao.tkmybatis.dialect.mysql.insert.MySqlBatchInsertMapper;
import com.abc.system.dao.tkmybatis.dialect.mysql.update.MySqlBatchUpdateMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface AbcTkMapper<T> extends Mapper<T>, MySqlMapper<T>,
        MySqlBatchInsertMapper<T>, MySqlBatchUpdateMapper<T> {
}
