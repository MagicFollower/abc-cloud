package com.abc.business.web.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * -
 *
 * @Description -
 * @Author -
 * @Date 2023/9/7 23:12
 * @Version 1.0
 */
@SpringBootConfiguration
public class DBConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setConnectionErrorRetryAttempts(0); // 失败后重连的次数，关闭重试
        druidDataSource.setBreakAfterAcquireFailure(true); // 请求失败之后中断
        druidDataSource.setMaxWait(5000); // 获取连接超时时间（ms）
        return druidDataSource;
    }
}
