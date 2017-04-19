# nbone-Framework 企业信息化快速开发平台

## 平台简介
nbone-Framework是基于多个优秀的开源项目，高度整合封装而成的高效、高性能， 强安全性的**开源**Java EE快速开发平台。

nbone-Framework其中扩展实现了Spring MVC框架和重新设计实现了ORM框架。目的是简化开发，更加专注于业务逻辑。

nbone-Framework是您快速完成项目的最佳基础平台解决方案，nbone-Framework是您想学习Java平台的最佳学习案例。

nbone-Framework是在Spring Framework基础上搭建的一个Java基础开发平台，以Spring MVC为模型视图控制器为基础并扩展其功能简化开发，提高工作效率，以JPA注解为基础作为持久映射层，扩展Spring Jdbc形成新的ORM框架 （可选择MyBatis作为数据访问层，并提供扩展插件，简化crud操作以及其他数据库操作）。

##技术选型:  
1、后端                 
- 核心框架：Spring Framework 4.0.0   升级4.0以上可以使用spring boot                             
* 视图框架：Spring MVC
* 服务端验证：Hibernate Validator 5.2 
* 持久层框架：MyBatis 3.2 , hibernate 3.3.10(可选)
* 日志管理：SLF4J 1.7、Log4j
* 工具类：Apache Commons、Jackson 2.6、Xstream 1.4、Dozer 5.3、POI 3.13 
* JPA持久层：JPA 1.0 建议2.0 
* Email：Javamail 1.4

2、平台

* 服务器中间件：在Java EE 5规范（Servlet 2.5、JSP 2.1）下开发，支持应用服务器中间件有Tomcat 6+、Jboss 7+、WebLogic 10+、WebSphere 8+。  建议：建议使用Java EE 6以上的规范(Servlet 3.0,Servlet 3.1)
* 开发环境：Java、Eclipse Java EE 4.3、Maven 3.1、Git
	
	                        
####Spring MVC扩展:
	增加@JsonRequestParam注解解析JSON参数
	增加@ClassMethodNameRequestMapping注解 默认支持类短名和方法名称映射  
	增加@ItemResponseBody注解 包装JavaObject To JSON结果集
	增加@RawResponseBody注解  返回原始JavaObject to JSON结果集
##### 使用:
1.配置：（ 注意：请在spring-mvc-servlet配置文件里配置）

	 <!--扩展插件配置 --> 
	<mvc:annotation-driven >
			<mvc:argument-resolvers>
				<bean class="org.nbone.framework.spring.web.method.annotation.JsonRequestParamMethodArgumentResolver"></bean>
				<bean class="org.nbone.framework.spring.web.method.annotation.JsonRequestBodyMethodProcessor"></bean>
				<bean class="org.nbone.framework.spring.web.method.annotation.FormModelMethodArgumentResolver"></bean>
				<bean class="org.nbone.framework.spring.web.method.annotation.NamespaceModelAttributeMethodProcessor"></bean>
			</mvc:argument-resolvers>
			
			<mvc:return-value-handlers>
				<bean class="org.nbone.framework.spring.web.method.annotation.ItemRequestResponseBodyMethodProcessor"></bean>
				<bean class="org.nbone.framework.spring.web.method.annotation.RawResponseBodyMethodProcessor"></bean>
			</mvc:return-value-handlers>
	</mvc:annotation-driven>
	
	<!-- 异常处理 -->
	<bean class="org.nbone.framework.spring.web.exception.WebExceptionResolver"></bean>   
	
	<!--自动方法名称映射 -->
	<bean id="classMethodNameHandlerMapping" class="org.nbone.framework.spring.web.method.annotation.ClassMethodNameHandlerMapping">
	</bean> 

2.使用	

	@Controller
	@ClassMethodNameRequestMapping
	public class UserController {
	
		/*
		 * http://localhost:8080/user/add
		 * 简化映射关系 默认使用方法名称作为映射
		 */
		public String add(User user){
			return "index";
		}
		
		public String update(User user){
			return "index";
		}
		
		/* http://localhost:8080/user/addMore
		 * user.name =chenyicheng, teacher.name = thinking
		 * 支持命名空间
		 */
		public String addMore(@Namespace("user")User user,@Namespace("teacher")Teacher teacher){
			return "index";
		}
		
		/*
		 * 支持Json String 自动换成 Java Object、Java List,(Spring 其实也支持List参数,前提是将list封装到VO中)
		 */
		@RequestMapping("addMoreJson")
		public String addMoreJson(User user,@JsonRequestParam Teacher teacher,@JsonRequestParam  List<Teacher> teachers){
			//@JsonRequestParam 直接转换成 老师对象 和 老师对象列表
			
			return "index";
		}
		/* http://localhost:8080/user/getUserList
		 * ItemResponseBody 注解自动包装对象 
		 */
		@ItemResponseBody
		public Object getUserList(User user){
			try {
				//service.getUserList();
			} catch (Exception e) {
				throw new RestClientException("操作失败!",e);
			}
			
			return new Object();
		}
	
	}
	
####nbone-orm Framework:	
##### 使用:
1.配置：

    <context:annotation-config />
    <!--配置nbone-orm 框架 -->
	<bean class="org.nbone.framework.spring.dao.config.JdbcComponentConfig">
		<property name="showSql" value="true"></property>
		<property name="dataSource" value="dataSource"></property>
	</bean>
2.使用	

	@Service
	public class UserService {
	
		@Resource
		private NamedJdbcDao namedJdbcDao;
	
		@Override
		public Long save(User object) {
			return (Long) namedJdbcDao.save(object);
		}
		@Override
		public void update(User object) {
			namedJdbcDao.update(object);
		}
		@Override
		public void delete(Long id) {
			namedJdbcDao.delete(User.class,id);
		}
		@Override
		public User get(Long id) {
			return namedJdbcDao.get(id);
		}	
	}
####Spring Jdbc扩展:
	集成springJDBC,提供springJDBCDao 
	封装SimpleJdbcDao NamedJdbcDao可以实现基于对象单表增删改查   
	
####提供丰富的工具箱:
	
	提供对Jsonlib的封装,简化 Java Object 和 JSON 之间的转化;
	
	提供对jackson的封装,简化 Java Object 和 JSON 之间的转化;
	
	提供对XML对象互相转化的封装,简化 Java Object 和 XML 之间的转化;
	
	提供properties文件动态缓存加载功能;
	
	提供反射相关工具类,简化Java反射使用;
	
	提供对httpclient的封装,模拟http get/post 请求;
	
	提供字符校验工具类:手机号/电话/邮箱/身份证号/IP/URL/中文字符等校验功能;
	
	提供日期、时间格式化,简化日期时间和字符串之间的相互转化;
	
	提供datagrid/tree/page/ResultWrapper等实体数据模型.

###作者信息

- 作者:     thinking lang9
- 作者邮箱：chenyicheng00@gmail.com
- create by 2016-03-01 thinking ChenYiCheng (create repository)
- create by 2016-03-24 thinking ChenYiCheng (upload project)

### git 地址：
- git@github.com:thinking-github/nbone.git
