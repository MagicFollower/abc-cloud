#### 后端开发规范

时间：2023年05月04日 18:51:29

---

#### 🌵控制器（Controller）

##### 1.1 入参

> 我们约定入参以`AaaReq`命名；
>
> Controller层方法接收的参数`A`和Service层方法接收的参数`B`有部分差异，前者`A`将直接处理视图传入的数据，后者`B`参数是业务必须参数（不需要包含`A`中所有数据）
>
> - 系统侧将提供`AbstractRequest`，内部封装了自定义的`PageInfo`，Controller层可在`AbstractRequest`基础上派生`AbstractBaseRequest`，具体的请求体继承自派生出的抽象类（Service层入参则直接继承`AbstractRequest`）

##### 1.2 响应

> 我么约定Controller层返回类型统一封装为`ResponseData`
>
> - *<u>**success:Boolean**</u>*
> - *<u>**code:int**</u>*
>   - *<u>**message:String**</u>*
> - *<u>**T:result**</u>*
> - *<u>**timestamp:long（默认填充）**</u>*
>
> 当然，会提供一个工具类`ResponseProcessor`，用于快速得到格式正确和错误的`ResponseData`

##### 1.3 Controller中逻辑模板

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
>     new ResponseProcessor<PageResponse<列表泛型>>().setData(pageResponse, response.getMsg())
>     ```
>
>   - ```java
>     // 数据回填（适用于新增）
>     new ResponseProcessor<列表泛型>().setData(列表, response.getMsg())
>     ```
>
>   - ```java
>     // 更新、删除、启停
>     new ResponseProcessor<>().setData(null, response.getMsg())
>     ```
>
> - 💥情况二：捕获 <u>*BaseException*</u>（`BizException的父类`）
>
>   - ```java
>     // 错误日志锚点使用三参数log.error
>     log.error(">>>>xxx|xxx|xxx:{}<<<", be.getMessage(), be)
>     new ResponseData<>().setErrorMessage(be.getErrorCode, be.getMessage);
>     ```
>
> - 💥情况三：捕获未知Exception
>
>   - ```java
>     // 错误日志锚点使用三参数log.error
>     log.error(">>>>xxx|xxx|xxx:{}<<<", e.getMessage(), e)
>     new ResponseData<>()
>     .setErrorMessage(SystemReturnCodeConstants.SYSTEM_ERROR.getCode(),SystemReturnCodeConstants.SYSTEM_ERROR.getMessage());
>     ```





#### 🌵Service（服务层）

##### 2.1 入参

> 和Controller层入参类似，我们约定将Service层的入参封装为`AaaRequest → AbstractRequest`
>
> - AaaVO
> - List< AaaVo >
> - → PageInfo
>
> `AaaRequest`中需要提供Service实现的参数校验方法。

##### 2.2 响应

> 我们约定响应分为两种，<u>二者均继承于`AbstractResponse(code:String, msg:String)🔔`</u>
>
> - ***<u>🎁BaseResponse< T ></u>***
>   - T result
>   - long total
> - ***<u>🎁DefaultResponse</u>***
>   - 空

##### 2.3 Service中逻辑模板

> 2.3.1 三个地方标记日志锚点（开始、结束与异常）
>
> 2.3.2 参数校验（`AaaRequest`）
>
> 2.3.3 定义初始化响应体response、total
>
> 2.3.4 从request中解析参数
>
> 2.3.5 【业务处理】← 需要捕获两种异常（`BaseException异常基类`和`Exception`）
>
> - BaseException异常基类
>
>   - ```java
>     log.error(">>>>xxx|xxx|xxx:{}<<<", be.getMessage(), be)
>     throw new BizException(be.getErrorCode(), be.getMessage())
>     ```
>
> -  Exception未知异常👉<u>***统一使用getMessage作为BizException的消息体 ***</u>！
>
>   -  ```java
>      log.error(">>>>xxx|xxx|xxx:{}<<<", e.getMessage(), e)
>      throw new BizException(SystemRetCodeConstants.SYSTEM_ERROR.getCode(), e.getMessage())
>      ```
>
> 2.3.6 填充响应体
>
> - <u>***total***</u>
> - <u>***result***</u>
> - <u>***code***</u>
> - <u>***msg***</u>





#### 🌵查询

##### 3.1查询均分页

- 是否分页由前端参数控制，不分页时可手动将分页参数设置至MAX

##### 3.2 查询结果放在分页对象中

- data
- total

##### 3.3 关于缓存（方案二实践）

> 方案一：缓存所有数据
>
> 方案二：缓存首页数据🚀

1. 缓存首页数据，缓存总数据条数
2. 查询请求首先落到Redis，当没有命中或Redis异常，则请求落到DB
   1. 判断是否为第一页
      1. 数据总数<=第一页数量 && 缓存记录数==数据总数，使用缓存（此时表示不适用缓存，全部展示）
      2. 分页参数每页数量和缓存中数量一致，使用缓存（此时缓存了第一页）
3. 从DB查询数据获取结果后，判断是否为第一页，是则缓存（缓存前需要清空缓存）







 

















