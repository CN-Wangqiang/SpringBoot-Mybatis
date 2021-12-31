### 项目介绍

SpringBoot整合Mybatis实现员工管理CRUD案例，加入了数据库，前端添加了日期选项的控件。

### 技术工具

项目后端采用SpringBoot构建，数据库使用的是MySQL，前端采用简单html,css和javascript渲染页面。涉及到的其他技术工具有MyBatis，maven，lombok。需要熟练掌握MySQL数据库，SpringBoot及MyBatis知识，简单的前端知识；

### 开发环境

- IDEA,DateGrip
- JDK8
- MySQL
- Maven

### 如何运行

1、git clone git@github.com:ID-Wangqiang/SpringBoot-Mybatis.git  
2、启动本地MySQL ，新增项目需要的数据库

```sql
CREATE DATABASE springboot;
```

![image-20211231112619160](https://cdn.jsdelivr.net/gh/ID-Wangqiang/MyBlogImg/img/202112311126213.png)

3. 在这个数据库下执行项目中的springboot.sql文件

![image-20211231112753442](https://cdn.jsdelivr.net/gh/ID-Wangqiang/MyBlogImg/img/202112311127491.png)

执行成功后如图所示

![image-20211231112952133](https://cdn.jsdelivr.net/gh/ID-Wangqiang/MyBlogImg/img/202112311129172.png)

3. 用IDEA打开项目，查看application.properties文件，将数据库连接账户密码设置为本地账户密码

![image-20211231113137102](https://cdn.jsdelivr.net/gh/ID-Wangqiang/MyBlogImg/img/202112311131158.png)

3. 运行SpringbootCrudApplication文件

![image-20211231113458886](https://cdn.jsdelivr.net/gh/ID-Wangqiang/MyBlogImg/img/202112311134952.png)

3. 浏览器输入http://localhost:8080 ，测试项目基本功能

![image-20211231113653174](https://cdn.jsdelivr.net/gh/ID-Wangqiang/MyBlogImg/img/202112311136222.png)

> 用户名和密码在user表中，默认（用户名：admin，密码：123456）

简易员工管理界面

![image-20211231113946228](https://cdn.jsdelivr.net/gh/ID-Wangqiang/MyBlogImg/img/202112311139289.png)

### 参考资料

[狂神说Java](https://www.bilibili.com/video/BV17a4y1x7zq)  