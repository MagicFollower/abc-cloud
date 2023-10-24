package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.example.dal.entity.User;
import org.example.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

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
}
