package org.example.controller;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.example.dal.entity.User;
import org.example.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/queryByIds")
    public String queryByIds() {
        List<Long> ids = Lists.newArrayList(1L, 2L, 3L, 11L, 12L, 13L);
        List<User> userList = userService.queryByIds(ids);
        return JSONObject.toJSONString(userList, JSONWriter.Feature.PrettyFormat);
    }

    @PostMapping("/queryByIdsWithXML")
    public String queryByIdsWithXML() {
        // List<Long> ids = Lists.newArrayList(1L, 2L, 3L, 11L, 12L, 13L);
        // List<User> userList = userService.queryByIdsWithXML(null);
        List<User> userList = userService.queryByIdsWithXML(new ArrayList<>(0));
        return JSONObject.toJSONString(userList, JSONWriter.Feature.PrettyFormat);
    }
}
