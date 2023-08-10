#### abc-common-dao组件文档

`更新时间：2023年08月10日 21:33:03`

---

##### 🚀版本：0.0.1-SNAPSHOT

- 支持tk-mybatis、拓展tk-mybatis支持mysql的批量添加与更新
  - `AbcTkMapper`
  - `AbcTkGlobalIdGen`（使用MP中的IdWorker）
- 支持mybatis-plus
  - `AbcMpMapper`
  - `AbcMpService`
  - `AbcMpServiceImpl`
- 🔓配置文件使用说明
  - mybatis默认会读取的xml：`classpath*:/mapper/**/*.xml`
  - 由于mybatis-plus和tk-mybatis结合使用，配置文件读取优先级方面会存在互相影响，详情见文档结束的注意事项⚠️

```yaml
mybatis:
  mapper-locations: ["classpath*:/mapper/**/*.xml"]
  configuration:
    # 默认为 true
    map-underscore-to-camel-case: true
    # 关闭日志打印 org.apache.ibatis.logging.nologging.NoLoggingImpl
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

- 🔓DAO和DAOxml存放位置约定
  - ① DAO存放在java目录下
  - ② DAOxml存放在resource下和java同级包路径下
  - ⚠️注意：建议使用mybatis-generator生成Entity和DAO/DAOxml！

- 🔓建议使用HikariCP连接池（`下方给出推荐示例配置`）

```yaml
mybatis:
  # "classpath*:/mapper/**/*.xml"
  mapper-locations: ["classpath*:/mapper/**/*.xml"]
  configuration:
    # 默认为 true
    map-underscore-to-camel-case: true
    # 关闭日志打印 org.apache.ibatis.logging.nologging.NoLoggingImpl
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

spring:
  datasource:
    # type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.204.101:23306/dev_test?autoReconnect=true&useServerPreparedStmts=true&cachePrepStmts=true&rewriteBatchedStatements=true&allowMultiQueries=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: ********************
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      ### 最小空闲连接，默认值10，小于0或大于maximum-pool-size，都会重置为maximum-pool-size
      minimum-idle: 10
      ### 最大连接数，小于等于0会被重置为默认值10；大于零小于1会被重置为minimum-idle的值
      maximum-pool-size: 20
      ### 连接池名称，默认HikariPool-1
      pool-name: HikariPool-Abc-1
      ### 连接超时时间:毫秒，小于250毫秒，会被重置为默认值30秒
      connection-timeout: 10000
      ### 连接测试查询
      connection-test-query: SELECT 1
      ### 空闲连接超时时间，默认值600000（10分钟），大于等于max-lifetime且max-lifetime>0，会被重置为0；不等于0且小于10秒，会被重置为10秒。
      idle-timeout: 600000
      ### 连接最大存活时间.不等于0且小于30秒，会被重置为默认值30分钟.设置应该比mysql设置的超时时间短；单位ms
      max-lifetime: 1800000
      ### 自动提交从池中返回的连接，默认值为true
      auto-commit: true
```







#### ⚠️使用注意事项

> 由于mybatis-plus和tk-mybatis结合使用，配置文件读取优先级方面会存在互相影响
>
> 🤔️问题：`为什么mybatis-plus-boot-starter和mapper-spring-boot-starter同时配置时，只有mybatis-plus的配置生效，默认mybatis的全局不生效？`

当两个组件同时配置时，`mybatis-plus-boot-starter` 中的自动化配置通常会覆盖掉 `mapper-spring-boot-starter` 中的配置。

如果您希望同时使用这两个组件，并且希望 `mapper-spring-boot-starter` 中的 MyBatis 配置生效，可以尝试以下方法：

禁用 `mybatis-plus-boot-starter` 的自动配置：

- 在主配置类（通常是 `Application` 类）上添加 `exclude` 参数，并排除 `MybatisPlusAutoConfiguration`

- ```java
  @SpringBootApplication(exclude = {MybatisPlusAutoConfiguration.class})
  public class YourApplication {
      // ...
  }
  ```

你可能会想直接在pom中移除abc-common-dao中的mp启动器依赖，向下面这样⤵️

```xml
<dependency>
    <groupId>com.abc</groupId>
    <artifactId>abc-common-dao</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <exclusions>
        <exclusion>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

这是可以的，但是由于TK-MYBATIS中ID生成器使用的是MP中的IdWorker，所以你此时需要手动实现一个TK-MYBATIS可以使用的ID生成器。

- 为了解决这个问题，common-dao包中将mp-starter和mp-core分开引入，你可以直接移除mp-starter但是还可以使用mp-core中的IdWorker和其他工具——`2023年8月10日21:30:57`

> 😄关于snowflake算法
>
> Snowflake算法是一种基于时间戳和机器ID的算法，它可以生成具有一定顺序的64位长整型ID。
>
> Snowflake算法的ID由以下几个部分组成：
>
> 1. timestamp（时间戳）：41位，精确到毫秒级别。
> 2. data center ID（数据中心ID）：5位，用于标识不同的数据中心。
> 3. worker ID（工作节点ID）：5位，用于标识不同的工作节点。
> 4. sequence（序列号）：12位，表示同一毫秒内生成的不同ID的序列。
>
> 通过组合以上部分，Snowflake算法保证了在分布式系统中生成的ID的唯一性、顺序性和时间戳的可读性。

关于Mybatis的常用全局配置项：

```yaml
mybatis:
  mapper-locations: ["classpath*:/aaa/**/*.xml"]  # 默认为classpath:/mapper/*.xml
  type-aliases-package: com.example.your-package.dal.entity  # 无默认值
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  # 默认o.a.i.l.nologging.NoLoggingImpl
    map-underscore-to-camel-case: false  # 默认为true
    jdbc-type-for-null: null  # 默认other
```



























***<u>END.</u>***
