### 背景

> 在日常开发中, 伴随着许许多多的文档
>
> 业务文档, 需求文档, 开发文档 等等
>
> 随着业务范围逐渐扩大, 项目逐渐增多, 数据库逐渐开始变得难以管理
>
> 所以数据库文档也开始变得重要起来

### 目标

> 整合现有数据库(mysql/clickhouse), 做好文档规范
>
> 新人辅导向, 该项目可以达到更直观的数据库关系查看
>
> 方便开发人员的定期review

### 首先需要一个mysql数据库,并配置对应的环境变量,环境变量含义可见: https://hub.docker.com/repository/docker/wangxiaowu950330/behappy-screw-doc

### 部署方式

- Docker-compose部署, 可以直接使用docker-compose文件部署(先修改环境变量)

```
docker-compose up -d
```

- Docker部署

```
# docker run -d --name behappy-screw-doc --privileged=true --restart=no -p 8999:8080 -v /opt/dbdoc/doc:/user/src/app/doc wangxiaowu950330/behappy-screw-doc:latest
```

### 使用方式

```text
大体支持两种角色,一种ROLE_ADMIN.另一种是ROLE_XXX
ADMIN用于管理数据库,用户和角色信息
XXX为普通角色,用于查看所分配数据库信息
```

#### 先配置数据源信息

![img.png](resources/image/img_1.png)

#### 配置数据库信息

![img.png](resources/image/img_2.png)

#### 创建数据库/同步数据库

![img.png](resources/image/img_3.png)

#### 对应角色分配数据库

![img.png](resources/image/img_4.png)

#### admin账户执行初始化文档

![img.png](resources/image/img_5.png)

### 历史回溯功能

此功能用于查询当前数据库的更新迭代信息

![img.png](resources/image/img_6.png)

![image.png](resources/image/img_7.png)

### 访问

```
http://xxx:8999/
账户: admin
密码: admin
```

- [X]  mysql文档管理
- [X]  角色划分
- [X]  clickhouse文档管理
- [X]  redis缓存(适用在分布式环境部署)和caffeine进程缓存(适用在单机)两种方式
- [X]  容器化
- [X]  以/doc开头的访问地址解析token,并判断当前角色是否允许访问
- [X]  数据更改后,可进行历史回溯
- [X]  手动初始化数据库数据改为自动初始化

### 注: 当前项目支持两种数据库, mysql和clickhouse, 但clickhouse因为screw原作者还未合代码, 所以如果是需要本地开发的话, 需要手动执行下此命令将jar包安装到本地库

```xml
参考: https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html

mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile="lib/screw-core-1.0.6-SNAPSHOT.jar" -DpomFile="lib/screw-1.0.6-SNAPSHOT.pom"
cp lib/screw-core-1.0.6-SNAPSHOT.pom ~/.m2/repository/cn/smallbun/screw/screw/1.0.6-SNAPSHOT/screw-1.0.6-SNAPSHOT.pom
mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile="lib/screw-core-1.0.6-SNAPSHOT.jar" -DpomFile="lib/screw-core-1.0.6-SNAPSHOT.pom"
```
