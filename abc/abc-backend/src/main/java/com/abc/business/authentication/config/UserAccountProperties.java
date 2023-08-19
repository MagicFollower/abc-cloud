package com.abc.business.authentication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * UserAccountConfig
 *
 * @Description UserAccountConfig
 * @Author -
 * @Date 2023/8/19 19:27
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "auth")
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
