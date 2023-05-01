package com.abc.system.dao.tkmybatis.idgenerator;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import tk.mybatis.mapper.genid.GenId;

/**
 * AbcTkGlobalIdGen（雪花算法）
 *
 * @Description AbcTkGlobalIdGen TkID自动生成器，使用MybatisPlus的雪花算法生成器
 * @Author Trivis
 * @Date 2023/5/1 18:33
 * @Version 1.0
 */
public class AbcTkGlobalIdGen implements GenId<Long> {
    @Override
    public Long genId(String table, String column) {
        // 使用Mybatis-plus的SnowFlakeID生成器
        // 你可以在此替换自定义的SnowFlakeID生成器, 可参考doc目录下的示例封装
        return IdWorker.getId();
    }
}
