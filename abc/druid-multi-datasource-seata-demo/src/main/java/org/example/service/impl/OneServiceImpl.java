package org.example.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dal.one.entity.OneAbcProduct;
import org.example.dal.one.persistence.OneAbcProductMapper;
import org.example.service.IOneService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneServiceImpl implements IOneService {

    private final OneAbcProductMapper oneAbcProductMapper;

    @Override
    @DS("one")
    public void oneAdd() {
        oneAbcProductMapper.insert(new OneAbcProduct());
    }
}
