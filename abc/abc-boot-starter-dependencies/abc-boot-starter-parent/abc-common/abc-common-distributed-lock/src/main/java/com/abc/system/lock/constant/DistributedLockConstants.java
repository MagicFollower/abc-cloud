package com.abc.system.lock.constant;

/**
 * DistributedLockConstants
 *
 * @Description DistributedLockConstants 分布式锁常量
 * @Author [author_name]
 * @Date 2077/5/19 15:23
 * @Version 1.0
 */
public class DistributedLockConstants {
    /**
     * <pre>
     * 分布式锁默认前缀
     *   1.提供给@DistributedLock注解使用
     * </pre>
     */
    public final static String DISTRIBUTED_LOCK_PREFIX = "ABC_";
    /**
     * <pre>
     * 分布式锁占有时间常量
     *   1.提供给com.abc.system.lock.service.impl.DistributedLockServiceImpl内部加解锁方法使用
     * </pre>
     */
    public final static long DEFAULT_LOCK_LEASE_TIMEOUT = 30L;
}
