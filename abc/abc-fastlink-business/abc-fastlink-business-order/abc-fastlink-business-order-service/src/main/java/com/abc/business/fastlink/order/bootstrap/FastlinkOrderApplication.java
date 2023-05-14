package com.abc.business.fastlink.order.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * FastlinkOrderApplication
 *
 * @Description FastlinkOrderApplication 详细介绍
 * @Author Trivis
 * @Date 2023/5/14 21:11
 * @Version 1.0
 */
@ComponentScan(basePackages = {"com.abc.business.fastlink"})
@SpringBootApplication
public class FastlinkOrderApplication {
    public static void main(String[] args) {
        // 指定Dubbo日志适配器
        System.setProperty("dubbo.application.logger", "slf4j");
        SpringApplication.run(FastlinkOrderApplication.class, args);
    }
}
