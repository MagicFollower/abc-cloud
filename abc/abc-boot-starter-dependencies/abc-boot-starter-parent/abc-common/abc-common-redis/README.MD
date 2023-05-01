### abc-common-redis组件文档

---

#### 🚀版本：0.0.1-SNAPSHOT

- 使用`fastjson2`作为Redis的序列化、反序列化器
- 注入`RedisService`至IOC容器（`封装RedisTmplate` ）
  - `RedisService`中的API返回值为`未包装基本类型` 
- `RedisService`提供的接口清单如下：

```conf
void setCacheObject(final String key, final T value)
void setCacheObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit)
long setCacheList(final String key, final List<T> dataList)
BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet)
void setCacheMap(final String key, final Map<String, T> dataMap)
➕void setCacheMapValue(final String key, final String hKey, final T value)
⬇️
boolean expire(final String key, final long timeoutSeconds)
boolean expire(final String key, final long timeout, final TimeUnit unit)
long getExpire(final String key)
```

```conf
boolean hasKey(String key)
⬇️
T getCacheObject(final String key)
List<T> getCacheList(final String key)
Set<T> getCacheSet(final String key)
Map<String, T> getCacheMap(final String key)
➕T getCacheMapValue(final String key, final String hKey)
➕List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
⬇️
Collection<String> keys(final String pattern)
```

```conf
boolean deleteObject(final String key)
boolean deleteObject(final Collection collection)
➕boolean deleteCacheMapValue(final String key, final String hKey)
```

- 使用该组件，你应该知道如何使用`SpringRedis`的基本配置
  - 单机
  - 哨兵
  - ~~集群（不推荐）~~

```yaml
spring:
  redis:
    database: 1
    host: 192.168.204.123
    # password: <your-redis-password>
    port: 30379
    timeout: 5000
```

```yaml
spring:
  redis:
    timeout: 5000
    # sentinel哨兵配置: https://www.yuque.com/u22384616/aeg3ox/llz54w
    sentinel:
      master: mymaster
      nodes: 127.0.0.1:26379,127.0.0.1:26380,127.0.0.1:26381
```







