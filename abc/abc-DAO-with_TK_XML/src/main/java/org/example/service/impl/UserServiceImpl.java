package org.example.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.example.dal.entity.User;
import org.example.dal.persistence.UserMapper;
import org.example.service.IUserService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;

    public List<User> queryByIds(List<Long> ids) {
        List<User> userList = new ArrayList<>(0);
        if (CollectionUtils.isNotEmpty(ids)) {
            Example userExample = new Example(User.class)
                    .selectProperties(User.Fields.id, User.Fields.name, User.Fields.age);
            Example.Criteria criteria = userExample.createCriteria().andIn(User.Fields.id, ids);
            userList = userMapper.selectByExample(userExample);
            System.out.println("---1");
            System.out.println(JSONObject.toJSONString(userList, JSONWriter.Feature.PrettyFormat));

            userExample = new Example(User.class);
            userExample.and(criteria);
            userList = userMapper.selectByExample(userExample);
            System.out.println("---2");
            System.out.println(JSONObject.toJSONString(userList, JSONWriter.Feature.PrettyFormat));
        }

        return userList;
    }

    @Override
    public List<User> queryByIdsWithXML(List<Long> ids) {
        return userMapper.queryByIdsWithXML(ids);
    }
}
