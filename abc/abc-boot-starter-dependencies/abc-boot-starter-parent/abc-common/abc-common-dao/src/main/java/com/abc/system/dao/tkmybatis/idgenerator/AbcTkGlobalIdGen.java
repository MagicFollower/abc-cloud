package com.abc.system.dao.tkmybatis.idgenerator;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import tk.mybatis.mapper.genid.GenId;

/**
 * AbcTkGlobalIdGen（雪花算法）
 * <pre>
 * 🤔️如何使用它？
 * 1.tk-mybatis在实体层使用javax.persistence的注解：@Table/@Id/@Column
 *   1.1 你需要将@Id放置在主键上，同时使用@KeySql注解，将实现GenId接口的ID生成类作为genId的属性传入注解，像这样⤵️
 * {@code
 *      @Id
 *      @KeySql(genId = AbcTkGlobalIdGen.class)
 *      private Long id;
 * }
 * 🤔️它什么时候生效？
 * 1.insert/insertSelective的时候会生效
 * 2.如果insert/insertSelective的实体中id已经赋值，则使用已经存在的值，当id=null时，会自动生成id并填充
 * </pre>
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
