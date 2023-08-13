package com.abc.business.fastlink.portal.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * FastlinkPortalApplication
 *
 * @Description FastlinkPortalApplication 详细介绍
 * @Author Trivis
 * @Date 2023/5/14 21:34
 * @Version 1.0
 */
@ComponentScan(basePackages = {"com.abc.business.fastlink"})
@SpringBootApplication
public class FastlinkPortalApplication {
    public static void main(String[] args) {
        // 指定Dubbo日志适配器
        System.setProperty("dubbo.application.logger", "slf4j");
        ApplicationContext applicationContext = SpringApplication.run(FastlinkPortalApplication.class, args);
    }
}
