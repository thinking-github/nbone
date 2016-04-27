package org.nbone.framework.spring.support;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.constant.ContentType;
import org.nbone.framework.AbstractHttpServlet;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Spring MVC 控制层超级基类，用于实现控制类的基本操作
 * <p/>
 * 
 * @author thinking  
 * @serial 1.0
 * @since  2013-8-28 
 */

public abstract class SuperController extends AbstractHttpServlet implements ContentType{
	
	protected static Log logger = LogFactory.getLog(SuperController.class);
	
	protected String  redirectStr = "redirect:";  
	
	protected HttpServletRequest request;  
    protected HttpServletResponse response;  
    protected HttpSession session; 
    
    protected Model model;
    /**
     * spring package  HttpServletRequest
     */
    protected WebRequest webRequest;
    
    
    
    /**
     * ModelAttribute 此注解用于http事件请求时第一个执行的方法
     * @param request 
     * @param response
     */
    @ModelAttribute
    public void doExecuteSetReqAndRes(HttpServletRequest request, HttpServletResponse response){  
        this.request = request;  
        this.response = response;  
        
        if(request != null) {
        	this.session = request.getSession();  
        }
        
    }  
    @ModelAttribute
    public void doExecuteController(WebRequest webRequest,Model model){
    	this.webRequest = webRequest;
    	this.model = model;
    }
    
    
	public void sendToClientWithJson(Object object) throws IOException {
		this.sendToClientWithJson(request, response, object);
	}
    

	//----------------------------------------------
	public static void test(){
    	
		ServletActionContext.getRequest();
    	
    }

}
