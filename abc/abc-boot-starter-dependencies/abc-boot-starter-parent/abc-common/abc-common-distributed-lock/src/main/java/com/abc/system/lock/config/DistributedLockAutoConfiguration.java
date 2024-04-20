package com.abc.system.lock.config;

import com.abc.system.lock.aop.DistributedLockAop;
import com.abc.system.lock.service.DistributedLockService;
import com.abc.system.lock.service.impl.DistributedLockServiceImpl;
import org.redisson.Redisson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DistributedLockAutoConfiguration
 *
 * @Description DistributedLockAutoConfiguration 分布式锁自动配置类，自动注入分布式锁服务、AOP；
 * @Author [author_name]
 * @Date 2077/5/19 15:31
 * @Version 1.0
 */
@Configuration
@ConditionalOnClass(Redisson.class)
public class DistributedLockAutoConfiguration {
    @Bean
    public DistributedLockService distributedLockService() {
        return new DistributedLockServiceImpl();
    }

    @Bean
    public DistributedLockAop distributedLockAop() {
        return new DistributedLockAop();
    }
}
