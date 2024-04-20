#### Dubbo微服务分组

`创建时间：2023年10月13日 22:23:02`

---

dubbo.registry.group=dev-xx

- portal
- goods

dubbo.registry.group=default/uat

- portal
- goods-1

<br/><br/>

配合Apollo的集群使用，每一个开发者在Apollo中配置一个专属集群ID。

<br/><br/>

解决问题：

1. 避免开发小组内不同代码版本的服务造成的portal调用差异；
2. 避免调试过程中请求发送到别人的机器上😷；
3. 不需要再APOLLO中单独设置一个UAT环境 ↓
   1. 例如当前有两个开发，一个测试，在Apollo中新建一个DEV环境，环境新建三个集群（default默认，dev-user1, dev-user2）
   2. 开发使用自己的集群配置，测试使用default默认集群配置，如果开发和测试想要切换数据库，可直接在自己所在集群内的配置（Namespace）中切换。



<br/><br/>

**<u>*END.*</u>**
