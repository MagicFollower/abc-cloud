package com.abc.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证授权中心
 * - 指定前缀，扫描所有包（包括：abc-api-system）
 */
@EnableFeignClients("com.abc")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AbcAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AbcAuthApplication.class, args);
        System.out.println("认证授权中心启动成功");
    }
}
