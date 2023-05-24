package com.abc.system.dao.tkmybatis.dialect.mysql.update;

import org.apache.ibatis.mapping.MappedStatement;

public interface MySqlIBatchExampleProvider {
    String batchUpdateByPrimaryKeySelective(MappedStatement var1);

    String batchUpdateByPrimaryKey(MappedStatement var1);
}
