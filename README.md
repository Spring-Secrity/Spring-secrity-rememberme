# Spring-secrity-rememberme



”记住我“是持久登录认证，应用程序会记住会话用户的身份，在登录时当你使用“记住我”的功能时，应用程序将会将一个cookie在登录成功后发送到浏览器。这个cookie将被保存到浏览器，并继续在一定期限（由cookie生命周期时间定义）.下一次，当您再次访问程序时，浏览器将检测到的cookie,那么用户就会自动登录，无需提供用户名和密码。

SpringSecrity提供了两种"记住我"的实现：

* 简单基于散列标记的方法：它使用散列来保护基于cookie标记的安全性
* 持久化标记方法：它使用一个数据库或其他持久化存储机制来保存生成的标记

今天教程中，我们将讨论用于持久化标记的方法

持久化标记方法，数据库中添加了一个名为 “persistent_logins ”表，可以创建使用下面的sql:

```
CREATE TABLE persistent_logins (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) NOT NULL,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP NOT NULL,
    PRIMARY KEY (series)
); 
```

此表包含用户名，“记住我”的最后一次活动时间(last_used时间戳)，Bcrypt内部实现安全令牌和一系列信息



在springSecrity中设置 “记住我”

```
<!--场景：-->
<!--http://localhost:8080/j_spring_security_check-->
<!--使用spring-security框架，自定义login页面时，发现上面的请求404-->
<!--原因：-->

<!--spring-security的版本问题，spring-security4.x版本，若需要自定义login页面时，需要自定login-processing-url=“/j_spring_security_check”-->
<!--spring-security3.x版本，不需要手动加-->
<!--解决：-->

<!--application-security.xml中加login-processing-url=“/j_spring_security_check”-->

<beans:beans xmlns="http://www.springframework.org/schema/security"
             xmlns:beans="http://www.springframework.org/schema/beans"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
    http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security-4.0.xsd">

    <http auto-config="true">
        <intercept-url pattern="/" access="permitAll"/>
        <intercept-url pattern="/home" access="permitAll"/>
        <intercept-url pattern="/admin**" access="hasRole('ADMIN')"/>
        <intercept-url pattern="/dba**" access="hasRole('ADMIN') and hasRole('DBA')"/>
        <form-login login-page="/login"
                    username-parameter="ssoId"
                    password-parameter="password"
                    authentication-failure-url="/Access_Denied"
                    />
        <csrf/>

        <remember-me
                remember-me-parameter="remember-me"
                remember-me-cookie="remember-me"
                token-validity-seconds="86400"
                data-source-ref="dataSource" />
    </http>

    <authentication-manager >
        <authentication-provider user-service-ref="customUserDetailsService">

        </authentication-provider>

    </authentication-manager>


    <beans:bean id="customUserDetailsService" class="com.anlu.secrity.service.impl.CustomUserDetailsService" />

</beans:beans>
```

完整的代码示例：

需要使用到以下技术：

- Spring 4.1.6.RELEASE
- Spring Security 4.0.1.RELEASE
- Hibernate 4.3.6.Final
- MySQL Server 5.6
- Maven 3
- JDK 1.7
- Tomcat 8.0.21
- Idea



#### 第1步: 工程目录结构

