package com.abc.system.lock.util;

import com.abc.system.lock.constant.DistributedLockConstants;

/**
 * DistributedLockUtils
 *
 * @Description DistributedLockUtils 详细介绍
 * @Author Trivis
 * @Date 2023/5/19 14:32
 * @Version 1.0
 */
public class DistributedLockUtils {

    /**
     * 为原有的lockKey添加前缀
     *
     * @param key 旧lockKey
     * @return 新lockKey
     */
    public static String enrichLockKey(String key) {
        return DistributedLockConstants.DISTRIBUTED_LOCK_PREFIX + key;
    }
}
