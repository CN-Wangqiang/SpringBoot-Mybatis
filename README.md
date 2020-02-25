# SpringBoot-CRUD
SpringBoot整合CRUD实现员工管理案例，将Mybatis整合到原项目中，加入了数据库，添加了日期选项的控件。
#### 环境要求

- IDEA
- MySQL 
- Maven  
- 需要熟练掌握MySQL数据库，SpringBoot及MyBatis知识，简单的前端知识；

#### 数据库环境

创建案例所使用的数据库

```sql
CREATE DATABASE `springboot`;

USE `springboot`;
```

创建登陆用户数据表

```sql
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) NOT NULL,
  `user_name` varchar(255)  NOT NULL COMMENT '用户名',
  `password` varchar(255)  NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

INSERT INTO `user` VALUES (1, 'admin', '123456');

```



创建部门信息的数据库表

```sql
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(10) NOT NULL,
  `department_name` varchar(255)  NOT NULL COMMENT '部门名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `department` VALUES (1, '市场部');
INSERT INTO `department` VALUES (2, '技术部');
INSERT INTO `department` VALUES (3, '销售部');
INSERT INTO `department` VALUES (4, '客服部');
INSERT INTO `department` VALUES (5, '公关部');
COMMIT;
```



创建存放员工信息的数据库表

```sql
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `employee_name` varchar(255)  NOT NULL COMMENT '员工姓名',
  `email` varchar(255)  NOT NULL COMMENT '员工邮箱',
  `gender` int(2) NOT NULL COMMENT '员工性别',
  `department_id` int(10) NOT NULL COMMENT '部门编号',
  `date` date NOT NULL COMMENT '入职日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `employee` VALUES (1, '张三', 'zhangsan@gmail.com', 0, 1, '2020-02-12');
INSERT INTO `employee` VALUES (2, '李四', 'lisi@qq.com', 1, 2, '2020-02-05');
INSERT INTO `employee` VALUES (3, '王五', 'wangwu@126.com', 0, 3, '2020-02-15');
INSERT INTO `employee` VALUES (4, '赵六', 'zhaoliu@163.com', 1, 4, '2020-02-21');
INSERT INTO `employee` VALUES (5, '田七', 'tianqi@foxmail.com', 0, 3, '2020-02-14');
INSERT INTO `employee` VALUES (10, '王伟', 'wangwei@gmail.com', 1, 3, '2020-02-08');
INSERT INTO `employee` VALUES (11, '张伟', 'zhangwei@gmail.com', 1, 2, '2020-02-11');
INSERT INTO `employee` VALUES (12, '李伟', 'liwei@gmail.com', 1, 3, '2020-02-18');
COMMIT;
```

#### 基本环境搭建

1. 新建Spring项目， 添加Lombok，Spring Web，Thymeleaf，Mybatis，MySQL Driver的支持
2. 相关的pom依赖

```xml
      <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.1</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
```

3. 建立基本结构和配置框架

- com.wangqiang.pojo
- com.wangqiang.dto
- com.wangqiang.mapper
- com.wangqiang.service   
- com.wangqiang.config

4. application.properties里配置数据库连接信息及Mapper映射文件信息

```properties
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.url=jdbc:mysql://localhost:3306/springboot?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
mybatis.type-aliases-package=com.wangqiang.pojo
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.configuration.map-underscore-to-camel-case=true
spring.messages.basename=i18n.login

```

5. 测试数据库连接

```java
package com.wangqiang;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class SpringbootCrudApplicationTests {

    @Autowired
    DataSource dataSource;

    @Test
    public void contextLoads() throws SQLException {
        System.out.println("数据源>>>>>>" + dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("连接>>>>>>>>>" + connection);
        System.out.println("连接地址>>>>>" + connection.getMetaData().getURL());
        connection.close();
    }

}
```

**查看输出结果，数据库配置ok**

#### 创建pojo实体类

1. 创建User实体

