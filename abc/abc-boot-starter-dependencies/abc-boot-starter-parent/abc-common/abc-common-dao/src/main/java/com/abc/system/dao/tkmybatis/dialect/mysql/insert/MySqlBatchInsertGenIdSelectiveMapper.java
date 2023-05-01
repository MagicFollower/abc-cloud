package com.abc.system.dao.tkmybatis.dialect.mysql.insert;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface MySqlBatchInsertGenIdSelectiveMapper<T> {
    @InsertProvider(
            type = MySqlBatchInsertGenIdSelectiveProvider.class,
            method = "dynamicSQL"
    )
    int batchInsertGenIdSelective(List<? extends T> var1);
}
