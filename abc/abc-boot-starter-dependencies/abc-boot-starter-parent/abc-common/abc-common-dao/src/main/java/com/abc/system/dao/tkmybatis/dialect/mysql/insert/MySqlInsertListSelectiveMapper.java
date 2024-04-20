package com.abc.system.dao.tkmybatis.dialect.mysql.insert;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface MySqlInsertListSelectiveMapper<T> {

    /**
     * selective模式批量写入（@KeySql(genId = AbcTkGlobalIdGen.class)存在时，使用ID生成方案，忽略数据库ID自增）
     * <pre>
     * 1.官方提供的MySqlMapper存在insertList，这个API会完全使用数据库主键ID生成方案，并且没有提供selective模式，这里额外提供一个对应的selective模式API
     * </pre>
     *
     * @param recordList 记录列表
     * @return 生效列数
     */
    @InsertProvider(type = MySqlInsertListSelectiveProvider.class, method = "dynamicSQL")
    int insertListSelective(List<? extends T> recordList);
}
