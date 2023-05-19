package com.abc.system.lock.service.impl;

import com.abc.system.lock.service.DistributedLockService;
import com.abc.system.lock.util.DistributedLockUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * DistributedLockServiceImpl
 *
 * @Description DistributedLockServiceImpl 分布式锁服务接口实现
 * @Author Trivis
 * @Date 2023/5/19 14:54
 * @Version 1.0
 */
public class DistributedLockServiceImpl implements DistributedLockService {

    /**
     * <pre>
     * 移除指定警告：“必须在有效 Spring Bean 中定义自动装配成员(@Component|@Service|…)”
     *   1.原因：该类已在com.abc.system.lock.config.DistributedLockAutoConfiguration中通过@Bean方式完成注入。
     * </pre>
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public RLock lock(String key) {
        if (StringUtils.isEmpty(key)) return null;
        RLock lock = redissonClient.getLock(DistributedLockUtils.enrichLockKey(key));
        lock.lock();
        return lock;
    }

    @Override
    public RLock lock(String key, long leaseTimeout) {
        return lock(key, leaseTimeout, TimeUnit.SECONDS);
    }

    @Override
    public RLock lock(String key, long leaseTimeout, TimeUnit timeUnit) {
        if (StringUtils.isEmpty(key)) return null;
        RLock lock = redissonClient.getLock(DistributedLockUtils.enrichLockKey(key));
        lock.lock(leaseTimeout, timeUnit);
        return lock;
    }

    @Override
    public boolean tryLock(String key, long waitTimeout, long leaseTimeout) {
        return tryLock(key, waitTimeout, leaseTimeout, TimeUnit.SECONDS);
    }

    @Override
    public boolean tryLock(String key, long waitTimeout, long leaseTimeout, TimeUnit timeUnit) {
        if (StringUtils.isEmpty(key)) return false;
        RLock lock = redissonClient.getLock(DistributedLockUtils.enrichLockKey(key));
        try {
            return lock.tryLock(waitTimeout, leaseTimeout, timeUnit);
        } catch (Exception ignored) {
        }
        return false;
    }

    @Override
    public void unlock(String key) {
        if (StringUtils.isEmpty(key)) return;
        redissonClient.getLock(DistributedLockUtils.enrichLockKey(key)).unlock();
    }

    @Override
    public void unlock(RLock lock) {
        if (lock == null) return;
        lock.unlock();
    }
}
