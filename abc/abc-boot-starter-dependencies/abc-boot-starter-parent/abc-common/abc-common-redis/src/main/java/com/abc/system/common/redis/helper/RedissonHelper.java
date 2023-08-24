package com.abc.system.common.redis.helper;

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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RedissonHelper
 *
 * @Description RedissonHelper
 * @Author Trivis
 * @Date 2023/5/15 23:23
 * @Version 1.0
 */
@Slf4j
public class RedissonHelper {

    private RedissonHelper() {

    }

    //###############################################################################
    //##### 基本键值Key:Value, 键值均为String类型
    //###############################################################################

    /**
     * 获取指定KEY的值
     * <p>
     * 获取string类型的值
     * </p>
     *
     * @param key key
     * @return value
     */
    public static String get(String key) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        // 首先获取redis中的key-value对象
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 保存至redis中
     * <pre>
     *     不过期
     * </pre>
     *
     * @param key   key
     * @param value value
     */
    public static void set(String key, String value) {
        set(key, value, -1, null);
    }

    /**
     * 保存至redis中
     *
     * @param key      存储的键
     * @param value    存储的值
     * @param timeout  过期时间，-1：表示未过期
     * @param timeUnit 过期时间单位
     */
    public static void set(String key, String value, long timeout, TimeUnit timeUnit) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RBucket<String> rBucket = redissonClient.getBucket(key);
        if (-1 == timeout) {
            rBucket.set(value);
        } else {
            rBucket.set(value, timeout, timeUnit);
        }
    }

    /**
     * 从Redis中删除指定KEY的值
     *
     * @param key 待删除的KEY
     * @return true:成功 false:失败
     */
    public static boolean delete(String key) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.delete();
    }

    //###############################################################################
    //##### 映射Map
    //###############################################################################

    /**
     * 取得指定Map中的指定KEY的List列表
     *
     * @param mainKey 主KEY
     * @param key     实际值对应的KEY
     * @return 列表
     */
    public static List<String> getMap(String mainKey, String key) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RMap<String, String> maps = redissonClient.getMap(mainKey);
        return JSONObject.parseObject(maps.get(key), new TypeReference<List<String>>() {
        });
    }

    /**
     * 将一个Map存放到redis中, Map的值类型为List
     *
     * @param mainKey  redis中的KEY
     * @param values   map集合
     * @param timeout  过期时间
     * @param timeUnit 过期时间单位
     */
    public static void setMap(String mainKey, Map<String, List<String>> values, long timeout, TimeUnit timeUnit) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RMap<String, String> maps = redissonClient.getMap(mainKey);
        values.forEach((k, v) -> maps.put(k, JSONObject.toJSONString(v)));
        maps.expire(Duration.of(timeout, ChronoUnit.valueOf(timeUnit.name())));
    }

    /**
     * 将List存放到指定KEY的REDIS中
     *
     * @param mainKey  redis中的KEY
     * @param key      集合中的KEY
     * @param values   对应的值
     * @param timeout  过期时间
     * @param timeUnit 过期时间单位
     */
    public static void setMap(String mainKey, String key, List<String> values, long timeout, TimeUnit timeUnit) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RMap<String, String> maps = redissonClient.getMap(mainKey);
        maps.put(key, JSONObject.toJSONString(values));
        maps.expire(Duration.of(timeout, ChronoUnit.valueOf(timeUnit.name())));
    }

    //###############################################################################
    //##### 集合Set
    //###############################################################################

    /**
     * 取得指定KEY的Set集合
     *
     * @param key key
     * @return Set#String
     */
    public static Set<String> getSet(String key) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RSet<String> set = redissonClient.getSet(key);
        return set.readAll();
    }

    /**
     * 将Set以指定的KEY存入REDIS中
     *
     * @param key    集合中的KEY
     * @param values 对应的值
     */
    public static void setSet(String key, Set<String> values) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RSet<String> set = redissonClient.getSet(key);
        set.clear();
        set.addAll(values);
    }

    /**
     * 将Set以指定的KEY存入REDIS中
     *
     * @param key      集合中的KEY
     * @param values   对应的值
     * @param timeout  过期时间
     * @param timeUnit 过期时间单位
     */
    public static void setSet(String key, Set<?> values, long timeout, TimeUnit timeUnit) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RSet<Object> set = redissonClient.getSet(key);
        set.add(values);
        set.expire(Duration.of(timeout, ChronoUnit.valueOf(timeUnit.name())));
    }

    //###############################################################################
    //##### 自增、自减（返回处理后的long结果）, key不存在将新建key，同时从0开始处理
    //##### 仅提供addAndGet/getAndAdd相关API，有自减的需求将步长step设置负值即可
    //###############################################################################

    public static long addAndGet(String key) {
        return addAndGet(key, 1L);
    }

    public static long getAndAdd(String key) {
        return getAndAdd(key, 1L);
    }

    public static long addAndGet(String key, long step) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RedissonAtomicLong redissonAtomicLong = (RedissonAtomicLong) redissonClient.getAtomicLong(key);
        return redissonAtomicLong.addAndGet(step);
    }

    public static long getAndAdd(String key, long step) {
        RedissonClient redissonClient = SpringHelper.getBean(RedissonClient.class);
        RedissonAtomicLong redissonAtomicLong = (RedissonAtomicLong) redissonClient.getAtomicLong(key);
        return redissonAtomicLong.getAndAdd(step);
    }


    //##########################################################
    //### 存储数据，分页获取；利用Redis的ZSet
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
        log.info(">>>>>>>>>>> save data to redis|key={},recordCount={}|save times={} ms <<<<<<<<<<<", key, rScoredSortedSet.size(), (Duration.between(start, end).toMillis()));

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
    public static PageResponse getPageInfoFromRedis(PageInfo pageInfo, String key) {
        PageResponse pageResponse = new PageResponse();
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
        pageResponse = new PageResponse();
        pageResponse.setData(result);
        pageResponse.setTotal(rScoredSortedSet.size());
        Instant endTime = Instant.now();
        log.info(">>>>>>>>>>> query data from redis|key={}|start={}|end={}|total size={}|query times={} ms <<<<<<<<<<<", key, start, end, rScoredSortedSet.size(), (Duration.between(startTime, endTime).toMillis()));
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
        log.info(">>>>>>>>>>> get zset count from redis|key={},recordCount={}|save times={} ms <<<<<<<<<<<", key, recordCount, (Duration.between(startTime, endTime).toMillis()));
        return recordCount;
    }
}
