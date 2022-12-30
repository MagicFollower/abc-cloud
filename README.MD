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

- PageUtils
- ServletUtils
- BaseController
- TableDataInfo
- TableSupport
- PageDomain

![](https://github.com/MagicFollower/abc-cloud/blob/main/doc/images/01_%E5%88%86%E9%A1%B5%E6%93%8D%E4%BD%9C.png?raw=true)







