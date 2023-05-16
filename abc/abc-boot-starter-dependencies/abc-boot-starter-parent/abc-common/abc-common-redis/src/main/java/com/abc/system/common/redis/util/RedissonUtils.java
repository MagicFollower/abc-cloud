package com.abc.system.common.redis.util;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.RedisException;
import com.abc.system.common.helper.SpringHelper;
import com.abc.system.common.page.PageInfo;
import com.abc.system.common.page.PageResponse;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.RedissonAtomicLong;
import org.redisson.api.*;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RedissonUtils
 *
 * @Description RedissonUtils 详细介绍
 * @Author Trivis
 * @Date 2023/5/15 23:23
 * @Version 1.0
 */
@Slf4j
public class RedissonUtils {

    private RedissonUtils() {

    }

    //##########################################################
    //###存储数据，分页获取
    //##########################################################

    /**
     * 将集合存放到Redis中指定KEY下面
     * <pre>
     *     利用Redis中的ZSet数据结构，需要分页的数据存储ZSet中🚀
     * </pre>
     *
     * @param key        Redis Key
     * @param recordMaps 待存储的数据集合，其中，KEY为实际需要存储的数据，为String类型，如果是对象，请转换为JSON字符串，
     *                   VALUE为排序字段，便于实现分页排序
     * @param duration   过期时间间隔
     * @return true:成功 false:失败
     */
    public static boolean saveRecordToRedis(String key, Map<Object, Double> recordMaps, Duration duration) {
        if (StringUtils.isEmpty(key)) {
            log.warn(">>>>>>>>>>> save data to redis|key is empty <<<<<<<<<<<");
            return false;
        }
        if (CollectionUtils.isEmpty(recordMaps)) {
            log.warn(">>>>>>>>>>> save data to redis|recordMaps is empty <<<<<<<<<<<");
            return false;
        }

        log.info(">>>>>>>>>>> save data to redis|key={} <<<<<<<<<<<", key);
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        Instant start = Instant.now();
        RScoredSortedSet<Object> rScoredSortedSet = redissonClient.getScoredSortedSet(key);
        rScoredSortedSet.addAll(recordMaps);
        rScoredSortedSet.expire(duration);
        Instant end = Instant.now();
        log.info(">>>>>>>>>>> save data to redis|key={},recordCount={}|save times={} ms <<<<<<<<<<<",
                key, rScoredSortedSet.size(), (Duration.between(start, end).toMillis()));

        return true;
    }