![2](http://p1aoqp63y.bkt.clouddn.com/QQ%E6%88%AA%E5%9B%BE20180104211045.png)



#### 第2步：更新 pom.xml 以包括所需的相关性

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.anlu.secrity</groupId>
  <artifactId>spring-secrity-rememberme</artifactId>
  <packaging>war</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>spring-secrity-rememberme Maven Webapp</name>
  <url>http://maven.apache.org</url>
  <properties>
    <springframework.version>4.1.6.RELEASE</springframework.version>
    <springsecurity.version>4.0.1.RELEASE</springsecurity.version>
    <hibernate.version>4.3.6.Final</hibernate.version>
    <mysql.connector.version>5.1.31</mysql.connector.version>
  </properties>

  <dependencies>

    <!-- Spring -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-core</artifactId>
      <version>${springframework.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
      <version>${springframework.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>${springframework.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-tx</artifactId>
      <version>${springframework.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-orm</artifactId>
      <version>${springframework.version}</version>
    </dependency>


    <!-- Spring Security -->
    <dependency>
      <groupId>org.springframework.security</groupId>
      <artifactId>spring-security-web</artifactId>
      <version>${springsecurity.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework.security</groupId>
      <artifactId>spring-security-config</artifactId>
      <version>${springsecurity.version}</version>
    </dependency>

    <dependency>
      <groupId>org.springframework.security</groupId>
      <artifactId>spring-security-taglibs</artifactId>
      <version>${springsecurity.version}</version>
    </dependency>

    <!-- Hibernate -->
    <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-core</artifactId>
      <version>${hibernate.version}</version>
    </dependency>
    <!-- jsr303 validation -->
    <dependency>
      <groupId>javax.validation</groupId>
      <artifactId>validation-api</artifactId>
      <version>1.1.0.Final</version>
    </dependency>
    <!-- Hibernate validators -->
    <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-validator</artifactId>
      <version>5.1.3.Final</version>
    </dependency>

    <!-- MySQL -->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>${mysql.connector.version}</version>
    </dependency>

    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>3.1.0</version>
    </dependency>
    <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>javax.servlet.jsp-api</artifactId>
      <version>2.3.1</version>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>

    <dependency>
      <groupId>commons-fileupload</groupId>
      <artifactId>commons-fileupload</artifactId>
      <version>1.3.1</version>
    </dependency>
  </dependencies>
  <build>
    <finalName>spring-secrity-rememberme</finalName>
  </build>
</project>

```



## 数据库表部分

#### 第3步：创建数据库模式并填充数据

```
/* For Remember-Me token storage purpose */
CREATE TABLE persistent_logins (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) NOT NULL,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP NOT NULL,
    PRIMARY KEY (series)
);


/*All User's gets stored in APP_USER table*/
create table APP_USER (
   id BIGINT NOT NULL AUTO_INCREMENT,
   sso_id VARCHAR(30) NOT NULL,
   password VARCHAR(100) NOT NULL,
   first_name VARCHAR(30) NOT NULL,
   last_name  VARCHAR(30) NOT NULL,
   email VARCHAR(30) NOT NULL,
   state VARCHAR(30) NOT NULL, 	
   PRIMARY KEY (id),
   UNIQUE (sso_id)
);
 
/* USER_PROFILE table contains all possible roles */  
create table USER_PROFILE(
   id BIGINT NOT NULL AUTO_INCREMENT,
   type VARCHAR(30) NOT NULL,
   PRIMARY KEY (id),
   UNIQUE (type)
);
 
/* JOIN TABLE for MANY-TO-MANY relationship*/   
CREATE TABLE APP_USER_USER_PROFILE (
    user_id BIGINT NOT NULL,
    user_profile_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, user_profile_id),
    CONSTRAINT FK_APP_USER FOREIGN KEY (user_id) REFERENCES APP_USER (id),
    CONSTRAINT FK_USER_PROFILE FOREIGN KEY (user_profile_id) REFERENCES USER_PROFILE (id)
);

/* Populate USER_PROFILE Table */
INSERT INTO USER_PROFILE(type)
VALUES ('USER');

INSERT INTO USER_PROFILE(type)
VALUES ('ADMIN');

INSERT INTO USER_PROFILE(type)
VALUES ('DBA');

/* Populate one Admin User. We need only one user to demonstrate this example. You can add more as done in previous posts*/
INSERT INTO APP_USER(sso_id, password, first_name, last_name, email, state)
VALUES ('sam','abc125', 'Sam','Smith','samy@xyz.com', 'Active');

/* Populate JOIN Table */
INSERT INTO APP_USER_USER_PROFILE (user_id, user_profile_id)
  SELECT user.id, profile.id FROM app_user user, user_profile profile
  where user.sso_id='sam' and profile.type='ADMIN';
```



## 安全部分

### 第4步：添加 Spring-Security配置文件



```
<!--场景：-->
<!--http://localhost:8080/j_spring_security_check-->
<!--使用spring-security框架，自定义login页面时，发现上面的请求404-->
<!--原因：-->

<!--spring-security的版本问题，spring-security4.x版本，若需要自定义login页面时，需要自定login-processing-url=“/j_spring_security_check”-->
<!--spring-security3.x版本，不需要手动加-->
<!--解决：-->

<!--application-security.xml中加login-processing-url=“/j_spring_security_check”-->

<beans:beans xmlns="http://www.springframework.org/schema/security"
             xmlns:beans="http://www.springframework.org/schema/beans"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
    http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security-4.0.xsd">

    <http auto-config="true">
        <intercept-url pattern="/" access="permitAll"/>
        <intercept-url pattern="/home" access="permitAll"/>
        <intercept-url pattern="/admin**" access="hasRole('ADMIN')"/>
        <intercept-url pattern="/dba**" access="hasRole('ADMIN') and hasRole('DBA')"/>
        <form-login login-page="/login"
                    username-parameter="ssoId"
                    password-parameter="password"
                    authentication-failure-url="/Access_Denied"
                    />
        <csrf/>

        <remember-me
                remember-me-parameter="remember-me"
                remember-me-cookie="remember-me"
                token-validity-seconds="86400"
                data-source-ref="dataSource" />
    </http>

    <authentication-manager >
        <authentication-provider user-service-ref="customUserDetailsService">

        </authentication-provider>

    </authentication-manager>


    <beans:bean id="customUserDetailsService" class="com.anlu.secrity.service.impl.CustomUserDetailsService" />

</beans:beans>
```





#### 第六步：定义CustomUserDetailsService 

```
package com.anlu.secrity.service.impl;

import com.anlu.secrity.model.User;
import com.anlu.secrity.model.UserProfile;
import com.anlu.secrity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private UserService userService;

    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String ssoId)
            throws UsernameNotFoundException {
        User user = userService.findBySso(ssoId);
        System.out.println("User : "+user);
        if(user==null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getSsoId(), user.getPassword(),
                user.getState().equals("Active"), true, true, true, getGrantedAuthorities(user));
    }


    private List<GrantedAuthority> getGrantedAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for(UserProfile userProfile : user.getUserProfiles()){
            System.out.println("UserProfile : "+userProfile);
//            authorities.add(new SimpleGrantedAuthority(userProfile.getType()));
            authorities.add(new SimpleGrantedAuthority("ROLE_"+userProfile.getType()));

        }
        System.out.print("authorities :"+authorities);
        return authorities;
    }

}

```





## Hibernate配置部分

Hibernate配置类包含数据源层，SessionFactory和事务管理的@Bean方法。数据源属性是取自 application.properties 文件，包含MySQL数据库详细连接信息。

```
package com.anlu.secrity.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties" })
public class HibernateConfiguration {
    @Autowired
    private Environment environment;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "com.anlu.secrity.model" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        return properties;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        return txManager;
    }
}

