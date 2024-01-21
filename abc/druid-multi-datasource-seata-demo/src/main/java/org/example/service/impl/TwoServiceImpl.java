package org.example.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dal.one.entity.OneAbcProduct;
import org.example.dal.one.persistence.OneAbcProductMapper;
import org.example.dal.two.entity.TwoAbcProduct;
import org.example.dal.two.persistence.TwoAbcProductMapper;
import org.example.service.IOneService;
import org.example.service.ITwoService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwoServiceImpl implements ITwoService {

    private final IOneService oneService;
    private final TwoAbcProductMapper twoAbcProductMapper;

    /**
     * <pre>
     * 1.这里的{@code @DS("two")} 会限制当前方法内部Mapper的数据源
     * 2.对于跨数据源，需要使用Service调用{@code oneService.oneAdd();}
     * ↓
     * 3.一个Service中，如果针对同一个数据源进行操作，推荐直接注入Mapper，Service注入意味着是【跨DataSource调用】或【RPC/HTTP调用】
     * 4.多数据源时，不推荐使用Primary指定主数据源（主从数据库除外），手动指定@DS注解可以避免使用错误的数据源
     * </pre>
     */
    @Override
    @DS("two")
    @GlobalTransactional
    public void oneTwoAdd() {
        oneService.oneAdd();
        twoAbcProductMapper.insert(new TwoAbcProduct());
    }
}
