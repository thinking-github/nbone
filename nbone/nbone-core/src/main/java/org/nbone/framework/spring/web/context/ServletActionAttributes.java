package org.nbone.framework.spring.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.ServletRequestAttributes;
/**
 * <p> note： spring 4.1 加入HttpServletResponse属性
 * @author thinking 
 * @version 1.0 
 * @since  2016-7-25
 * @see spring 2.0
 * @see  org.springframework.web.context.request.ServletWebRequest
 */
public class ServletActionAttributes extends ServletRequestAttributes{

	
	private final HttpServletResponse response;
	
	public ServletActionAttributes(HttpServletRequest request,HttpServletResponse response) {
		super(request);
		this.response = response;
	}


	public HttpServletResponse getResponse() {
		return response;
	}
	
	
	public  HttpSession getHttpSession(boolean allowCreate){
		return getSession(allowCreate);
	}
	
	

}
