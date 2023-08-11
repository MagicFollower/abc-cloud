## README.MD

时间：2022年12月27日20:43:44

---

#### 一、开启验证码

- 验证码配置在gateway模块中
  - 属性清单：com.abc.gateway.config.properties.CaptchaProperties
    - enabled   是否开启
    - type         验证码类型
      - math
      - char

#### 二、网关白名单、动态路由

- com.abc.gateway.config.properties.IgnoreWhiteProperties
  - security.ignore
  - 白名单需要配置login接口等
    - /auth/login

```
security:
  ignore:
    whites:
      - /auth/login
```

- 网关动态路由配置

```
# Spring
spring:
  application:
    # 应用名称
    name: abc-gateway
  cloud:
    gateway:
      routes:
        - id: abc-auth
          uri: lb://abc-auth
          predicates:
            - Path=/auth/**
        - id: abc-system
          uri: lb://abc-system
          predicates:
            - Path=/system/**
```



#### 三、分页操作
> 官方文档：`https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md`

```xml
<!-- Pagehelper 1.4.6 -->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
</dependency>
```
> SpringBoot基本配置

```yaml
# MyBatis分页插件配置
pagehelper:
  helper-dialect: mysql
  reasonable: false
  default-count: false
```

> 核心类关系与基本使用

- PageUtils
- ServletUtils
- BaseController
- TableDataInfo
- TableSupport
- PageDomain

![01_分页原理](https://github.com/MagicFollower/abc-cloud/blob/main/doc/images/01_%E5%88%86%E9%A1%B5%E6%93%8D%E4%BD%9C.png?raw=true)

![02_分页实际操作.png](https://github.com/MagicFollower/abc-cloud/blob/main/doc/images/02_%E5%88%86%E9%A1%B5%E5%AE%9E%E9%99%85%E6%93%8D%E4%BD%9C.png?raw=true)

![03_表格分页数据对象TableDataInfo.png](https://github.com/MagicFollower/abc-cloud/blob/main/doc/images/03_%E8%A1%A8%E6%A0%BC%E5%88%86%E9%A1%B5%E6%95%B0%E6%8D%AE%E5%AF%B9%E8%B1%A1.png?raw=true)