    /**
     * 获取分页数据
     * <pre>
     *     利用Redis的ZREVRANGEBYSCORE、ZRANGEBYSCORE命令实现缓存分页，如：订单、优惠券等需要在缓存中分页查询的业务场景；
     *         ↪️ZREVRANGEBYSCORE 是 Redis 的一个有序集合（Sorted Set）命令，用于获取指定 score 范围内按照 score 从大到小排序的元素列表。
     *         具体用法如下：
     *         ```
     *         ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count]
     *         ```
     *         其中，key 表示有序集合的键名，max 和 min 分别表示需要获取的元素的 score 范围，[WITHSCORES] 表示是否需要返回元素的 score 值（默认不返回），[LIMIT offset count] 表示需要获取的元素的偏移量和数量。
     *         例如，如果有一个有序集合名为 myzset，其中包含了一些元素，每个元素都有一个 score 值，现在需要获取 score 在 10 到 20 之间的元素按照 score 从大到小排序的列表，可以使用以下命令：
     *         ```
     *         ZREVRANGEBYSCORE myzset 20 10
     *         ```
     *         这个命令会返回 score 在 10 到 20 之间的元素列表，按照 score 从大到小排序。
     *         ↪️需要注意的是，ZREVRANGEBYSCORE 命令返回的元素列表中包含了元素的 value 值，但是默认不包含元素的 score 值。如果需要返回元素的 score 值，可以在命令末尾添加 WITHSCORES 参数。
     * </pre>
     *
     * @param pageInfo 分页参数
     * @param key      待分页的KEY
     * @return 指定分页内的数据
     */
    public static PageResponse<List<String>> getPageInfoFromRedis(PageInfo pageInfo, String key) {
        PageResponse<List<String>> pageResponse = new PageResponse<>();
        if (StringUtils.isEmpty(key)) {
            log.warn(">>>>>>>>>>> query page data from redis|key is empty <<<<<<<<<<<");
            return pageResponse;
        }
        // 开始记录、结束记录
        int start = (pageInfo.getPageNum() - 1) * pageInfo.getPageSize();
        int end = start + pageInfo.getPageSize() - 1;
        log.info(">>>>>>>>>>> query page data from redis|key={}|start={}|end={} <<<<<<<<<<<", key, start, end);

        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        Instant startTime = Instant.now();
        RScoredSortedSet<String> rScoredSortedSet = redissonClient.getScoredSortedSet(key);
        List<String> result = (List<String>) rScoredSortedSet.valueRangeReversed(start, end);
        pageResponse = new PageResponse<>();
        pageResponse.setData(result);
        pageResponse.setTotal(rScoredSortedSet.size());
        Instant endTime = Instant.now();
        log.info(">>>>>>>>>>> query data from redis|key={}|start={}|end={}|total size={}|query times={} ms <<<<<<<<<<<",
                key, start, end, rScoredSortedSet.size(), (Duration.between(startTime, endTime).toMillis()));
        return pageResponse;
    }

    /**
     * 统计指定KEY集合中元素个数
     *
     * @param key key
     * @return 集合中元素个数
     */
    public static int getZSetCount(String key) {
        if (StringUtils.isEmpty(key)) {
            log.warn(">>>>>>>>>>> get zset count from redis|key is empty <<<<<<<<<<<");
            return 0;
        }
        log.info(">>>>>>>>>>> get zset count from redis|key={} <<<<<<<<<<<", key);
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        Instant startTime = Instant.now();
        RScoredSortedSet<List<String>> rScoredSortedSet = redissonClient.getScoredSortedSet(key);
        int recordCount = null != rScoredSortedSet ? rScoredSortedSet.size() : 0;
        Instant endTime = Instant.now();
        log.info(">>>>>>>>>>> get zset count from redis|key={},recordCount={}|save times={} ms <<<<<<<<<<<",
                key, recordCount, (Duration.between(startTime, endTime).toMillis()));
        return recordCount;
    }


    /**
     * 获取指定KEY的值
     * <p>
     * 获取string类型的值
     * </p>
     *
     * @param key key
     * @return value
     */
    public static String getValue(String key) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        // 首先获取redis中的key-value对象
        RBucket<String> bucket = redissonClient.getBucket(key);
        String value = null;
        try {
            value = bucket.get();
        } catch (Exception e) {
            log.info(">>>>>>>>>>> get value for {} exception: <<<<<<<<<<<", key, e);
        }

