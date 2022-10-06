### 当年在伟大航路尽头拉夫德鲁找到了这个项目



#### JWT授权实现以及用户接口

1. 配置Mysql

2. 配置SpringBoot
    Maven仓库地址
    Mybatis-Plus官网
    在pom.xml文件中添加依赖：
    Spring Boot Starter JDBC
    Project Lombok
    MySQL Connector/J
    mybatis-plus-boot-starter
    mybatis-plus-generator
    spring-boot-starter-security
    jjwt-api
    jjwt-impl
    jjwt-jackson
    在application.properties中添加数据库配置：
    spring.datasource.username=root
    spring.datasource.password=123456
    spring.datasource.url=jdbc:mysql://localhost:3306/kob?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    SpringBoot中的常用模块
  - pojo层：将数据库中的表对应成Java中的Class
  - mapper层（也叫Dao层）：将pojo层的class中的操作，映射成sql语句
  - service层：写具体的业务逻辑，组合使用mapper中的操作
  - controller层：负责请求转发，接受页面过来的参数，传给Service处理，接到返回值，再传给页面

3. 修改Spring Security
    实现service.impl.UserDetailsServiceImpl类，继承自UserDetailsService接口，用来接入数据库信息
    实现config.SecurityConfig类，用来实现用户密码的加密存储

  ```java
  @Configuration
  @EnableWebSecurity
  public class SecurityConfig {
  
   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }
  }
  ```

  实现utils.JwtUtil类，为jwt工具类，用来创建、解析jwt token
  实现config.filter.JwtAuthenticationTokenFilter类，用来验证jwt token，如果验证成功，则将User信息注入上下文中
  配置config.SecurityConfig类，放行登录、注册等接口

4. 编写API
    将数据库中的id域变为自增
    在数据库中将id列变为自增
    在pojo.User类中添加注解：@TableId(type = IdType.AUTO)
    实现/user/account/token/：验证用户名密码，验证成功后返回jwt token（令牌）
    实现/user/account/info/：根据令牌返回用户信息
    实现/user/account/register/：注册账号
