package org.example.dal.persistence;

import com.abc.system.dao.mybatisplus.mapper.AbcMpMapper;
import com.abc.system.dao.tkmybatis.dialect.AbcTkMapper;
import org.example.dal.entity.User;

public interface UserMapper extends AbcTkMapper<User>, AbcMpMapper<User> {
}
