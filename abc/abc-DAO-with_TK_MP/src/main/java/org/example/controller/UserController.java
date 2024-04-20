package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.dal.entity.User;
import org.example.dal.persistence.UserMapper;
import org.example.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final UserMapper userMapper;

    @PostMapping("/api01")
    public User api01() {
        LambdaQueryWrapper<User> query = Wrappers.<User>query().lambda()
                .select(User::getAge)
                .last("LIMIT 1");
        User one = userService.getOne(query);
        // User one = userService.getOne(query, false);
        System.out.println("one = " + one);
        return one;
    }


//    @Transactional
    @PostMapping("/api02-batchInsert-TK")
    public void api02() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setName("name-" + RandomStringUtils.randomAlphabetic(10));
            userList.add(user);
        }
        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setName("name-" + RandomStringUtils.randomAlphabetic(5));
            user.setAge(ThreadLocalRandom.current().nextInt(100));
            userList.add(user);
        }
        // userMapper.batchInsertGenIdSelective(userList);

        for (User x : userList) {
            // userMapper.insert(x);
            userService.save(x);
        }
    }

    @PostMapping("/api03-batchUpdate-TK")
    public void api03() {

        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setId(100L);
        user.setName("name-" + RandomStringUtils.randomAlphabetic(10));
        userList.add(user);
        User user1 = new User();
        user1.setId(100L);
        user1.setName("name-" + RandomStringUtils.randomAlphabetic(10));
        user1.setAge(99);
        userList.add(user1);
        // userMapper.batchUpdateByPrimaryKeySelective(userList);

        for (int i = 0; i < 2; i++) {
            User x = new User();
            x.setName("name-" + RandomStringUtils.randomAlphabetic(10));
            userList.add(x);
        }
        for (int i = 0; i < 3; i++) {
            User x = new User();
            x.setName("name-" + RandomStringUtils.randomAlphabetic(5));
            x.setAge(ThreadLocalRandom.current().nextInt(100));
            userList.add(x);
        }
        // userService.saveOrUpdateBatch(userList);

        userService.updateById(user);
    }
}
