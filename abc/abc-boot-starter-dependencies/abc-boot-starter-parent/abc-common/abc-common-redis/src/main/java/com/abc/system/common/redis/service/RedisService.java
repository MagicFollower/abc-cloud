package com.abc.system.common.redis.service;

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
 * <pre>
 * 注意：
 * 🤔️当前服务默认会为序列化后的键值添加Java序列化标记, 如果想移除这种序列化标记，请手动配置key、value为String类型序列化器/自定义序列化器。
 * 127.0.0.1:6381[15]> KEYS *
 *   → 1) "USER_002"
 *   → 2) "\xac\xed\x00\x05t\x00\bUSER_001"
 * 127.0.0.1:6381[15]> get \xac\xed\x00\x05t\x00\bUSER_001
 *   → (nil)
 * 127.0.0.1:6381[15]> get "\xac\xed\x00\x05t\x00\bUSER_001"
 *   → "\xac\xed\x00\x05t\x00\x1e{\"username\":\"\xe6\xb5\x8b\xe8\xaf\x95\xe7\x94\xa8\xe6\x88\xb7001\"}"
 * 127.0.0.1:6381[15]>
 * 🤔️关于序列化前缀`\xac\xed\x00\x05t\x00\b`。
 *   → `\xac\xed\x00\x05t\x00\b` 是一个字节序列，看起来像是Java序列化后的字节码。具体来说，这个字节序列可以分为以下几个部分：
 *     → `\xac\xed`：这两个字节是Java序列化文件的魔数，表示这是一个Java序列化文件。
 *     → `\x00\x05`：这两个字节是Java序列化文件的版本号，表示这个序列化文件是使用Java 5生成的。
 *     → `t`：这个字节表示下一个对象是一个对象类型。
 *     → `\x00\b`：这两个字节表示下一个对象的长度为8个字节（64位）。
 * 🤔️手动配置String类型序列化器/自定义序列化器（示例）。
 * {@code
 * @Configuration
 * @EnableCaching
 * @AutoConfigureBefore(RedisAutoConfiguration.class)
 * public class RedisConfig implements CachingConfigurer {
 *     @Bean
 *     @SuppressWarnings(value = {"unchecked", "rawtypes"})
 *     public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
 *         RedisTemplate<Object, Object> template = new RedisTemplate<>();
 *         template.setConnectionFactory(connectionFactory);
 *
 *         FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);
 *
 *         // 使用StringRedisSerializer来序列化和反序列化redis的key值
 *         template.setKeySerializer(new StringRedisSerializer());
 *         template.setValueSerializer(serializer);
 *
 *         // Hash的key也采用StringRedisSerializer的序列化方式
 *         template.setHashKeySerializer(new StringRedisSerializer());
 *         template.setHashValueSerializer(serializer);
 *
 *         template.afterPropertiesSet();
 *         return template;
 *     }
 * }
 * }
 * </pre>
 * @Author [author_name]
 * @Date 2077/5/1 15:43
 * @Version 1.0
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class RedisService {
    public final RedisTemplate redisTemplate;

    public RedisService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
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
     * 设置有效时间(Duration)
     *
     * @param key      Redis键
     * @param duration 时间范围
     * @return true=设置成功；false=设置失败
     */
    public boolean setExpire(final String key, final Duration duration) {
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
     * @return 缓存键值对应的数据（数据不存在返回null）
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
        if (dataList != null && !dataList.isEmpty()) {
            Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
            return count != null ? count : 0;
        }
        return 0;
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @param duration 过期时间
     * @return 操作后列表的长度
     */
    public <T> long setList(final String key, final List<T> dataList, final Duration duration) {
        if (dataList != null && !dataList.isEmpty()) {
            Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
            if (count != null && count != 0) {
                setExpire(key, duration);
            }
            return count != null ? count : 0;
        }
        return 0;
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
        if (dataSet != null && !dataSet.isEmpty()) {
            for (T t : dataSet) {
                setOperation.add(t);
            }
        }
        return setOperation;
    }

    /**
     * 缓存Set
     *
     * @param key      缓存键值
     * @param dataSet  缓存的数据
     * @param duration 过期时间
     * @return BoundSetOperations
     */
    public <T> BoundSetOperations<String, T> setSet(final String key, final Set<T> dataSet, final Duration duration) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        if (dataSet != null && !dataSet.isEmpty()) {
            for (T t : dataSet) {
                setOperation.add(t);
            }
            setExpire(key, duration);
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
        if (dataMap != null && !dataMap.isEmpty()) {
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
        if (dataMap != null && !dataMap.isEmpty()) {
            redisTemplate.opsForHash().putAll(key, dataMap);
            setExpire(key, duration);
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
}

