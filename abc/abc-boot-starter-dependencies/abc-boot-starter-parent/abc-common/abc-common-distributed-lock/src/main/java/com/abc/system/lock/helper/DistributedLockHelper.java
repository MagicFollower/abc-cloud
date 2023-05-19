package com.abc.system.lock.helper;

import com.abc.system.common.helper.SpringHelper;
import com.abc.system.lock.service.DistributedLockService;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * DistributedLockHelper
 *
 * @Description
 * <pre>
 * 这是一个分布式锁帮助器，🤔️什么是帮助器？和Utils工具包有和差别？
 *   1.这是本项目中的一种开发约定，Helper是对Utils的抽象，Helper是工具中使用到SpringIOC中的容器服务的Utils;
 *   2.相对Utils，你可以将Helper理解为与SpringIOC容器深度关联的Utils。
 * </pre>
 * @Author Trivis
 * @Date 2023/5/19 15:01
 * @Version 1.0
 */
public class DistributedLockHelper {

    private final static DistributedLockService distributedLockService = SpringHelper
            .getBean(DistributedLockService.class);

    public static RLock lock(String key, long leaseTimeout, TimeUnit timeUnit) {
        return distributedLockService.lock(key, leaseTimeout, timeUnit);
    }

    public static void unlock(RLock lock) {
        distributedLockService.unlock(lock);
    }
}
