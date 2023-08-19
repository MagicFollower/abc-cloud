package com.abc.business.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * AbcBackendBootstrap
 *
 * @Description AbcBackendBootstrap
 * @Author -
 * @Date 2023/8/18 22:36
 * @Version 1.0
 */
@ComponentScan(basePackages = {"com.abc.business"})
@SpringBootApplication
public class AbcBackendBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(AbcBackendBootstrap.class, args);
    }
}
