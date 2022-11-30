# 在伟大航路尽头拉夫德鲁找到了这个项目



## 简介





作为一个高于入门级的`前后端分离`项目(我认为) 你可以在这个项目中学习到：

- `前后端如何交互(SpringBoot + mybatis-plus)` 
- `用户登录验证(jsonwebtoken + security)` 
- `通过websocket进行交互` 
- `匹配系统(手写消息队列/多线程)(ReentrantLock + Condition  + Thread + MultiValueMap)` 
- `锁的使用(ReentrantLock)` 
- `代码运行系统(jooq)` 
- `聊天系统(websocket)` 
- `web网页编写(vue3 + bootstrap + vuex + vueRouter + jquery)`



## 功能

```
- 登录 / 注销

- 个人中心
  - bot代码
  - 帖子模块
  - 好友模块

- 游戏界面

- 排行榜

- 对战列表
  - 对局回放

- 聊天系统
  - 群聊
  - 公屏
  - 找人建群

- 匹配系

- 运行代码系统 
```



## 前序准备

你需要在本地安装 [node](http://nodejs.org/) 和 [git](https://git-scm.com/)。本项目技术栈基于 `前端`：[vue](https://cn.vuejs.org/index.html)、[vuex](https://vuex.vuejs.org/zh-cn/)、[vue-router](https://router.vuejs.org/zh-cn/) 、[vue-cli](https://github.com/vuejs/vue-cli) 、[axios](https://github.com/axios/axios) 和 [element-ui](https://github.com/ElemeFE/element)，`后端`使用`idea`开发: java 、springboot 、SpringMVC、springCloud等 ，`数据库`：mysql 提前了解和学习这些知识会对使用本项目有很大的帮助。



## 开发

```sh
# 克隆项目
git clone https://git.acwing.com/ourhome/kob.git

# 进入项目目录
cd web

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
vue ui (如果报错用cmd启动)

# 后端
下载安装Lombok插件
修改application.properties 中的配置

# 数据库
create database kob;
source kob_ddl.sql
```

浏览器访问 [http://localhost:8080](http://localhost:8080/)



## 其他

由于个人水平有限，有建议欢迎留言

尚未上线、敬请期待...
