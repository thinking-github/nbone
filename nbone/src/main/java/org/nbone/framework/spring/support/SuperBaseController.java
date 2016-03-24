package org.nbone.framework.spring.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nbone.constant.ContentType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Spring MVC 控制层超级基类，用于实现控制类的基本操作
 * <p/>
 * 
 * @author thinking  2013-8-28 
 * @serial 1.0
 */

public abstract class SuperBaseController implements ContentType{
	
	protected HttpServletRequest request;  
    protected HttpServletResponse response;  
    protected HttpSession session; 
    protected Model model;
    
    /**
     * ModelAttribute 此注解用于http事件请求时第一个执行的方法
     * @param request 
     * @param response
     */
    @ModelAttribute
    public void doExecuteSetReqAndRes(HttpServletRequest request, HttpServletResponse response,Model model){  
        this.request = request;  
        this.response = response;  
        this.model = model;
        if(request != null) {
        	this.session = request.getSession();  
        }
        
    }  
    
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	
	//----------------------------------------------
	public static void test(){
    	
    	ServletRequestAttributes servlet = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
		RequestAttributes RequestA = RequestContextHolder.getRequestAttributes();
    	
    }

}
