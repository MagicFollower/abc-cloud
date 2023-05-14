## 🌵Dubbo微服务示例、common系统组件集成测试

时间：2023年5月14日21:58:01

---

#### 一、三个模块依赖与作用

abc-fastlink-business-goods

⤴️

abc-fastlink-business-order

⤴️

abc-fastlink-business-portal（门户服务，暴露web端口）





#### 二、abc-fastlink-business-portal返回值

> com.abc.business.fastlink.portal.controller.order.OrderController

请求：

- POST#localhost:9999/fastlink/base/order/query

响应：

```json
{
    "success": true,
    "code": 200,
    "message": "成功",
    "result": {
        "total": 1,
        "data": "goods_data→order_data"
    },
    "timestamp": 1684072588490
}
```