```



application.properties

```
jdbc.driverClassName = com.mysql.jdbc.Driver
jdbc.url = jdbc:mysql://localhost:3306/yiibai
jdbc.username = myuser
jdbc.password = mypassword
hibernate.dialect = org.hibernate.dialect.MySQLDialect
hibernate.show_sql = true
hibernate.format_sql = true

```



## DAO, Model & Service服务

#### 第4步: 创建模型类

用户可以有多个角色[DBA，ADMIN，USER]。而角色可以被分配给一个以上的用户。因此，有一个用户和用户配置[角色]之间存在多对多的关系。我们保持这种关系单向[用户到用户配置]，因为我们只是在查找对于给用户的角色。我们将使用使用连接表来实现多一对多关联。



```
package com.anlu.secrity.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="APP_USER")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Column(name="SSO_ID", unique=true, nullable=false)
    private String ssoId;

    @NotEmpty
    @Column(name="PASSWORD", nullable=false)
    private String password;

    @NotEmpty
    @Column(name="FIRST_NAME", nullable=false)
    private String firstName;

    @NotEmpty
    @Column(name="LAST_NAME", nullable=false)
    private String lastName;

    @NotEmpty
    @Column(name="EMAIL", nullable=false)
    private String email;

    @NotEmpty
    @Column(name="STATE", nullable=false)
    private String state=State.ACTIVE.getState();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "APP_USER_USER_PROFILE",
            joinColumns = { @JoinColumn(name = "USER_ID") },
            inverseJoinColumns = { @JoinColumn(name = "USER_PROFILE_ID") })
    private Set<UserProfile> userProfiles = new HashSet<UserProfile>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSsoId() {
        return ssoId;
    }

    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((ssoId == null) ? 0 : ssoId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        if (id != other.id)
            return false;
        if (ssoId == null) {
            if (other.ssoId != null)
                return false;
        } else if (!ssoId.equals(other.ssoId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", ssoId=" + ssoId + ", password=" + password
                + ", firstName=" + firstName + ", lastName=" + lastName
                + ", email=" + email + ", state=" + state + ", userProfiles=" + userProfiles +"]";
    }
}

```

```
package com.anlu.secrity.model;

import javax.persistence.*;

@Entity
@Table(name="USER_PROFILE")
public class UserProfile {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name="TYPE", length=15, unique=true, nullable=false)
    private String type = UserProfileType.USER.getUserProfileType();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof UserProfile))
            return false;
        UserProfile other = (UserProfile) obj;
        if (id != other.id)
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UserProfile [id=" + id + ",  type=" + type	+ "]";
    }
}