```java
package com.wangqiang.pojo;
import lombok.Data;

@Data
public class User {
    private int id;
    private String userName;
    private String password;
}

```

2. 创建Department实体

```java
package com.wangqiang.pojo;
import lombok.Data;

@Data
public class Department {
    private int id;
    private String departmentName;
}
```

3. 创建Employee实体

```java
package com.wangqiang.pojo;
import lombok.Data;
import java.sql.Date;

@Data
public class Employee {
    private int id;
    private String employeeName;
    private String email;
    private int gender;
    private int departmentId;
    private Date date;
}
```

4. 创建EmployeeDTO实体

```java
package com.wangqiang.dto;
import lombok.Data;
import java.sql.Date;

@Data
public class EmployeeDTO {
    private int id;
    private String employeeName;
    private String email;
    private int gender;
    private String departmentName;
    private Date date;
}
```

#### Mapper层

文件存放目录：

com.wangqiang.mapper 相关接口

resources/mapper 相关mapper.xml

1. 编写User的Mapper接口：UserMapper

```java
package com.wangqiang.mapper;
import com.wangqiang.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    User selectPasswordByName(@Param("userName") String userName,@Param("password") String password);
}

```

2. 编写接口对应的Mapper.xml文件：UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.wangqiang.mapper.UserMapper">
    <select id="selectPasswordByName" resultType="User">
    select * from user where user_name = #{userName} and password = #{password}
</select>
</mapper>
```

3. 编写Department的Mapper接口：DepaertmentMapper

```java
package com.wangqiang.mapper;
import com.wangqiang.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface DepartmentMapper {
    List<Department> selectAllDepartment();
}
```

4. 编写接口对应的Mapper.xml文件：DepaertmentMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.wangqiang.mapper.DepartmentMapper">
    <select id="selectAllDepartment" resultType="Department">
    select * from department
  </select>
</mapper>
```

5. 编写Employee的Mapper接口：EmployeeMapper

```java
package com.wangqiang.mapper;
import com.wangqiang.dto.EmployeeDTO;
import com.wangqiang.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface EmployeeMapper {
    //查询全部员工信息
    List<EmployeeDTO> selectAllEmployeeDTO();
    //根据id查询员工信息
    Employee selectEmployeeById(int id);
    //添加一个员工信息
    int addEmployee(Employee employee);
    //修改一个员工信息
    int updateEmployee(Employee employee);
    //根据id删除员工信息
    int deleteEmployee(int id);
}
```

