package com.abc.system.dao.tkmybatis.dialect;

import com.abc.system.dao.tkmybatis.dialect.mysql.insert.MySqlBatchInsertMapper;
import com.abc.system.dao.tkmybatis.dialect.mysql.update.MySqlBatchUpdateMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * AbcTkMapper
 * <pre>
 * 1.Mapper                        -> 基本CRUD（使用自定义策略or数据库自增，回填） ------ Official
 * 2.MySqlMapper
 *   2.1 insertUseGeneratedKeys    -> 强制使用数据库主键策略（回填），主键不自增时报错 ------ Official
 *   2.2 insertList                -> 强制使用数据库主键策略（回填），主键不自增时报错 ------ Official
 * 3.MySqlBatchInsertMapper
 *   3.1 insertListSelective       -> 强制使用数据库主键策略（使用数据库主键自增时不回填，主键不自增时报错）
 *   3.2 batchInsertGenId          -> 使用自定义策略or数据库自增（使用数据库主键自增时不回填，主键不自增时报错）
 *   3.3 batchInsertGenIdSelective -> 使用自定义策略or数据库自增（使用数据库主键自增时不回填，主键不自增时报错）
 * 4.MySqlBatchUpdateMapper
 *   4.1 batchUpdateByPrimaryKey
 *   4.2 batchUpdateByPrimaryKeySelective
 * </pre>
 * @param <T>
 */
public interface AbcTkMapper<T> extends Mapper<T>, MySqlMapper<T>,
        MySqlBatchInsertMapper<T>, MySqlBatchUpdateMapper<T> {
}
