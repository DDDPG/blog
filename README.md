## 博客介绍

<p align=center>
  <a href="https://www.ttkwsd.top">
    <img src="https://foruda.gitee.com/avatar/1662735858745624876/7774198_wu_shengdong_1662735858.png" alt="阿冬的个人博客" style="border-radius: 50%;">
  </a>
</p>


<p align=center>
   基于Springboot + Vue 开发的前后端分离博客
</p>
<p align="center">
   <a target="_blank" href="https://gitee.com/wu_shengdong/blog">
      <img src="https://img.shields.io/hexpm/l/plug.svg"/>
      <img src="https://img.shields.io/badge/JDK-1.8+-green.svg"/>
      <img src="https://img.shields.io/badge/springboot-2.4.0-green"/>
      <img src="https://img.shields.io/badge/vue-2.6.14-green"/>
      <img src="https://img.shields.io/badge/mysql-8.0.27-green"/>
      <img src="https://img.shields.io/badge/mybatis--plus-3.4.0-green"/>
      <img src="https://img.shields.io/badge/redis-6.2.6-green"/>
      <img src="https://img.shields.io/badge/elasticsearch-7.12.1-green"/>
      <img src="https://img.shields.io/badge/rabbitmq-3.9.11-green"/>
   </a>
</p>

[在线地址](#在线地址) | [目录结构](#目录结构) | [项目特点](#项目特点) | [技术介绍](#技术介绍) | [运行环境](#运行环境) | [开发环境](#开发环境) | [项目截图](#项目截图) | [快速开始](#快速开始) | [注意事项](#注意事项) | [项目总结](#项目总结) | [交流群](#交流群)

## 在线地址

**项目链接：** [www.ttkwsd.top](https://www.ttkwsd.top)

**后台链接：** [admin.ttkwsd.top](https://admin.ttkwsd.top)

**Github地址：** [https://github.com/ICAN1999/blog](https://github.com/ICAN1999/blog)

**Gitee地址：** [https://gitee.com/wu_shengdong/blog](https://gitee.com/wu_shengdong/blog)

如果您觉得我写得不错的话，就给我个star吧，您的star就是对我的最大的鼓励。

## 目录结构

前端项目位于blog-vue下，blog为前台，system为后台。

后端项目位于blog-springboot下。

SQL文件位于根目录下的blog.sql。

ElasticSearch的mapper映射位于根目录下的mapper.json。

可直接导入该项目于本地，修改后端配置文件中的数据库等连接信息，项目中使用到的关于阿里云、腾讯云功能和第三方授权登录等需要自行开通。

当你克隆项目到本地后使用邮箱注册后修改为admin角色。

```
blog-springboot
├── annotation    --  自定义注解
├── aspect        --  aop模块
├── config        --  配置模块
├── constant      --  常量模块
├── controller    --  控制器模块
├── entity        --  实体类模块
├── enums         --  枚举模块
├── exception     --  自定义异常模块
├── filter     	  --  过滤器模块（Jwt过滤）
├── handler       --  处理器模块（Security处理、异常处理等）
├── interceptor   --  拦截器模块（Redis拦截器等）
├── listener      --  监听器模块（监听RabbitMQ、Redis）
├── manager       --  异步任务管理器模块
├── mapper        --  Mapper文件模块
├── model         --  dto、vo模块
├── quartz        --  定时任务模块
├── service       --  服务模块
├── strategy      --  策略模块（用于扩展第三方登录、搜索模式、上传文件模式、点赞模式等策略）
└── utils         --  工具类模块
```

## 项目特点

- 前台参考"Hexo"的"Shoka"设计，响应式体验好。
- 后台参考"element-admin"设计，侧边栏，历史标签，面包屑自动生成。
- 采用Markdown编辑器，写法简单。
- 点赞，取消点赞功能。
- 评论支持表情输入回复等，样式参考B站评论区。
- 邮件模板评论回复提醒，页面美观。
- 添加音乐播放器，在线听音乐。
- 前后端分离部署，适应当前潮流。
- 接入第三方登录，减少注册成本。
- 支持发布说说，随时分享趣事。
- 含有友链功能，结交更多朋友。
- 留言采用弹幕墙，更加炫酷。
- 支持代码高亮和复制，图片预览，深色模式等功能，提升用户体验。
- 搜索文章支持高亮分词，拼音搜索，响应速度快。
- 新增文章目录、最新评论、最新文章等功能，优化用户体验。
- 新增aop注解实现日志管理。  
- 后台管理支持B站图床，博客配置，Excel导出等信息，操作简单。
- 代码支持多种搜索模式（Elasticsearch或MYSQL），支持多种上传模式（OSS、COS、本地），可支持配置。
- 代码遵循阿里巴巴开发规范，利于开发者学习。

## 技术介绍

**前端：** vue + vuex + vue-router + axios + vuetify + element-ui + echarts

**后端：** SpringBoot + Quartz + Thymeleaf + Nginx + Docker + SpringSecurity + Swagger2 + MyBatisPlus + Mysql + Redis + ElasticSearch + RabbitMQ

**其他：** 接入QQ、微博、Gitee、Github第三方登录

## 运行环境

**服务器：** 腾讯云2核4G CentOS7.6

**CDN：** 阿里云全站加速

**对象存储：** 阿里云OSS、腾讯云COS

**最低配置：** 1核2G服务器（关闭ElasticSearch）

## 开发环境

| 开发工具            | 说明              |
| ------------------- | ----------------- |
| IDEA                | Java开发工具IDE   |
| VSCode              | Vue开发工具IDE    |
| Navicat             | MySQL远程连接工具 |
| RedisDesktopManager | Redis远程连接工具 |
| Xshell              | Linux远程连接工具 |
| Xftp                | Linux文件上传工具 |

| 开发环境      | 版本   |
| ------------- | ------ |
| JDK           | 1.8    |
| MySQL         | 8.0.27 |
| Redis         | 6.2.6  |
| Elasticsearch | 7.12.1 |
| RabbitMQ      | 3.9.11 |

## 项目截图

![博客首页.png](https://static.ttkwsd.top/articles/home.png)

![article.jpg](https://static.ttkwsd.top/articles/articles.png)

![friend.png](https://static.ttkwsd.top/articles/friend.png)

![admin.png](https://static.ttkwsd.top/articles/admin.png)

![comment.png](https://static.ttkwsd.top/articles/comment.png)

![picture.png](https://static.ttkwsd.top/articles/picture.png)

## 注意事项

- 项目拉下来运行后，可到后台管理页面网站配置处修改博客相关信息.
- 邮箱配置，第三方授权配置需要自己申请。
- ElasticSearch需要安装ik分词器和拼音分词器，用mapper.json来创建索引。

## 项目总结

自己花了大概三个多月的时间写完这个项目，经历各种Bug以及项目部署，总得来说太不容易了。在这里十分感谢以下项目：

[X1192176811 (风丶宇) ](https://github.com/X1192176811/blog)

**[hexo-theme-shoka](https://github.com/amehime/hexo-theme-shoka)**

[RuoYi-Vue: 🎉 基于SpringBoot，Spring Security，JWT，Vue & Element 的前后端分离权限管理系统](https://gitee.com/y_project/RuoYi-Vue)

[一个离线IP地址定位库和IP定位数据管理框架](https://github.com/lionsoul2014/ip2region)

[基于Vue.js的弹幕交互组件 | A danmaku component for Vue](https://github.com/hellodigua/vue-danmaku)

感谢以上大佬们的开源精神💖，提供这么优秀的项目🌹

## 交流群

![博客技术交流群聊二维码.png](https://static.talkxj.com/articles/1594437310326.png)