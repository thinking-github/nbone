package org.nbone.framework.spring.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
/**
 * 
 * @author thinking
 * @version 1.0 
 * @since  2016年7月25日
 */
public class ServletActionHolder extends RequestContextHolder {
	
	
	public static ServletActionAttributes getServletActionAttributes(){
		return ((ServletActionAttributes) getRequestAttributes());
	}
	
	
	public static HttpServletRequest getRequest(){
		return getServletActionAttributes().getRequest();
	}
	
	public static HttpServletResponse getResponse(){
		return getServletActionAttributes().getResponse();
	}
	
	public static HttpSession getSession(boolean allowCreate){
		return getRequest().getSession(allowCreate);
	}


	
	public static HttpSession getSpringSession(boolean allowCreate){
		return getServletActionAttributes().getHttpSession(allowCreate);
	}

}
