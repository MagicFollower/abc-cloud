## abc-common-dubbo模块介绍

> abc/abc-boot-starter-dependencies/abc-boot-starter-parent/abc-common/abc-common-dubbo

时间：2023年7月22日14:35:41

---

#### 消费者、生产者过滤器

> /resources/META-INF/dubbo/com.alibaba.dubbo.rpc.Filter
>
> - `❓dubbo微服务框架中配置的consumerTraceFilter和providerTraceFilter有什么作用?在什么过程生效？`

```properties
consumerTraceFilter=com.abc.system.common.dubbo.filter.ConsumerDubboTraceFilter
providerTraceFilter=com.abc.system.common.dubbo.filter.ProviderDubboTraceIdFilter
```

🚀Dubbo微服务框架中的`consumerTraceFilter`和`providerTraceFilter`是用于跟踪Dubbo服务调用链路的过滤器。这两个过滤器可以在Dubbo的消费者端和生产者端分别配置，用于记录服务调用的详细信息，包括请求和响应的数据、调用时间、调用链路等信息。这些信息可以帮助开发人员快速定位服务调用过程中的问题，提高调试效率。

具体来说，`consumerTraceFilter`和`providerTraceFilter`的作用如下：

1. `consumerTraceFilter`：在Dubbo的消费者端，这个过滤器可以<u>记录服务消费者发送的请求数据和接收到的响应数据</u>，并记录服务调用的时间、调用链路等信息。这些信息可以帮助开发人员快速定位服务调用过程中的问题，例如请求超时、响应错误等问题。

2. `providerTraceFilter`：在Dubbo的生产者端，这个过滤器可以<u>记录服务生产者接收到的请求数据和发送的响应数据</u>，并记录服务调用的时间、调用链路等信息。这些信息可以帮助开发人员快速定位服务提供者出现的问题，例如处理请求出错、响应超时等问题。

🚀生效阶段？这两个过滤器在服务调用过程中会自动生效，<u>无需手动调用</u>。它们会<u>自动拦截服务调用的请求和响应</u>，并将相关信息记录到日志中。如果需要查看服务调用的详细信息，可以通过查看Dubbo的日志文件来获取。

需要注意的是，为了确保这些过滤器正常工作，需要在Dubbo配置文件中进行相应的配置，以便让Dubbo框架正确地加载和使用这些过滤器。





































*END.*