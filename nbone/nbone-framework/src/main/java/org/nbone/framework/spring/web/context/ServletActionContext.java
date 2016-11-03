package org.nbone.framework.spring.web.context;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author thinking
 * @version 1.0 
 * @since 2015-06-06
 * 
 * @see org.springframework.web.context.request.RequestContextListener
 * @see org.springframework.web.filter.RequestContextFilter
 * @see org.springframework.web.servlet.DispatcherServlet
 * @see org.springframework.web.context.request.RequestContextHolder
 * @since spring 2.0
 */
public class ServletActionContext {
	
	
    /**
     * 返回当前请求的 request
     * <p> spring RequestContextListener和RequestContextHolder，是用过ThreadLocal实现的。
     * <p> the java web.xml config a listener
     * 	<listener>  
     *		<listener-class>org.springframework.web.context.request.RequestContextListener</listener-class>    
	 *	</listener>  
     */
	public static HttpServletRequest getRequest(){
		ServletRequestAttributes servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
		HttpServletRequest request =  servletRequest.getRequest();
		
		RequestAttributes RequestA = RequestContextHolder.getRequestAttributes();
		return request;
	}
	
	
	

}
