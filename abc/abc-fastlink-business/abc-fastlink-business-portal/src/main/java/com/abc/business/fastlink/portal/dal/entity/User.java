package com.abc.business.fastlink.portal.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User
 *
 * @Description User
 * @Author Rake
 * @Date 2023/7/14 20:04
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String username;
    private int age;
}
