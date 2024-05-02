package com.abc.system.dao.tkmybatis.dialect;

import com.abc.system.dao.tkmybatis.dialect.mysql.insert.MySqlBatchInsertMapper;
import com.abc.system.dao.tkmybatis.dialect.mysql.update.MySqlBatchUpdateMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * AbcTkMapper
 * <pre>
 * 1.Mapper
 * 2.MySqlMapper
 *   2.1 insertUseGeneratedKeys    -> 强制使用数据库主键策略（回填）
 *   2.2 insertList                -> 强制使用数据库主键策略（回填）
 * </pre>
 * @param <T>
 */
public interface AbcTkMapper<T> extends Mapper<T>, MySqlMapper<T>,
        MySqlBatchInsertMapper<T>, MySqlBatchUpdateMapper<T> {
}
