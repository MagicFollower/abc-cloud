时间：2023年05月01日 14:55:09

---

#### 一、当前目录

1. 从零开始封装一个自己的系统模块；
2. 完美的maven继承关系，使用命令直接统一更新所有系统模块版本；
3. 实用且简介的系统模块封装（面向WE后端）。



#### 二、系统平台版本号迭代方案

> A.B.C.D

- A：模块上线年数（建议从2开始）
- B：新功能上线
- C：小功能上线/bug修复
- D：开发版本号（<u>该版本不对外开放，平台内部开发测试使用</u>）



#### 三、系统微服务设计规范

##### 3.1 规范一

- Maven项目，父级pom下包含两个子模块（api、service）
  - api用于推送到远程仓库，供其他微服务进行RPC（远程过程调用（Remote Procedure Call））
  - ![Maven项目，父级pom下包含两个子模块（api、service）](https://github.com/MagicFollower/abc-cloud/blob/main/abc/img/SpringBoot-Maven%E5%BE%AE%E6%9C%8D%E5%8A%A1%E9%A1%B9%E7%9B%AE%E7%BB%93%E6%9E%84.png)

##### 3.2 规范二

-  接`规范一`，关于三个项目的版本号问题
  -  ![SpringBoot-Maven微服务版本号与继承关系-1](https://github.com/MagicFollower/abc-cloud/blob/main/abc/img/SpringBoot-Maven微服务版本号与继承关系-1.png)
  -  ![SpringBoot-Maven微服务版本号与继承关系-2](https://github.com/MagicFollower/abc-cloud/blob/main/abc/img/SpringBoot-Maven微服务版本号与继承关系-2.png)
- 统一更新版本号
  - `mvn versions:set -DgenerateBackupPoms=false -DautoVersionSubmodules=true`













<br/><br/><br/><br/>

***<u>END.</u>***
