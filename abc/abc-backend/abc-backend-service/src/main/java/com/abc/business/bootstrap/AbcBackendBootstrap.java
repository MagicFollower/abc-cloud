package com.abc.business.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * AbcBackendBootstrap
 *
 * @Description AbcBackendBootstrap
 * @Author -
 * @Date 2077/8/18 22:36
 * @Version 1.0
 */
@ComponentScan(basePackages = {"com.abc.business"})
@SpringBootApplication
@MapperScan(basePackages = {"com.abc.business.web.dal.persistence"})
public class AbcBackendBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(AbcBackendBootstrap.class, args);
    }
}
