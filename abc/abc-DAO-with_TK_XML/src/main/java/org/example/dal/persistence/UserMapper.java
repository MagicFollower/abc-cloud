package org.example.dal.persistence;

import org.apache.ibatis.annotations.Param;
import org.example.dal.entity.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    List<User> queryByIdsWithXML(@Param("ids") List<Long> ids);
}