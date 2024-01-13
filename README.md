<h2 align="center">
	 WhiskeyXD's BlogLite
</h2>
<div align="center">
	<img src="https://img.shields.io/badge/JDK-11-blue"/>
    <img src="https://img.shields.io/badge/SpringBoot-2.7.14-green"/>
    <img src="https://img.shields.io/badge/Nuxt.js-3.9.1-palegreen"/>
</div>



## 简介

SpringBoot + Nuxt.js 个人自用博客系统，前后端分离(SSR + SPA)

欢迎预览：[whiskeyxd.top](https://whiskeyxd.top)



## 框架与依赖

### 后端

- **核心框架**:  SpringBoot
- **持久层**: MyBatis
- **认证与鉴权**: Spring Security  + Auth0 JWT
- **SQL**: MySQL
- **NoSQL**: Redis
- **搜索引擎**: RediSearch



### 前端

- **全栈框架**: Nuxt3
- **UI**: Element Plus
- **Markdown 样式**: [github-markdown-css](https://github.com/sindresorhus/generate-github-markdown-css)



## 部署

在先行配置好`systemd`服务的情况下， 本项目提供了bash脚本`deploy.sh`以供自动化部署：

~~~shell
chmod +x ./deploy.sh
# 通过命令行参数指定构建目标，以构建所有目标为例：
./deploy.sh -ans
~~~



### 后端部署

1. 确保部署环境安装了MySQL Server与Redis Server，JRE最低要求为Java 11

2. 如果希望启用RediSearch作为搜索引擎，那么需要手动安装其作为Redis模块，或者直接安装Redis Stack，其提供了对RediSearch 2.x的集成

   若不希望安装Redis Stack，对于Debian系Linux，可以考虑通过包管理器安装这个模块：

   ~~~shell
   sudo apt update
   sudo apt install redis-redisearch
   ~~~

   这会将RediSearch的动态链接库安装至`/usr/lib/redis/modules/redisearch.so`

   可以通过修改`redis.conf`的方式使Redis在下一次启动加载该模块，或者，在`redis-cli`手动加载：

   ~~~
   MODULE LOAD /usr/lib/redis/modules/redisearch.so
   ~~~

3. 添加`application-prod.yml`作为生产环境配置文件，保持大部分内容与`application-dev.yml`相同，此外应当修改数据库连接信息，以及JWT密钥



### 前端部署

访客端与管理端两个前端项目的部署方式没有限定，在假定使用Nginx作为网关服务器的情况下：

- 确保不要暴露额外的端口：仅暴露80与443

- 通过虚拟主机的方式让两个前端项目分别于其它端口绑定域名，并通过反向代理从80 443端口访问

- 由于不暴露后端应用服务器，以及出于免于配置CORS的考虑，fetch API请求应当同样由反向代理转发，注意反向代理会隐藏掉原始的请求源地址，应当通过自定义HTTP首部传递给后端

  

## 许可

[MIT](https://raw.githubusercontent.com/SignedWhiskeyXD/BlogLite/master/LICENSE?token=GHSAT0AAAAAACB3GGPMW7SN3PFXTUD2KL5MZILVJ6A)