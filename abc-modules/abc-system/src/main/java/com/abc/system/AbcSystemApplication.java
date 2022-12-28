package com.abc.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 系统模块
 *
 * @author abc
 */
@SpringBootApplication
@MapperScan("com.abc.**.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class AbcSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(AbcSystemApplication.class, args);
        System.out.println("系统模块启动成功");

    }
}
