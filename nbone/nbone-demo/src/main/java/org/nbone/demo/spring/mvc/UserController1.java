package org.nbone.demo.spring.mvc;

import java.util.List;

import org.nbone.demo.common.domain.Teacher;
import org.nbone.demo.common.domain.User;
import org.nbone.framework.spring.web.bind.annotation.ClassMethodNameRequestMapping;
import org.nbone.framework.spring.web.bind.annotation.ItemResponseBody;
import org.nbone.framework.spring.web.bind.annotation.JsonRequestParam;
import org.nbone.framework.spring.web.bind.annotation.Namespace;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;

/**
 * Spring MVC 扩展使用模式
 * @author thinking
 * @since 2016-08-25
 * @version 1.0
 */
@Controller
//@RequestMapping("user")
@ClassMethodNameRequestMapping
public class UserController1 {
	
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
	
	/*
	 * 1.代码过多收钱的问题,
	 * 
	 * 2.我们不要以为代码写的"越多"越值钱，其实不然。编程简单化,组件化
	 * 
	 * 3.servlet 3.0技术简述 JavaEE6 tomcat7以上支持,jetty8 以上支持(spring web  3.1以后提供简化支持,例如:WebApplicationInitializer,容器启动时自动加载 )
	 */

}
