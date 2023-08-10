### common-apollo-client组件介绍

时间：2023年5月12日20:17:56

---

##### 🌵需求分析：

1. 引入该依赖后就可以使用使用apollo配置中心；
2. 提供系统级别的全局缓存，可以解析apollo中的配置作为全局配置，例如：系统名称、系统版本、系统开发人员等；
3. 热更新。



##### 🌵需要提供的配置：

```yaml
apollo:
  ### Apollo默认配置
  bootstrap:
    enabled: true
    namespaces: datasource.yaml, dubbo.yaml, redis.yaml
  ### 自定义的系统级别配置缓存
  system:
    namespaces: system.yml
```

