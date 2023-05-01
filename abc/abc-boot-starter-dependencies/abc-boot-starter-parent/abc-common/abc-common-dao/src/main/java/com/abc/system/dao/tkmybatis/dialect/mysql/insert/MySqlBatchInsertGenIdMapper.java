package com.abc.system.dao.tkmybatis.dialect.mysql.insert;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface MySqlBatchInsertGenIdMapper<T>{
    @InsertProvider(
            type = MySqlBatchInsertGenIdProvider.class,
            method = "dynamicSQL"
    )
    int batchInsertGenId(List<? extends T> var1);
}
