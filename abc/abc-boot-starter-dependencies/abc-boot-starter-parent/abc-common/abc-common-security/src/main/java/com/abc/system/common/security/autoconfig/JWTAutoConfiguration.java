package com.abc.system.common.security.autoconfig;

import com.abc.system.common.security.config.JWTProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWTAutoConfiguration
 *
 * @Description JWTAutoConfiguration 详细介绍
 * @Author [author_name]
 * @Date 2077/5/27 23:54
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(JWTProperties.class)
public class JWTAutoConfiguration {
}