        return value;
    }

    /**
     * 取得指定KEY的Set集合
     *
     * @param key key
     * @return Set#String
     */
    public static Set<String> getSet(String key) {
        log.info(">>>>>>>>>>> get value to redis|set|key:{}|start <<<<<<<<<<<", key);
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        Set<String> resultSet = new HashSet<>(32);
        try {
            RSet<String> set = redissonClient.getSet(key);
            resultSet = set.readAll();
            log.info(">>>>>>>>>>> get value to redis|set|success <<<<<<<<<<<");
        } catch (Exception e) {
            log.error(">>>>>>>>>>> get value to redis|set|key:{}|exception: {}<<<<<<<<<<<", key, e.getMessage(), e);
        }
        return resultSet;
    }

    /**
     * 从Redis中删除指定KEY的值
     *
     * @param key 待删除的KEY
     * @return true:成功 false:失败
     */
    public static boolean delete(String key) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        // 首先获取redis中的key-value对象
        RBucket<String> bucket = redissonClient.getBucket(key);
        try {
            return bucket.delete();
        } catch (Exception e) {
            log.info(">>>>>>>>>>> delete key {} exception: <<<<<<<<<<<", key, e);
        }

        return false;
    }

    /**
     * 保存至redis中
     *
     * @param key      存储的键
     * @param value    存储的值
     * @param timeOut  过期时间，-1：表示未过期
     * @param timeUnit 过期时间单位
     * @return true:成功 false:失败
     */
    public static boolean setValue(String key, String value, long timeOut, TimeUnit timeUnit) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);

        try {
            // 将获取到token存入redis中
            RBucket<String> rBucket = redissonClient.getBucket(key);

            // -1表示未过期
            if (-1 == timeOut) {
                // 60分钟过期
                rBucket.set(value);
            } else {
                // 60分钟过期
                rBucket.set(value, timeOut, timeUnit);
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>> set key={},value={} exception: <<<<<<<<<<<", key, value, e);
        }

        return true;
    }

    /**
     * 保存至redis中
     * <pre>
     *     不过期
     * </pre>
     *
     * @param key   key
     * @param value value
     * @return true:成功 false:失败
     */
    public static boolean setValue(String key, String value) {
        // 60分钟过期
        return setValue(key, value, -1, null);
    }

    /**
     * 取得指定Map中的指定KEY的列表
     *
     * @param mainKey 主KEY
     * @param key     实际值对应的KEY
     * @return 列表
     */
    public static List<String> getValueFromMap(String mainKey, String key) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        // 首先获取redis中的key-value对象
        RMap<String, String> maps = redissonClient.getMap(mainKey);
        List<String> value = null;
        try {
            value = JSONObject.parseObject(maps.get(key), new TypeReference<List<String>>() {
            });
        } catch (Exception e) {
            log.error(">>>>>>>>>>> get value from Map {} exception: <<<<<<<<<<<", key, e);
        }

        return value;
    }

    /**
     * 将一个Map存放到redis中
     *
     * @param mainKey  redis中的KEY
     * @param values   map集合
     * @param timeOut  过期时间
     * @param timeUnit 过期时间单位
     * @return true:成功 false:失败
     */
    public static boolean setValueForMap(String mainKey, Map<String, List<String>> values, long timeOut,
                                         TimeUnit timeUnit) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RMap<String, String> maps = redissonClient.getMap(mainKey);
        try {
            long startTime = System.currentTimeMillis();
            values.forEach((k, v) -> {
                maps.put(k, JSONObject.toJSONString(v));
            });
            // 设置过期时间
            maps.expire(timeOut, timeUnit);
            log.info(">>>>>>>>>>> set value for Map|mainKey:{}|query times={} ms |success <<<<<<<<<<<", mainKey
                    , (System.currentTimeMillis() - startTime));
            return true;
        } catch (Exception e) {
            log.error(">>>>>>>>>>> set value for Map {} exception: <<<<<<<<<<<", mainKey, e);
        }

        return false;
    }

    /**
     * 将List存放到指定KEY的REDIS中
     * <pre>
     *     mainKey:{
     *         {
     *             key:values
     *         }
     *     }
     * </pre>
     *
     * @param mainKey  redis中的KEY
     * @param key      集合中的KEY
     * @param values   对应的值
     * @param timeOut  过期时间
     * @param timeUnit 过期时间单位
     * @return true:成功 false:失败
     */
    public static boolean setValueForMap(String mainKey, String key, List<String> values, long timeOut,
                                         TimeUnit timeUnit) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RMap<String, String> maps = redissonClient.getMap(mainKey);
        try {
            maps.put(key, JSONObject.toJSONString(values));
            // 设置过期时间
            maps.expire(timeOut, timeUnit);
            return true;
        } catch (Exception e) {
            log.error(">>>>>>>>>>> get value for {} exception: <<<<<<<<<<<", key, e);
        }

        return false;
    }

    /**
     * 将Set以指定的KEY存入REDIS中
     * <pre>
     *     key:{
     *         {
     *             value
     *         }
     *     }
     * </pre>
     *
     * @param key      集合中的KEY
     * @param values   对应的值
     * @param timeout  过期时间
     * @param timeUnit 过期时间单位
     * @return true:成功 false:失败
     */
    public static boolean setValueForSet(String key, Set<? extends Object> values, long timeout, TimeUnit timeUnit) {
        log.info(">>>>>>>>>>> save value to redis|set|key:{}|start <<<<<<<<<<<", key);
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RSet<Object> set = redissonClient.getSet(key);
        try {
            set.add(values);
            // 设置过期时间
            set.expire(timeout, timeUnit);
            log.info(">>>>>>>>>>> save value to redis|set|success <<<<<<<<<<<");
            return true;
        } catch (Exception e) {
            log.error(">>>>>>>>>>> save value to redis|set|key:{}|exception: <<<<<<<<<<<", key, e);
            throw new RedisException(SystemRetCodeConstants.REQUISITE_PARAMETER_NOT_EXIST.getCode(),
                    SystemRetCodeConstants.REQUEST_DATA_ERROR.getMessage().concat(":用户ID为空"));
        }
    }

    /**
     * 将Set以指定的KEY存入REDIS中
     * <pre>
     *     key:{
     *         {
     *             value
     *         }
     *     }
     * </pre>
     *
     * @param key    集合中的KEY
     * @param values 对应的值
     * @return true:成功 false:失败
     */
    public static boolean setValueForSet(String key, Set<String> values) {
        log.info(">>>>>>>>>>> save value to redis|set|key:{}|start <<<<<<<<<<<", key);
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        try {
            RSet<String> set = redissonClient.getSet(key);
            set.clear();
            set.addAll(values);
            log.info(">>>>>>>>>>> save value to redis|set|success <<<<<<<<<<<");
            return true;
        } catch (Exception e) {
            log.error(">>>>>>>>>>> save value to redis|set|key:{}|exception: <<<<<<<<<<<", key, e);
            throw new RedisException(SystemRetCodeConstants.REQUISITE_PARAMETER_NOT_EXIST.getCode(),
                    SystemRetCodeConstants.REQUEST_DATA_ERROR.getMessage().concat(":用户ID为空"));
        }
    }

    /**
     * 生成指定KYE的自增长值
     *
     * @param key  存储的KEY
     * @param step 步长
     * @return 增长后的值
     */
    public static long getIncr(String key, long step) {
        if (StringUtils.isEmpty(key)) {
            return -1;
        }

        if (step <= 0) {
            step = 1;
        }

        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RedissonAtomicLong redissonAtomicLong = (RedissonAtomicLong) redissonClient.getAtomicLong(key);
        // 增加指定步长
        return redissonAtomicLong.addAndGet(step);
    }

    /**
     * 指定KEY递增
     *
     * @param key 存储的KEY
     * @return 递增后的值
     */
    public static long getIncr(String key) {
        if (StringUtils.isEmpty(key)) {
            return -1;
        }

        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RedissonAtomicLong redissonAtomicLong = (RedissonAtomicLong) redissonClient.getAtomicLong(key);
        // 递增1
        return redissonAtomicLong.getAndAdd(1L);
    }

    /**
     * 指定KEY自减
     *
     * @param key 存储的KEY
     * @return 递减后的值
     */
    public static long getDecr(String key) {
        if (StringUtils.isEmpty(key)) {
            return -1;
        }

        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RedissonAtomicLong redissonAtomicLong = (RedissonAtomicLong) redissonClient.getAtomicLong(key);
        // 递减1
        return redissonAtomicLong.getAndDecrement();
    }
}
