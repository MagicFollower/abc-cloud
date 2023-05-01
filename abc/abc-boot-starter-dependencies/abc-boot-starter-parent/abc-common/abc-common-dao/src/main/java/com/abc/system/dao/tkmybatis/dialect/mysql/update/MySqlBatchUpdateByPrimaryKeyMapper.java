package com.abc.system.dao.tkmybatis.dialect.mysql.update;

import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface MySqlBatchUpdateByPrimaryKeyMapper<T> {
    @UpdateProvider(
            type = MySqlBatchExampleProvider.class,
            method = "dynamicSQL"
    )
    int batchUpdateByPrimaryKey(List<? extends T> var1);
}
