#### 后端开发规范

时间：2023年05月04日 18:51:29

---

#### 一、控制器（Controller）

1.1 入参

> 我们约定入参以`AaaReq`命名；
>
> Controller层方法接收的参数`A`和Service层方法接收的参数`B`有部分差异，前者`A`将直接处理视图传入的数据，后者`B`参数是业务必须参数（不需要包含`A`中所有数据）
>
> - 系统侧将提供`AbstractRequest`，内部封装了自定义的`PageInfo`，Controller层可在`AbstractRequest`基础上派生`AbstractBaseRequest`，具体的请求体继承自派生出的抽象类（Service层入参则直接继承`AbstractRequest`）

1.2 响应

> 我么约定Controller层返回类型统一封装为`ResponseData`
>
> - success:Boolean
> - code:int
>   - message:String
> - T:result
> - timestamp:long（默认填充）
>
> 当然，会提供一个工具类`ResponseProcessor`，用于快速得到格式正确和错误的`ResponseData`

1.3 Controller中逻辑模板

> 第一步：解析`AaaReq`，将适合的数据填充到Service层的参数`AaaRequest`中；
>
> 第二步：调用服务；
>
> 第三步：处理结果
>
> - 情况一：正常得到响应
>
>   - ```java
>     // 查询（后端全部分页，分页参数由前端控制，不想要分页时，前端放大分页参数即可）
>     PageResponse pageResponse = new PagerResponse(列表, total);
>     new ResponseData<PageResponse<列表泛型>>().setData(pageResponse, response.getMsg())
>     ```
>
>   - ```java
>     // 数据回填（适用于新增）
>     new ResponseData<列表泛型>().setData(列表, response.getMsg())
>     ```
>
>   - ```java
>     // 更新、删除、启停
>     new ResponseData<>().setData(null, response.getMsg())
>     ```
>
> - 情况二：捕获到BizException
>
>   - ```java
>     new ResponseData<>().setErrorMessage(be.getErrorCode, be.getMessage);
>     ```
>
> - 情况三：捕获到未知Exception
>
>   - ```java
>     new ResponseData<>()
>     .setErrorMessage(SystemReturnCodeConstants.SYSTEM_ERROR.getCode(),SystemReturnCodeConstants.SYSTEM_ERROR.getMessage());
>     ```





#### 二、Service（服务层）

2.1 入参

> 和Controller层入参类似，我们约定将Service层的入参封装为`AaaRequest → AbstractRequest`
>
> - AaaVO
> - List< AaaVo >
> - → PageInfo
>
> `AaaRequest`中需要提供Service实现的参数校验方法。

2.2 响应

> 我们约定响应分为两种，<u>二者均继承于`AbstractResponse(code:String, msg:String)🔔`</u>
>
> - BaseResponse< T >
>   - T result
>   - long total
> - DefaultResponse
>   - 空

2.3 Service中逻辑模板

> 2.3.1 三个地方标记日志锚点（开始、结束与异常）
>
> 2.3.2 参数校验（`AaaRequest`）
>
> 2.3.3 定义初始化响应体response、total
>
> 2.3.4 从request中解析参数
>
> 2.3.5 【业务处理】← 需要捕获两种异常（`BizException`和`Exception`）
>
> 2.3.6 填充响应体
>
> - total
> - result
> - code
> - msg





