```

```
package com.anlu.secrity.model;

public enum UserProfileType {
    USER("USER"),
    DBA("DBA"),
    ADMIN("ADMIN");

    String userProfileType;

    private UserProfileType(String userProfileType){
        this.userProfileType = userProfileType;
    }

    public String getUserProfileType(){
        return userProfileType;
    }
}

```



```
package com.anlu.secrity.model;

public enum State {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    DELETED("Deleted"),
    LOCKED("Locked");

    private String state;

    private State(final String state){
        this.state = state;
    }

    public String getState(){
        return this.state;
    }

    @Override
    public String toString(){
        return this.state;
    }


    public String getName(){
        return this.name();
    }


}

```



#### 第5步: 创建 Dao 层

```
package com.anlu.secrity.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public abstract class AbstractDao<PK extends Serializable,T> {
    private final Class<T> persistentClass;
    @SuppressWarnings("unchecked")
    public AbstractDao(){
        this.persistentClass =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }
    @SuppressWarnings("unchecked")
    public T getByKey(PK key) {
        return (T) getSession().get(persistentClass, key);
    }

    public void persist(T entity){
        getSession().persist(entity);
    }
    public void delete(T entity) {
        getSession().delete(entity);
    }

    protected Criteria createEntityCriteria(){
        return getSession().createCriteria(persistentClass);
    }

}

```



```
package com.anlu.secrity.dao;

import com.anlu.secrity.model.User;

public interface UserDao {
    void save(User user);

    User findById(int id);

    User findBySSO(String sso);
}

```



```
package com.anlu.secrity.dao.impl;

import com.anlu.secrity.dao.AbstractDao;
import com.anlu.secrity.dao.UserDao;
import com.anlu.secrity.model.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {
    public void save(User user) {
        persist(user);
    }

    public User findById(int id) {
        return findById(id);
    }

    public User findBySSO(String sso) {
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("ssoId", sso));
        return (User) crit.uniqueResult();
    }
}

```

#### 第6步: 创建Service层

```
package com.anlu.secrity.service;

import com.anlu.secrity.model.User;

public interface UserService {
    void save(User user);

    User findById(int id);

    User findBySso(String sso);
}

```

```
package com.anlu.secrity.service.impl;

import com.anlu.secrity.dao.UserDao;
import com.anlu.secrity.model.User;
import com.anlu.secrity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao dao;

    public void save(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        System.out.println(passwordEncoder.encode(password));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user);
    }

    public User findById(int id) {
        return dao.findById(id);
    }

    public User findBySso(String sso) {
        return dao.findBySSO(sso);
    }
}

```

## Controller控制器





```
package com.anlu.secrity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HelloController {

    @RequestMapping(value = "/hello")
    public ModelAndView tohello(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("hello");
        return mv;
    }

    @RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
    public String homePage(ModelMap model) {
        model.addAttribute("greeting", "Hi, Welcome to mysite");
        return "welcome";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "admin";
    }

    @RequestMapping(value = "/dba", method = RequestMethod.GET)
    public String dbaPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "dba";
    }

    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "accessDenied";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }


}

```

## 视图部分

login.jsp

```
<%--
  Created by IntelliJ IDEA.
  User: Anlu
  Date: 2018/1/1
  Time: 18:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>HelloWorld Login page</title>
    <link href="lib/bootstrap/css/bootstrap.css"  rel="stylesheet"></link>
    <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
    <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css" />
</head>

