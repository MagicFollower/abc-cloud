package com.abc.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关启动程序
 *
 * @author abc
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AbcGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AbcGatewayApplication.class, args);
        System.out.println("网关启动成功");
    }
}
