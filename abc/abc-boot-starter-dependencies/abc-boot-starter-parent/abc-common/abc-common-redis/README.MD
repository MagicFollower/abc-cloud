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

- 关于Redis的连接池

  - spring-boot-starter-data-redis:2.7.11使用的lettuce连接池

    ```yaml
    spring:
      redis:
        host: localhost
        port: 6379
        password: password
        timeout: 3000
        lettuce:
          pool:
            min-idle: 10
            max-idle: 20
            max-active: 30
    ```

  - redisson-spring-boot-starter:3.18.0使用的是自定义的连接池（***↪️默认会引入spring-boot-starter-data-redis，但是⤵️***）

    - ☀️`redisson-spring-boot-starter`会引入`spring-boot-starter-data-redis`，***但是移除了其中lettuce。***

    - [redisson/redisson-spring-boot-starter at master · redisson/redisson (github.com)](https://github.com/redisson/redisson/tree/master/redisson-spring-boot-starter#spring-boot-starter)

    - 注意：两种接口，两种配置，但是只有一种配置会生效（配置之间存在优先级）
    
      - ![redisson-spring-boot-starter配置优先级.png](https://github.com/MagicFollower/abc-cloud/blob/main/abc/abc-boot-starter-dependencies/abc-boot-starter-parent/abc-common/img/common-redis/redisson-spring-boot-starter%E9%85%8D%E7%BD%AE%E4%BC%98%E5%85%88%E7%BA%A7.png?raw=true)
      
      - ```xml
        <dependency>
            <groupId>org.redisson</groupId>
            <artifactId>redisson-spring-boot-starter</artifactId>
            <version>3.18.0</version>
        </dependency>
        <!-- codeC编解码器，效率较高，可替换为jsckson，参考下方参数解析 -->
        <dependency>
            <groupId>com.esotericsoftware</groupId>
            <artifactId>kryo</artifactId>
            <version>5.5.0</version>
        </dependency>
        ```
      
      ```yaml
      spring:
        redis:
         redisson: 
           config: |
              clusterServersConfig:
                password: null
                connectTimeout: 3000
                timeout: 3000
                idleConnectionTimeout: 10000
                retryAttempts: 2
                retryInterval: 1000
                failedSlaveReconnectionInterval: 3000
                failedSlaveCheckInterval: 60000
                clientName: null
                loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}
                subscriptionsPerConnection: 5
                subscriptionConnectionMinimumIdleSize: 1
                subscriptionConnectionPoolSize: 50
                slaveConnectionMinimumIdleSize: 24
                slaveConnectionPoolSize: 64
                masterConnectionMinimumIdleSize: 24
                masterConnectionPoolSize: 64
                readMode: "SLAVE"
                subscriptionMode: "SLAVE"
                nodeAddresses:
                - "redis://127.0.0.1:7004"
                - "redis://127.0.0.1:7001"
                - "redis://127.0.0.1:7000"
                masterName: "master"
                database: 0	
                scanInterval: 1000
                pingConnectionInterval: 0
                keepAlive: false
                tcpNoDelay: false
              threads: 4
              nettyThreads: 4
              codec: !<org.redisson.codec.Kryo5Codec> {}
              transportMode: "NIO"
      ```
      
      - 哨兵配置

        - ```yaml
          spring:
            application:
              name: abc-fastlink-business-portal
            main:
              banner-mode: off
            redis:
              redisson:
                file: classpath:redisson.yaml
          ```
      
        - ```yaml
          sentinelServersConfig:
            idleConnectionTimeout: 10000
            connectTimeout: 3000
            timeout: 3000
            retryAttempts: 2
            retryInterval: 1000
            failedSlaveReconnectionInterval: 3000
            failedSlaveCheckInterval: 60000
            password: null
            clientName: null
            loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}
            subscriptionsPerConnection: 5
            subscriptionConnectionMinimumIdleSize: 1
            subscriptionConnectionPoolSize: 50
            slaveConnectionMinimumIdleSize: 24
            slaveConnectionPoolSize: 64
            masterConnectionMinimumIdleSize: 24
            masterConnectionPoolSize: 64
            readMode: "SLAVE"
            subscriptionMode: "SLAVE"
            sentinelAddresses:
              - "redis://192.168.204.101:26379"
              - "redis://192.168.204.101:26380"
              - "redis://192.168.204.101:26381"
            masterName: "master"
            database: 15
          threads: 4
          nettyThreads: 4
          codec: !<org.redisson.codec.Kryo5Codec> {}
          transportMode: "NIO"
          ```
      
        - ```yaml
          spring:
            application:
              name: abc-fastlink-business-portal
            main:
              banner-mode: off
            redis:
              redisson:
                #file: classpath:redisson.yaml
                config: |
                  sentinelServersConfig:
                    idleConnectionTimeout: 10000
                    connectTimeout: 3000
                    timeout: 3000
                    retryAttempts: 2
                    retryInterval: 1000
                    failedSlaveReconnectionInterval: 3000
                    failedSlaveCheckInterval: 60000
                    password: null
                    clientName: null
                    loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}
                    subscriptionsPerConnection: 5
                    subscriptionConnectionMinimumIdleSize: 1
                    subscriptionConnectionPoolSize: 50
                    slaveConnectionMinimumIdleSize: 24
                    slaveConnectionPoolSize: 64
                    masterConnectionMinimumIdleSize: 24
                    masterConnectionPoolSize: 64
                    readMode: "SLAVE"
                    subscriptionMode: "SLAVE"
                    sentinelAddresses:
                      - "redis://192.168.204.101:26379"
                      - "redis://192.168.204.101:26380"
                      - "redis://192.168.204.101:26381"
                    masterName: "master"
                    database: 15
                  threads: 4
                  nettyThreads: 4
                  codec: !<org.redisson.codec.Kryo5Codec> {}
                  transportMode: "NIO"
          ```
      
      -  单机模式
      
        - ```yaml
          singleServerConfig:
            idleConnectionTimeout: 10000
            connectTimeout: 10000
            timeout: 3000
            retryAttempts: 3
            retryInterval: 1500
            password: null
            subscriptionsPerConnection: 5
            clientName: null
            address: "redis://127.0.0.1:6379"
            subscriptionConnectionMinimumIdleSize: 1
            subscriptionConnectionPoolSize: 50
            connectionMinimumIdleSize: 24
            connectionPoolSize: 64
            database: 0
            dnsMonitoringInterval: 5000
          threads: 16
          nettyThreads: 32
          codec: !<org.redisson.codec.Kryo5Codec> {}
          transportMode: "NIO"
          ```
      
      - ***配置解析***⤵️
      
      - redisson配置中idleConnectionTimeout、connectTimeout、timeout分别表示什么？
      
        - 在Redisson的配置中，`idleConnectionTimeout`、`connectTimeout`、`timeout`三个参数分别表示：
      
          - `idleConnectionTimeout`：空闲连接超时时间，当连接池中的连接空闲时间超过该值时，连接会被自动关闭。默认值是30秒。
          - `connectTimeout`：连接超时时间，当连接建立超过该时间时，会抛出异常。默认值是10000毫秒。
          - `timeout`：命令执行超时时间，当命令执行时间超过该值时，会抛出异常。默认值是3000毫秒。
      
          这三个参数都是关于Redis连接的超时时间设置，但是它们之间有一些差异：
      
          - `idleConnectionTimeout`是关于连接空闲时间的超时设置，即连接在空闲一段时间后会被自动关闭，主要影响连接池的性能。
          - `connectTimeout`是连接建立的超时时间设置，即连接在建立一段时间后如果没有建立成功就会抛出异常。
          - `timeout`是命令执行的超时时间设置，即命令执行在一定时间内没有响应就会抛出异常。
      
          需要注意的是，这三个参数都是可以根据实际情况进行调整的，但是需要根据实际情况进行合理的设置，以确保Redis连接的稳定性和性能。
      
      - redisson配置中retryAttempts、retryInterval、failedSlaveReconnectionInterval、failedSlaveCheckInterval分别表示什么？
      
        - 在Redisson的配置中，`retryAttempts`、`retryInterval`、`failedSlaveReconnectionInterval`、`failedSlaveCheckInterval`四个参数分别表示：
      
          - `retryAttempts`：Redis命令重试次数，当Redis命令执行失败时，会自动重试该命令的次数。默认值是3次。
          - `retryInterval`：Redis命令重试间隔时间，当Redis命令执行失败后，重试该命令的时间间隔。默认值是1500毫秒。
          - `failedSlaveReconnectionInterval`：从节点连接失败重试间隔时间，当Redis从节点连接失败时，重试连接的时间间隔。默认值是3000毫秒。
          - `failedSlaveCheckInterval`：从节点连接检查间隔时间，Redisson会定期检查从节点的连接是否正常。默认值是60000毫秒。
      
          这四个参数都是关于Redis连接的重试和从节点故障恢复的设置，但是它们之间有一些差异：
      
          - `retryAttempts`和`retryInterval`主要关注Redis命令执行的重试设置，即当Redis命令执行失败时，会自动重试该命令的次数和时间间隔。
          - `failedSlaveReconnectionInterval`和`failedSlaveCheckInterval`主要关注Redis从节点的故障恢复设置，即当Redis从节点连接失败时，重试连接的时间间隔和Redisson检查从节点连接是否正常的时间间隔。
      
          需要注意的是，这四个参数都是可以根据实际情况进行调整的，但是需要根据实际情况进行合理的设置，以确保Redis连接的稳定性和性能。
      
      - redisson配置中clientName、loadBalancer分别表示什么？
      
        - 在Redisson的配置中，`clientName`和`loadBalancer`两个参数分别表示：
      
          - `clientName`：Redis客户端名称，用于标识Redisson客户端。默认值是"redisson"。
          - `loadBalancer`：Redis集群负载均衡算法，用于在Redis集群中选择一个节点进行操作。默认值是"org.redisson.connection.balancer.RoundRobinLoadBalancer"，即轮询算法。
      
          这两个参数都是关于Redis连接的设置，但是它们之间有一些差异：
      
          - `clientName`主要用于标识Redisson客户端，可以根据实际情况进行设置，以便在Redis中进行监控和管理。
          - `loadBalancer`主要用于在Redis集群中选择一个节点进行操作，可以根据实际情况进行选择不同的负载均衡算法。
      
          需要注意的是，`loadBalancer`参数可以根据实际情况进行调整，以便选择最适合自己的负载均衡算法。另外，需要确保所选择的负载均衡算法能够保证Redis集群的稳定性和性能。
      
      - redisson配置中subscriptionsPerConnection、subscriptionConnectionMinimumIdleSize、subscriptionConnectionPoolSize分别表示什么？
      
        - 在Redisson的配置中，`subscriptionsPerConnection`、`subscriptionConnectionMinimumIdleSize`、`subscriptionConnectionPoolSize`三个参数分别表示：
      
          - `subscriptionsPerConnection`：每个连接的最大订阅数量，即每个Redis连接最多可以订阅的频道和模式的数量。默认值是5。
          - `subscriptionConnectionMinimumIdleSize`：订阅连接池最小空闲连接数，即订阅连接池中最少保持的空闲连接数量。默认值是1。
          - `subscriptionConnectionPoolSize`：订阅连接池最大连接数，即订阅连接池中最多可以创建的Redis连接数量。默认值是50。
      
          这三个参数都是关于Redis订阅连接池的设置，但是它们之间有一些差异：
      
          - `subscriptionsPerConnection`是关于Redis连接订阅数量的设置，即每个Redis连接最多可以订阅的频道和模式的数量。
          - `subscriptionConnectionMinimumIdleSize`是关于订阅连接池空闲连接数量的设置，即订阅连接池中最少保持的空闲连接数量。
          - `subscriptionConnectionPoolSize`是关于订阅连接池最大连接数量的设置，即订阅连接池中最多可以创建的Redis连接数量。
      
          需要注意的是，这三个参数都是可以根据实际情况进行调整的，但是需要根据实际情况进行合理的设置，以确保Redis订阅连接池的稳定性和性能。
      
      - 每个连接的订阅数量指的是什么？订阅连接池是什么
      
        - 每个连接的订阅数量指的是每个Redis连接最多可以同时订阅的频道和模式的数量。在Redis中，客户端可以通过订阅频道和模式来接收Redis服务器发送的消息，而每个Redis连接都可以订阅多个频道和模式。
      
          订阅连接池是Redisson中用于管理Redis订阅连接的连接池。在Redisson中，为了提高Redis订阅的性能和可靠性，采用了连接池的方式来管理Redis订阅连接。订阅连接池中维护了多个Redis连接，以便在Redis订阅期间保持连接的可用性。在Redisson中，默认情况下，订阅连接池中最多可以创建50个Redis连接，其中最少保持1个空闲连接。同时，每个Redis连接最多可以订阅5个频道和模式。这些参数都可以根据实际情况进行调整，以满足不同场景下的需求。
      
      - redisson配置中slaveConnectionMinimumIdleSize、slaveConnectionPoolSize、masterConnectionMinimumIdleSize、masterConnectionPoolSize分别表示什么？
      
        - 在Redisson的配置中，`slaveConnectionMinimumIdleSize`、`slaveConnectionPoolSize`、`masterConnectionMinimumIdleSize`、`masterConnectionPoolSize`四个参数分别表示：
      
          - `slaveConnectionMinimumIdleSize`：从节点连接池最小空闲连接数，即从节点连接池中最少保持的空闲连接数量。默认值是1。
          - `slaveConnectionPoolSize`：从节点连接池最大连接数，即从节点连接池中最多可以创建的Redis连接数量。默认值是64。
          - `masterConnectionMinimumIdleSize`：主节点连接池最小空闲连接数，即主节点连接池中最少保持的空闲连接数量。默认值是10。
          - `masterConnectionPoolSize`：主节点连接池最大连接数，即主节点连接池中最多可以创建的Redis连接数量。默认值是64。
      
          这四个参数都是关于Redis主从复制模式下（哨兵模式）的连接池设置，但是它们之间有一些差异：
      
          - `slaveConnectionMinimumIdleSize`和`slaveConnectionPoolSize`是关于从节点连接池的设置，即从节点连接池中最少保持的空闲
      
      - redisson配置中readMode、subscriptionMode、scanInterval、pingConnectionInterval分别表示什么？
      
        - 在Redisson的配置中，`readMode`、`subscriptionMode`、`scanInterval`、`pingConnectionInterval`四个参数分别表示：
      
          - `readMode`：Redisson读取模式，即在读取数据时采用的策略。默认值是`SLAVE`，表示优先从从节点读取数据，如果从节点不可用，则从主节点读取数据。另外还可以设置为`MASTER`，表示只从主节点读取数据。
          - `subscriptionMode`：Redisson订阅模式，即在订阅频道和模式时采用的策略。默认值是`SLAVE`，表示优先从从节点订阅频道和模式，如果从节点不可用，则从主节点订阅频道和模式。另外还可以设置为`MASTER`，表示只从主节点订阅频道和模式。
          - `scanInterval`：Redisson扫描间隔时间，即Redisson定期检查连接池中连接是否可用的时间间隔。默认值是1000毫秒，即每隔1秒钟检查一次连接池中的连接是否可用。
          - `pingConnectionInterval`：Redisson心跳检测间隔时间，即Redisson定期发送PING命令检测Redis连接是否可用的时间间隔。默认值是0，表示不进行心跳检测。如果设置了该参数，则表示每隔一定时间（单位是毫秒）发送一次PING命令检测Redis连接是否可用。
      
          这四个参数都是关于Redisson客户端的一些配置，用于控制Redisson在连接Redis服务器时的一些行为。需要注意的是，这些参数都是可以根据实际情况进行调整的，但是需要根据实际情况进行合理的设置，以确保Redisson客户端的稳定性和性能。
      
      - redisson配置中keepAlive、tcpNoDelay分别表示什么？
      
        - 在Redisson的配置中，`keepAlive`和`tcpNoDelay`两个参数分别表示：
      
          - `keepAlive`：是否开启TCP连接的SO_KEEPALIVE属性。如果设置为true，则表示开启TCP连接的SO_KEEPALIVE属性，即在TCP连接空闲一段时间后，会自动向对端发送心跳包以保持连接的可用性。默认值是false，表示不开启TCP连接的SO_KEEPALIVE属性。
          - `tcpNoDelay`：是否开启TCP连接的TCP_NODELAY属性。如果设置为true，则表示开启TCP连接的TCP_NODELAY属性，即禁用Nagle算法，可以提高小数据包传输的性能。默认值是true，表示开启TCP连接的TCP_NODELAY属性。
      
          需要注意的是，这两个参数都是关于TCP连接的一些配置，用于控制TCP连接的行为。需要根据实际情况进行设置，以确保TCP连接的稳定性和性能。
      
      - redisson配置中threads、nettyThreads、codec、transportMode分别表示什么？
      
        - 在Redisson的配置中，`threads`、`nettyThreads`、`codec`、`transportMode`四个参数分别表示：
      
          - `threads`：Redisson客户端的工作线程数量。默认值是当前CPU核心数*2。
          - `nettyThreads`：Redisson客户端Netty的工作线程数量。默认值是当前CPU核心数*2。
          - `codec`：Redisson客户端的编解码器类型。默认值是`org.redisson.codec.JsonJacksonCodec`，表示使用JSON格式进行编解码。另外还可以设置为`org.redisson.codec.SerializationCodec`，表示使用Java序列化进行编解码。
          - `transportMode`：Redisson客户端的传输模式。默认值是`NIO`，表示使用Java NIO进行网络传输。另外还可以设置为`EPOLL`，表示使用Linux EPOLL进行网络传输。
      
          需要注意的是，这四个参数都是关于Redisson客户端的一些配置，用于控制Redisson客户端的行为。需要根据实际情况进行设置，以确保Redisson客户端的稳定性和性能。
      
      - org.redisson.codec.Kryo5Codec是什么？
      
        - `org.redisson.codec.Kryo5Codec`是Redisson提供的一种编解码器类型，用于在Redisson客户端和Redis服务器之间进行数据的序列化和反序列化。它使用Kryo 5.x版本进行对象的序列化和反序列化，相比于Java默认的序列化方式，Kryo序列化更加高效，序列化后的数据更加紧凑，可以减少网络传输的数据量和传输时间，从而提高Redisson客户端的性能。
      
      - *End.*



