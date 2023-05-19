package com.abc.system.common.redis.service;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.redis.RedisException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RedisService
 *
 * @Description RedisService, 自动注入到IOC容器
 * @Author Trivis
 * @Date 2023/5/1 15:43
 * @Version 1.0
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class RedisService {
    public final RedisTemplate redisTemplate;

    public RedisService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void set(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param duration 时间范围
     */
    public <T> void set(final String key, final T value, final Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    /**
     * 设置有效时间(单位秒)
     *
     * @param key            Redis键
     * @param timeoutSeconds 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeoutSeconds) {
        return expire(key, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * 设置有效时间(Duration)
     *
     * @param key      Redis键
     * @param duration 时间范围
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final Duration duration) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, duration));
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间(秒)
     */
    public long getExpire(final String key) {
        Long expire = redisTemplate.getExpire(key);
        return expire != null ? expire : 0;
    }

    /**
     * 获得缓存的基本对象
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T get(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存键值
     * @return true/false
     */
    public boolean delete(final String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return true/false
     */
    public boolean delete(final Collection<String> collection) {
        Long delete = redisTemplate.delete(collection);
        return delete != null && delete > 0;
    }

    //###############################################################
    //######List、Set、Map
    //###############################################################

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 操作后列表的长度
     */
    public <T> long setList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count != null ? count : 0;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return BoundSetOperations
     */
    public <T> BoundSetOperations<String, T> setSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        for (T t : dataSet) {
            setOperation.add(t);
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key 缓存键值
     * @return Set集合
     */
    public <T> Set<T> getSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key     缓存键值
     * @param dataMap Map
     * @param <T>     T
     */
    public <T> void setMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 缓存Map
     *
     * @param key     缓存键值
     * @param dataMap Map
     * @param <T>     T
     */
    public <T> void setMap(final String key, final Map<String, T> dataMap, final Duration duration) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
            expire(key, duration);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key 缓存键值
     * @return T
     */
    public <T> Map<String, T> getMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiMapValue(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteMapValue(final String key, final String hKey) {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串模式（aaa*、*aaa、*aaa*）
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }


    private void keyExistsCheck(String key){
        if(StringUtils.isEmpty(key)) {
            // throw new RedisException(SystemRetCodeConstants.REDIS_KEY_NOT_EXISTS);
        }
    }
}

