package com.abc.system.lock.service;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * DistributedLockService
 *
 * @Description DistributedLockService 分布式锁服务接口
 * @Author [author_name]
 * @Date 2077/5/19 14:53
 * @Version 1.0
 */
public interface DistributedLockService {

    RLock lock(String key);

    RLock lock(String key, long leaseTimeout);

    RLock lock(String key, long leaseTimeout, TimeUnit timeUnit);

    boolean tryLock(String key, long waitTimeout, long leaseTimeout);

    boolean tryLock(String key, long waitTimeout, long leaseTimeout, TimeUnit timeUnit);

    void unlock(String key);

    void unlock(RLock lock);
}
