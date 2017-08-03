package org.nbone.demo.spring.mvc;

import org.nbone.demo.common.domain.StudentAndTeacher;
import org.nbone.demo.common.domain.Teacher;
import org.nbone.demo.common.domain.User;
import org.nbone.mx.datacontrols.support.ResultWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * Spring MVC 传统使用模式
 * @author thinking
 *
 */
@Controller
@RequestMapping("user")
public class UserController {
	
	/*
	 * ------------------基本映射--------------------
	 */
	@RequestMapping("add")
	public String add(User user){
		return "index";
	}
	
	@RequestMapping("update")
	public String update(User user){
		return "index";
	}
	
	/*
	 * 组合对象 当含有相同的属性时值都是最后一个
	 */
	@RequestMapping("addMore")
	public String addMore(User user,Teacher teacher){
		return "index";
	}
	/*
	 * user.name =chenyicheng
	 * teacher.name = thinking
	 * 需要封装两个基本对象
	 */
	@RequestMapping("addMore")
	public String addMore(StudentAndTeacher stuTea){
		return "index";
	}
	
	/*
	 * Json 转换,手动Json字符串转化	
	 */
	@RequestMapping("addMore")
	public String addMoreJson(User user,String teacherJsonStr,String teacherJsonList){
		
		//teacherJsonStr to Teacher Object
		//teacherJsonList to Teacher Array or List
		
		return "index";
	}
	
	/*
	 * 返回对象封装
	 * {successful:成功状态,errCode:"",message:"",resultValue:""  ....以后扩展问题需要修改源代码}
	 */
	@ResponseBody
	@RequestMapping("getUserList")
	public Object getUserList(User user){
		
		ResultWrapper result = new ResultWrapper(true);
		try {
			//service.getUserList();
			result.setResultValue(new Object());
			result.setMessage("操作 成功");
		} catch (Exception e) {
			result.setSuccessful(false);
			result.setMessage("操作 失败!");
		}
		
		return result;
	}
	

}
