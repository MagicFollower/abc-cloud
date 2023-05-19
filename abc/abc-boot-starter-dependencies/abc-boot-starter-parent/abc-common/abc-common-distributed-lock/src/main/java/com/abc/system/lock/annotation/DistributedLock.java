package com.abc.system.lock.annotation;

import com.abc.system.lock.constant.DistributedLockConstants;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * DistributedLock 分布式锁注解
 *
 * @Description <pre>
 * DistributedLock 分布式锁注解，只需要轻轻放到方法上即可为该方法添加redis分布式锁😎
 *   1.key自动生成：prefix_className_methodName, 不开放手动配置参数；
 * </pre>
 * @Author Trivis
 * @Date 2023/5/19 14:19
 * @Version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * 锁超时释放时间(缺省: 30s)
     */
    long leaseTime() default DistributedLockConstants.DEFAULT_LOCK_LEASE_TIMEOUT;

    /**
     * 锁超时释放时间单元(缺省: TimeUnit.SECONDS)
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
