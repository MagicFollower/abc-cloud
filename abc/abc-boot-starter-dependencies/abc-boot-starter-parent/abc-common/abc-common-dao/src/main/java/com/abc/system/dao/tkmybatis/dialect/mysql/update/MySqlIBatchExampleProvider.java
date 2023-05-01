package com.abc.system.dao.tkmybatis.dialect.mysql.update;

import org.apache.ibatis.mapping.MappedStatement;

public interface MySqlIBatchExampleProvider {
    String updateBatchByPrimaryKeySelective(MappedStatement var1);

    String updateBatchByPrimaryKey(MappedStatement var1);
}
