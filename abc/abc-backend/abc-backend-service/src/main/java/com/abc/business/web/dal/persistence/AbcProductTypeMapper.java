package com.abc.business.web.dal.persistence;

import com.abc.business.web.dal.entity.AbcProductType;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface AbcProductTypeMapper extends Mapper<AbcProductType> {
}