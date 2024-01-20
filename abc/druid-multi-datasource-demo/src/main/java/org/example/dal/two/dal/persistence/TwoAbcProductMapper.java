package org.example.dal.two.dal.persistence;


import org.example.dal.two.dal.entity.TwoAbcProduct;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TwoAbcProductMapper extends Mapper<TwoAbcProduct> {

    List<Map> test();
}