6. 编写接口对应的Mapper.xml文件：EmployeeMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.wangqiang.mapper.EmployeeMapper">

    <resultMap id="EmployeeDTO" type="com.wangqiang.dto.EmployeeDTO">
        <id column="id" jdbcType="INTEGER" property="id" />
        <result column="employee_name" jdbcType="VARCHAR" property="employeeName" />
        <result column="email" jdbcType="VARCHAR" property="email" />
        <result column="gender" jdbcType="INTEGER" property="gender" />
        <result column="department_name" jdbcType="VARCHAR" property="departmentName" />
        <result column="date" jdbcType="DATE" property="date" />
    </resultMap>

    <select id="selectAllEmployeeDTO"  resultMap="EmployeeDTO">
        select e.id,e.employee_name,e.email,e.gender,d.department_name,e.date
        from employee e,department d
        where e.department_id = d.id
    </select>

    <select id="selectEmployeeById" resultType="Employee">
    	select * from employee where id = #{id}
		</select>

    <insert id="addEmployee" parameterType="Employee">
    		insert into employee (id,employee_name,email,gender,department_id,date)
    	values (#{id},#{employeeName},#{email},#{gender},#{departmentId},#{date})
		</insert>

    <update id="updateEmployee" parameterType="Employee">
    update employee
    		set employee_name=#{employeeName},email=#{email} ,gender=#{gender} ,department_id=#{departmentId} ,date=#{date}
    where id = #{id}
</update>

    <delete id="deleteEmployee" parameterType="int">
    		delete from employee where id = #{id}
		</delete>
</mapper>
```

#### Service层

com.wangqiang.service 

1. EmployeeService接口：

```java
package com.wangqiang.service;
import com.wangqiang.dto.EmployeeDTO;
import com.wangqiang.pojo.Employee;
import java.util.List;

public interface EmployeeService {
    //查询全部员工信息
    List<EmployeeDTO> selectAllEmployeeDTO();
    //根据id查询员工信息
    Employee selectEmployeeById(int id);
    //添加一个员工信息
    int addEmployee(Employee employee);
    //修改一个员工信息
    int updateEmployee(Employee employee);
    //根据id删除员工信息
    int deleteEmployee(int id);
}
```

2. EmployeeServiceImpl实现类：

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeDTO> selectAllEmployeeDTO() {
        return employeeMapper.selectAllEmployeeDTO();
    }

    @Override
    public Employee selectEmployeeById(int id) {
        return employeeMapper.selectEmployeeById(id);
    }

    @Override
    public int addEmployee(Employee employee) {
        return employeeMapper.addEmployee(employee);
    }

    @Override
    public int updateEmployee(Employee employee) {
        return employeeMapper.updateEmployee(employee);
    }

    @Override
    public int deleteEmployee(int id) {
        return employeeMapper.deleteEmployee(id);
    }
}
```

3. DepartmentService接口

```
package com.wangqiang.service;
import com.wangqiang.pojo.Department;
import java.util.List;

public interface DepartmentService {
    List<Department> selectAllDepartment();
}
```

3. DepartmentImpl实现类：

```java
package com.wangqiang.service;
import com.wangqiang.mapper.DepartmentMapper;
import com.wangqiang.pojo.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> selectAllDepartment() {
        return departmentMapper.selectAllDepartment();
    }
}
```

4. UserService接口

```java
package com.wangqiang.service;
import com.wangqiang.pojo.User;

public interface UserService {
    User selectPasswordByName(String userName,String password);
}
```

5. UserServiceImpl实现类

```java
package com.wangqiang.service;
import com.wangqiang.mapper.UserMapper;
import com.wangqiang.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectPasswordByName(String userName,String password) {
        return userMapper.selectPasswordByName(userName,password);
    }
}
```

6. **测试获取数据情况**

```java
    @Autowired
    EmployeeService employeeService;
    
		@Test
    public void test(){
        List<EmployeeDTO> employees = employeeService.selectAllEmployeeDTO();
        for (EmployeeDTO employee : employees) {
            System.out.println(employee);
        }
    }
    @Test
    public void test2(){
        Employee employee = employeeService.selectEmployeeById(1);
        System.out.println(employee);
        //Employee(id=1, employeeName=张三, email=zhangsan@gmail.com, gender=0, departmentId=1, date=2020-02-12)
    }

    @Test
    public void test3(){
        Employee employee = new Employee();
        employee.setId(6);
        employee.setEmployeeName("test");
        employee.setEmail("123@qq.com");
        employee.setDepartmentId(2);
        Date date = new Date(2020-02-02);
        employee.setDate(date);
        employeeService.addEmployee(employee);
        Employee employee1 = employeeService.selectEmployeeById(6);
        System.out.println(employee1);
        //Employee(id=6, employeeName=test, email=123@qq.com, gender=0, departmentId=2, date=1970-01-01)
    }

    @Test
    public void test4(){
        Employee employee = new Employee();
        employee.setId(6);
        employee.setEmployeeName("test");
        employee.setEmail("123@qq.com");
        employee.setDepartmentId(3);
        Date date = new Date(2020-02-02);
        employee.setDate(date);
        employeeService.updateEmployee(employee);
        Employee employee1 = employeeService.selectEmployeeById(6);
        System.out.println(employee1);
        //Employee(id=6, employeeName=test, email=123@qq.com, gender=0, departmentId=3, date=1970-01-01)
    }

    @Test
    public void test05(){
        employeeService.deleteEmployee(6);
    }

    @Autowired
    private UserService userService;
    @Test
    public void test06(){
        User admin = userService.selectPasswordByName("admin","123456");
        System.out.println(admin);
        //User(id=1, name=admin, password=123456)
    }

    @Autowired
    private DepartmentService departmentService;
    @Test
    public void test07(){
        List<Department> departments = departmentService.selectAllDepartment();
        for (Department department : departments) {
            System.out.println(department);
        }
    }
```



#### Controller层  

1. 登陆页LoginController

```java
package com.wangqiang.controller;
import com.wangqiang.pojo.User;
import com.wangqiang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(@RequestParam("username")String username,
                        @RequestParam("password")String password,
                        HttpSession session,
                        Model model){
        User user = userService.selectPasswordByName(username, password);
        if ( user != null){
            //登录成功！
            session.setAttribute("username",user.getUserName());
            //登录成功！防止表单重复提交，我们重定向
            return "redirect:/main.html";
        }else {
            //登录失败！存放错误信息
            model.addAttribute("msg","用户名或密码错误");
            return "index";
        }
    }

    @GetMapping("/user/loginOut")
    public String loginOut(HttpSession session){
        session.invalidate();
        return "redirect:/index.html";
    }
}
```

2. 员工信息Controller

```java
package com.wangqiang.controller;
import com.wangqiang.dto.EmployeeDTO;
import com.wangqiang.pojo.Department;
import com.wangqiang.pojo.Employee;
import com.wangqiang.service.DepartmentService;
import com.wangqiang.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Collection;


@Controller
public class EmploeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    //查询所有员工，返回列表页面
    @GetMapping("/emp")
    public String list(Model model){
        Collection<EmployeeDTO> employees = employeeService.selectAllEmployeeDTO();
//        将结果放在请求中
        model.addAttribute("emps",employees);
        return "emp/list.html";
    }

    //to员工添加页面
    @GetMapping("/add")
    public String toAdd(Model model){
        //查出所有的部门，提供选择
        Collection<Department> departments = departmentService.selectAllDepartment();
        model.addAttribute("departments",departments);
        return "emp/add.html";
    }

    //员工添加功能，使用post接收
    @PostMapping("/add")
    public String add(Employee employee){
        //保存员工信息
        employeeService.addEmployee(employee);
        //回到员工列表页面，可以使用redirect或者forward
        return "redirect:/emp";
    }

    //to员工修改页面
    @GetMapping("/emp/{id}")
    public String toUpdateEmp(@PathVariable("id") Integer id, Model model){
        //根据id查出来员工
        Employee employee = employeeService.selectEmployeeById(id);
        //将员工信息返回页面
        model.addAttribute("emp",employee);
        //查出所有的部门，提供修改选择
        Collection<Department> departments = departmentService.selectAllDepartment();
        model.addAttribute("departments",departments);
        return "emp/update.html";
    }

    @PostMapping("/updateEmp")
    public String updateEmp(Employee employee){
        employeeService.updateEmployee(employee);
        //回到员工列表页面
        return "redirect:/emp";
    }

    @GetMapping("/delEmp/{id}")
    public String deleteEmp(@PathVariable("id")Integer id){
        //根据id删除员工
        employeeService.deleteEmployee(id);
        return "redirect:/emp";
    }
}
```

#### 完善Config文件

1. 编写Interceptor拦截器配置

```java
package com.wangqiang.config;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object username = request.getSession().getAttribute("username");
        if (username == null){//未登录，返回登录页面
            request.setAttribute("msg","没有权限");
            request.getRequestDispatcher("/index.html").forward(request,response);
            return false;
        }else {
            //登录，放行
            return true;
        }
    }
}
```

2. 编写国际化配置文件

```java
package com.wangqiang.config;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class MyLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String language = httpServletRequest.getParameter("l");
        Locale locale = Locale.getDefault();
        if (!StringUtils.isEmpty(language)){
            String[] split = language.split("_");
            locale = new Locale(split[0],split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
```

3. 编写WebMvc文件，将上述配置到MvcConfiguration中

```java
package com.wangqiang.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器，及拦截请求和要剔除哪些请求!
        //我们还需要过滤静态资源文件，否则样式显示不出来
        registry.addInterceptor(new MyHandlerInterceptor())
                .addPathPatterns("/**")
       .excludePathPatterns("/","/index.html","/login","/css/**","/js/**","/img/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/main.html").setViewName("main");
    }

    @Bean
    public LocaleResolver localeResolver(){
      //国际化相关配置
        return new MyLocaleResolver();
    }
}
```

#### 前端视图

1. 登陆页index.html

```html
<!DOCTYPE html>
<html lang="en"  xmlns:th="http://www.thymeleaf.org">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<meta name="description" content="">
		<meta name="author" content="">
		<title>Signin Template for Bootstrap</title>
		<!-- Bootstrap core CSS -->
		<link th:href="@{/css/bootstrap.min.css}" rel="stylesheet">
		<!-- Custom styles for this template -->
		<link th:href="@{/css/signin.css}" rel="stylesheet">
	</head>

	<body class="text-center">
		<form class="form-signin" th:action="@{/login}">
			<img class="mb-4" th:src="@{/img/bootstrap-solid.svg}" alt="" width="72" height="72">
			<h1 class="h3 mb-3 font-weight-normal" th:text="#{login.tip}">Please sign in</h1>
			<input type="text" name="username" class="form-control" th:placeholder="#{login.username}" required="" autofocus="">
			<input type="password" name="password" class="form-control" th:placeholder="#{login.password}" required="">
			<!--判断是否显示，使用if, ${}可以使用工具类，可以看thymeleaf的中文文档-->
			<p style="color: red" th:text="${msg}" th:if="${not #strings.isEmpty(msg)}"></p>
			<div class="checkbox mb-3">
				<label>
          <input type="checkbox" value="remember-me" > [[#{login.remember}]]
        </label>
			</div>
			<button class="btn btn-lg btn-primary btn-block" type="submit">[[#{login.btn}]]</button>
			<p class="mt-5 mb-3 text-muted">© 2017-2018</p>
			<a class="btn btn-sm" th:href="@{/index.html(l=zh_CN)}">中文</a>
			<a class="btn btn-sm" th:href="@{/index.html(l=en_US)}">English</a>
		</form>
	</body>
</html>
```

2. 系统管理页 main.html

```html
<!DOCTYPE html>
<!-- saved from url=(0052)http://getbootstrap.com/docs/4.0/examples/dashboard/ -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<meta name="description" content="">
		<meta name="author" content="">

		<title>Dashboard Template for Bootstrap</title>
		<!-- Bootstrap core CSS -->
		<link th:href="@{/css/bootstrap.min.css}" rel="stylesheet">

		<!-- Custom styles for this template -->
		<link th:href="@{/css/dashboard.css}" rel="stylesheet">
		<style type="text/css">
			/* Chart.js */

			@-webkit-keyframes chartjs-render-animation {
				from {
					opacity: 0.99
				}
				to {
					opacity: 1
				}
			}
			
			@keyframes chartjs-render-animation {
				from {
					opacity: 0.99
				}
				to {
					opacity: 1
				}
			}
			
			.chartjs-render-monitor {
				-webkit-animation: chartjs-render-animation 0.001s;
				animation: chartjs-render-animation 0.001s;
			}
		</style>
	</head>

	<body>
	<div th:replace="~{common/commons::topbar}"></div>


		<div class="container-fluid">
			<div class="row">
				<!--引入抽取的topbar-->
				<!--模板名 ： 会使用thymeleaf的前后缀配置规则进行解析
                使用~{模板::标签名}-->
				<div th:replace="~{common/commons::sidebar(active='main.html')}"></div>
				
				<main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
					<div class="chartjs-size-monitor" style="position: absolute; left: 0px; top: 0px; right: 0px; bottom: 0px; overflow: hidden; pointer-events: none; visibility: hidden; z-index: -1;">
						<div class="chartjs-size-monitor-expand" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;">
							<div style="position:absolute;width:1000000px;height:1000000px;left:0;top:0"></div>
						</div>
						<div class="chartjs-size-monitor-shrink" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;">
							<div style="position:absolute;width:200%;height:200%;left:0; top:0"></div>
						</div>
					</div>
					<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
						<h1 class="h2">Dashboard</h1>
						<div class="btn-toolbar mb-2 mb-md-0">
							<div class="btn-group mr-2">
								<button class="btn btn-sm btn-outline-secondary">Share</button>
								<button class="btn btn-sm btn-outline-secondary">Export</button>
							</div>
							<button class="btn btn-sm btn-outline-secondary dropdown-toggle">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-calendar"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>
                This week
              </button>
						</div>
					</div>
					<canvas class="my-4 chartjs-render-monitor" id="myChart" width="1076" height="454" style="display: block; width: 1076px; height: 454px;"></canvas>
				</main>
			</div>
		</div>

		<!-- Bootstrap core JavaScript
    ================================================== -->
		<!-- Placed at the end of the document so the pages load faster -->
		<script type="text/javascript" src="asserts/js/jquery-3.2.1.slim.min.js" ></script>
		<script type="text/javascript" src="asserts/js/popper.min.js" ></script>
		<script type="text/javascript" src="asserts/js/bootstrap.min.js" ></script>
	</body>
</html>
```

3. 公共页 /common/commons.html

```html
<!DOCTYPE html >
<html lang="en" xmlns:th="http://www.thymeleaf.org">

<nav class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0" th:fragment="topbar">
    <!--后台主页显示登录用户的信息-->
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">[[${session.username}]]</a>
    <input class="form-control form-control-dark w-100" type="text" placeholder="Search" aria-label="Search">
    <ul class="navbar-nav px-3">
        <li class="nav-item text-nowrap">
            <a class="nav-link" href="#" th:href="@{/user/loginOut}">Sign out</a>
        </li>
    </ul>
</nav>

<nav class="col-md-2 d-none d-md-block bg-light sidebar" th:fragment="sidebar">
    <div class="sidebar-sticky">
        <ul class="nav flex-column">
            <li class="nav-item">
                <a th:class="${active} == 'main.html'?'nav-link active':'nav-link'" th:href="@{/main.html}">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-home">
                        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
                        <polyline points="9 22 9 12 15 12 15 22"></polyline>
                    </svg>
                    首页 <span class="sr-only">(current)</span>
                </a>

            <li class="nav-item">
                <a th:class="${active} == 'list.html'?'nav-link active':'nav-link'" th:href="@{/emp}">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-users">
                        <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                        <circle cx="9" cy="7" r="4"></circle>
                        <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
                        <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
                    </svg>
                    员工管理
                </a>
            </li>
        </ul>
    </div>
</nav>

</html>
```

3. 员工详情页 /emp/list.html

```html
<!DOCTYPE html>
<!-- saved from url=(0052)http://getbootstrap.com/docs/4.0/examples/dashboard/ -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<meta name="description" content="">
		<meta name="author" content="">

		<title>Dashboard Template for Bootstrap</title>
		<!-- Bootstrap core CSS -->
		<link th:href="@{/css/bootstrap.min.css}" rel="stylesheet">

		<!-- Custom styles for this template -->
		<link th:href="@{/css/dashboard.css}" rel="stylesheet">
		<style type="text/css">
			
			@-webkit-keyframes chartjs-render-animation {
				from {
					opacity: 0.99
				}
				to {
					opacity: 1
				}
			}
			
			@keyframes chartjs-render-animation {
				from {
					opacity: 0.99
				}
				to {
					opacity: 1
				}
			}
			
			.chartjs-render-monitor {
				-webkit-animation: chartjs-render-animation 0.001s;
				animation: chartjs-render-animation 0.001s;
			}
		</style>
	</head>

	<body>
		<div th:replace="~{common/commons::topbar}"></div>

		<div class="container-fluid">
			<div class="row">
				<div th:replace="~{common/commons::sidebar(active='list.html')}"></div>

				<main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
					<h2>员工管理</h2>
					<!--添加员工按钮-->
					<a class="btn btn-sm btn-primary" th:href="@{/add}">添加</a>

					<div class="table-responsive">
						<table class="table table-striped table-sm">
							<thead>
								<tr>
									<th>序号</th>
									<th>姓名</th>
									<th>邮箱</th>
									<th>性别</th>
									<th>部门</th>
									<th>入职日期</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<tr th:each="emp:${emps}">
									<td th:text="${emp.getId()}"></td>
									<td th:text="${emp.getEmployeeName()}"></td>
									<td th:text="${emp.getEmail()}"></td>
									<td th:text="${emp.getGender() == 0 ? '女':'男'}"></td>
									<td th:text="${emp.getDepartmentName()}"></td>
									<!--使用时间格式化工具-->
									<td th:text="${#dates.format(emp.getDate(),'yyyy-MM-dd')}"></td>
									<td >

										<a class="btn btn-sm btn-primary" th:href="@{/emp/}+${emp.id}">编辑</a>
										<a class="btn btn-sm btn-danger" th:href="@{/delEmp/}+${emp.id}">删除</a>
									</td>
								</tr>

							</tbody>
						</table>
					</div>
				</main>
			</div>
		</div>
		<!-- Bootstrap core JavaScript
    ================================================== -->
		<!-- Placed at the end of the document so the pages load faster -->
		<script type="text/javascript" src="asserts/js/jquery-3.2.1.slim.min.js"></script>
		<script type="text/javascript" src="asserts/js/popper.min.js"></script>
		<script type="text/javascript" src="asserts/js/bootstrap.min.js"></script>
	
	</body>
</html>
```

4. 添加员工页 /emp/add.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<meta name="description" content="">
		<meta name="author" content="">
		<title>Dashboard Template for Bootstrap</title>
		<!-- Bootstrap core CSS -->
		<link th:href="@{/css/bootstrap.min.css}" rel="stylesheet">
		<!-- Custom styles for this template -->
		<link th:href="@{/css/dashboard.css}" rel="stylesheet">


		<style type="text/css">
			/* Chart.js */
			
			@-webkit-keyframes chartjs-render-animation {
				from {
					opacity: 0.99
				}
				to {
					opacity: 1
				}
			}
			
			@keyframes chartjs-render-animation {
				from {
					opacity: 0.99
				}
				to {
					opacity: 1
				}
			}
			
			.chartjs-render-monitor {
				-webkit-animation: chartjs-render-animation 0.001s;
				animation: chartjs-render-animation 0.001s;
			}
		</style>
	</head>

	<body>
		<div th:replace="~{common/commons::topbar}"></div>

		<div class="container-fluid">
			<div class="row">
				<div th:replace="~{common/commons::sidebar(active='list.html')}"></div>

				<main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
					<h2>添加员工信息</h2>
					<form th:action="@{/add}" method="post">
						<div class="form-group">
							<label for="InputName">姓名</label>
							<input name="employeeName" type="text" class="form-control" id="InputName" placeholder="张三" >

						</div>
						<div class="form-group">
							<label for="InputEmail1">邮箱</label>
							<input name="email" type="email" class="form-control" id="InputEmail1" placeholder="123@123.com">
						</div>
						<div class="form-group">
						<label >性别</label>
							<div class="form-check form-check-inline" >
								<input class="form-check-input" type="radio" name="gender"  value="1">
								<label class="form-check-label">男</label>
							</div>
							<div class="form-check form-check-inline" >
								<input class="form-check-input" type="radio" name="gender"  value="0">
								<label class="form-check-label">女</label>
							</div>
					</div>
						<div class="form-group">
							<label>部门</label>
							<!--提交的是部门的ID-->
							<select class="form-control" name="departmentId">
								<option>请选择</option>
								<option  th:each="dept:${departments}" th:text="${dept.departmentName}" th:value="${dept.id}">1</option>
							</select>
						</div>
						<div class="form-group">
							<label>入职日期</label>
							<input name="date" type="text"  class="form-control" id="dateFormat" autocomplete="off">
						</div>

						<button type="submit" class="btn btn-primary">提交</button>
					</form>
				</main>
			</div>
		</div>

		<!-- Bootstrap core JavaScript
    ================================================== -->
		<!-- Placed at the end of the document so the pages load faster -->
		<script type="text/javascript" src="asserts/js/jquery-3.2.1.slim.min.js"></script>
		<script type="text/javascript" src="asserts/js/popper.min.js"></script>
		<script type="text/javascript" src="asserts/js/bootstrap.min.js"></script>
		
<!--		日期组件-->
		<script type="text/javascript" src="/laydate/laydate.js"></script>
		<!-- 改成你的路径 -->
		<script>
			//执行一个laydate实例
			laydate.render({
				elem: '#dateFormat' ,
				trigger:'click'//指定元素
			});
		</script>
	</body>

</html>
```

5. 修改员工页 /emp/add.html

```html
<!DOCTYPE html>
<!-- saved from url=(0052)http://getbootstrap.com/docs/4.0/examples/dashboard/ -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<meta name="description" content="">
		<meta name="author" content="">

		<title>Dashboard Template for Bootstrap</title>
		<!-- Bootstrap core CSS -->
		<link th:href="@{/css/bootstrap.min.css}" rel="stylesheet">

		<!-- Custom styles for this template -->
		<link th:href="@{/css/dashboard.css}" rel="stylesheet">
		<style type="text/css">
			/* Chart.js */
			
			@-webkit-keyframes chartjs-render-animation {
				from {
					opacity: 0.99
				}
				to {
					opacity: 1
				}
			}
			
			@keyframes chartjs-render-animation {
				from {
					opacity: 0.99
				}
				to {
					opacity: 1
				}
			}
			
			.chartjs-render-monitor {
				-webkit-animation: chartjs-render-animation 0.001s;
				animation: chartjs-render-animation 0.001s;
			}
		</style>
	</head>

	<body>
		<div th:replace="~{common/commons::topbar}"></div>

		<div class="container-fluid">
			<div class="row">
				<div th:replace="~{common/commons::sidebar(active='list.html')}"></div>

				<main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
					<h2>修改员工信息</h2>
					<form th:action="@{/updateEmp}" method="post" >
						<input name="id" type="hidden" class="form-control" th:value="${emp.id}">
						<div class="form-group">
							<label>姓名</label>
							<input name="employeeName" type="text" class="form-control " th:value="${emp.employeeName}" >
						</div>
						<div class="form-group">
							<label>邮箱</label>
							<input name="email" type="email" class="form-control" th:value="${emp.email}">
						</div>
						<div class="form-group">
							<label>性别</label><br/>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="radio" name="gender" value="1"
									   th:checked="${emp.gender==1}">
								<label class="form-check-label">男</label>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="radio" name="gender" value="0"
									   th:checked="${emp.gender==0}">
								<label class="form-check-label">女</label>
							</div>
						</div>
						<div class="form-group">
							<label>部门</label>
							<!--提交的是部门的ID-->
							<select class="form-control" name="departmentId">
								<option th:selected="${dept.id == emp.departmentId}" th:each="dept:${departments}"
										th:text="${dept.departmentName}" th:value="${dept.id}">1
								</option>
							</select>
						</div>
						<div class="form-group">
							<label>时间</label>
							<input name="date" type="text"  class="form-control" th:value="${#dates.format(emp.date,'yyyy-MM-dd')}" id="dateFormat" autocomplete="off">
						</div>
						<button type="submit" class="btn btn-primary">修改</button>
					</form>

</html>
```

### 参考资料

[狂神说Java](https://www.bilibili.com/video/av75233634?p=20)   
