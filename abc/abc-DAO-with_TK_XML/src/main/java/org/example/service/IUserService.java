package org.example.service;

import org.example.dal.entity.User;

import java.util.List;

public interface IUserService {
    List<User> queryByIds(List<Long> ids);

    List<User> queryByIdsWithXML(List<Long> ids);
}
