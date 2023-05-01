#### abc-common-dao组件文档

---

##### 🚀版本：0.0.1-SNAPSHOT

- 支持tk-mybatis、拓展tk-mybatis支持mysql的批量添加与更新
  - `AbcTkMapper`
  - `AbcTkGlobalIdGen`
- 支持mybatis-plus
  - `AbcMpMapper`
  - `AbcMpService`
  - `AbcMpServiceImpl`
- 🔓配置文件使用说明
  - mybatis默认会读取的xml：`classpath*:/mapper/**/*.xml`
  - 由于mybatis-plus和tk-mybatis结合使用，尽量避免单独配置每个组件，尽量使用mybatis统一的配置
    - tk-mybatis使用mybatis的配置，如果有额外的需求，可以单独配置mybatis-plus的配置。
      - ⚠️注意：mybatis.mapper-locations配置不会对mybatis-plus生效！

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

