package org.example.service.impl;

import com.abc.system.dao.mybatisplus.service.impl.AbcMpServiceImpl;
import org.example.dal.entity.User;
import org.example.dal.persistence.UserMapper;
import org.example.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends AbcMpServiceImpl<UserMapper, User> implements IUserService {
}