<body>
<div id="mainWrapper">
    <div class="login-container">
        <div class="login-card">
            <div class="login-form">
                <c:url var="loginUrl" value="/login" />
                <form action="${loginUrl}" method="post" class="form-horizontal">
                    <c:if test="${param.error != null}">
                        <div class="alert alert-danger">
                            <p>Invalid username and password.</p>
                        </div>
                    </c:if>
                    <c:if test="${param.logout != null}">
                        <div class="alert alert-success">
                            <p>You have been logged out successfully.</p>
                        </div>
                    </c:if>
                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="username"><i class="fa fa-user"></i></label>
                        <input type="text" class="form-control" id="username" name="ssoId" placeholder="Enter Username" required>
                    </div>
                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="password"><i class="fa fa-lock"></i></label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="Enter Password" required>
                    </div>
                    <div class="input-group input-sm">
                        <div class="checkbox">
                            <label><input type="checkbox" id="rememberme" name="remember-me"> Remember Me</label>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}"
                           value="${_csrf.token}" />

                    <div class="form-actions">
                        <input type="submit"
                               class="btn btn-block btn-primary btn-default" value="Log in">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>
```



如你所看到的，CSRF参数需要在JSP中的EL表达式访问，所以还需要通过添将以下的代码添加JSP的顶部来强行执行EL表达式解析编译：



admin.jsp

```
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Admin page</title>
</head>
<body>
	Dear <strong>${user}</strong>, Welcome to Admin Page.
	
	<sec:authorize access="isFullyAuthenticated()">
		<label><a href="#">Create New User</a> | <a href="#">View existing Users</a></label>
	</sec:authorize>
	<sec:authorize access="isRememberMe()">
		<label><a href="#">View existing Users</a></label>
	</sec:authorize>
 
	<a href="<c:url value="/logout" />">Logout</a>
</body>
</html>
```



welcome.jsp

```
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Welcome page</title>
	<link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
	<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
</head>
<body>
	<div class="success">
		Greeting : ${greeting}
		This is a welcome page.
	</div>
</body>
</html>
```



dba.jsp

```
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DBA page</title>
</head>
<body>
	Dear <strong>${user}</strong>, Welcome to DBA Page.
	<a href="<c:url value="/logout" />">Logout</a>
</body>
</html>
```

accessDenied.jsp

```
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>AccessDenied page</title>
</head>
<body>
	Dear <strong>${user}</strong>, You are not authorized to access this page
	<a href="<c:url value="/logout" />">Logout</a>
</body>
</html>
```



## 运行程序

打开浏览器并访问 -  http://localhost:8080/SpringSecurityRememberMeAnnotation/admin

系统将提示您登录，如下图中所示 - 

![2](http://p1aoqp63y.bkt.clouddn.com/QQ%E6%88%AA%E5%9B%BE20180104215329.png)

使用"记住我" 验证登录(使用用户：anlu/111111)，您将获得管理员视图，如下所示 - 

验证查看 Cookies。打开谷歌浏览器设置/显示高级设置/内容设置/所有Cookie和网站数据显示本地主机。你会发现有两个 cookies ，一个用于当前会话[JSESSIONID]，一个用于"记住我"的身份验证。

![1](http://p1aoqp63y.bkt.clouddn.com/QQ%E6%88%AA%E5%9B%BE20180104215306.png)









点击"记住我"(remember-me)，查看cookie详细信息。你可以看到 cookie 的有效期是一天(如在安全配置设置)。

现在查看 persistent_logins 表中数据。应该有当前用户的 Cookies 记录信息。



数据库中多了一条信息

![2](http://p1aoqp63y.bkt.clouddn.com/QQ%E6%88%AA%E5%9B%BE20180104215934.png)

现在退出登录，如下图中所示  - 

![4](http://p1aoqp63y.bkt.clouddn.com/QQ%E6%88%AA%E5%9B%BE20180104220056.png)

现在再试尝试打开：http://localhost:8080/SpringSecurityRememberMeAnnotation/admin，应该直接登录，而不再需要使用输入用户名和密码登录。



![2](http://p1aoqp63y.bkt.clouddn.com/QQ%E6%88%AA%E5%9B%BE20180104220206.png)

还可以看到 “Create New User” 选项不可用，这是因为一个是仅当用户(使用登录名/密码，例如)有权限的认证才能使用此功能，现在从Chrome中删除 "记住我" 的cookie(如前所示)，并再次尝试访问：<http://localhost:8080/SpringSecurityRememberMeAnnotation/admin>，程序应该提示您登录。



