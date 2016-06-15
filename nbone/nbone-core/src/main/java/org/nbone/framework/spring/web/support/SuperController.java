package org.nbone.framework.spring.web.support;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.constant.ContentType;
import org.nbone.framework.AbstractHttpServlet;
import org.nbone.util.DateFPUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    protected void doExecuteSetReqAndRes(HttpServletRequest request, HttpServletResponse response){  
        this.request = request;  
        this.response = response;  
        
        if(request != null) {
        	this.session = request.getSession();  
        }
        
    }  
    @ModelAttribute
    protected void doExecuteController(WebRequest webRequest,Model model){
    	this.webRequest = webRequest;
    	this.model = model;
    }
    
    /**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
/*		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});*/
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateFPUtils.parseDateMultiplePattern(text));
			}
		});
		
	}
    
	
	
    
	protected void sendToClientWithJson(Object object) throws IOException {
		this.sendToClientWithJson(request, response, object);
	}
    

	//----------------------------------------------
	public static void test(){
    	
		ServletActionContext.getRequest();
    	
    }

}
