package com.abc.business.fastlink.goods.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 *
 * @Description 启动类
 * @Author Trivis
 * @Date 2023/5/13 22:39
 * @Version 1.0
 */
@ComponentScan(basePackages = {"com.abc.business.fastlink", "com.abc.system.common"})
@SpringBootApplication
public class FastlinkGoodsApplication {
    public static void main(String[] args) {
        // 指定Dubbo日志适配器
        System.setProperty("dubbo.application.logger", "slf4j");
        SpringApplication.run(FastlinkGoodsApplication.class, args);
    }
}
