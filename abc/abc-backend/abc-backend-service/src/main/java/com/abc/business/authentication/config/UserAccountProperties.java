package com.abc.business.authentication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 用户账号信息配置类
 *
 * @Description 用户账号信息配置类
 * @Author -
 * @Date 2077/8/19 19:27
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "abc.auth")
public class UserAccountProperties {

    /**
     * 用户列表
     */
    private List<UserAccountItem> users;

    @Data
    public static final class UserAccountItem {
        /**
         * 用户名
         */
        private String username;
        /**
         * 密码(明文)
         */
        private String password;
    }
}
