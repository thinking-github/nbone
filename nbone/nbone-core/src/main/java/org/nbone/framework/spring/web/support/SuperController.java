package org.nbone.framework.spring.web.support;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.constants.ContentType;
import org.nbone.framework.AbstractHttpServlet;
import org.nbone.framework.spring.web.context.ServletActionAttributes;
import org.nbone.framework.spring.web.context.ServletActionContext;
import org.nbone.framework.spring.web.context.ServletActionHolder;
import org.nbone.util.DateFPUtils;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
 * @since spring 3.1
 */

public abstract class SuperController extends AbstractHttpServlet implements ContentType{
	
	protected static Log logger = LogFactory.getLog(SuperController.class);
	
	protected String  redirectStr = "redirect:";  
	
	@Deprecated
    protected Model model;
    /**
     * spring package  HttpServletRequest
     */
    @Deprecated
    protected WebRequest webRequest;
    
    
    /**
     * ModelAttribute 此注解用于http事件请求时第一个执行的方法
     * @param request 
     * @param response
     */
    @ModelAttribute
    protected void doExecuteSetReqAndRes(HttpServletRequest request, HttpServletResponse response){  
        //XXX:thinking  ThreadLocal
        ServletActionAttributes attributes = new ServletActionAttributes(request,response);
        ServletActionHolder.setRequestAttributes(attributes);
        
        
    }  
    //@ModelAttribute
    protected void doExecuteController(WebRequest webRequest,Model model){
    	
    }
    
    
    public HttpServletRequest getRequest() {
		return ServletActionHolder.getRequest();
	}
    
	public HttpServletResponse getResponse() {
		return ServletActionHolder.getResponse();
	}
	/**
     * @see spring org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver
     */
    protected Map<String,String> getParameterMap(){
    	HttpServletRequest webRequest = getRequest();
    	Map<String, String[]> parameterMap = webRequest.getParameterMap();
    	Map<String, String> result = new LinkedHashMap<String, String>(parameterMap.size());
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			if (entry.getValue().length > 0) {
				result.put(entry.getKey(), entry.getValue()[0]);
			}
		}
		return result;
    }
    
    /**
     * @see spring org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver
     */
    protected MultiValueMap<String,String> paramsMultiValueMap(){
    	HttpServletRequest webRequest = getRequest();
    	Map<String, String[]> parameterMap = webRequest.getParameterMap();
		MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>(parameterMap.size());
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			for (String value : entry.getValue()) {
				result.add(entry.getKey(), value);
			}
		}
		return result;
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
    
	
	protected String redirect(String url){
		StringBuilder urlsb = new StringBuilder("redirect:");
		urlsb.append(url);
		return redirectStr.toString();
	}
	
    
	protected void sendToClientWithJson(Object object) throws IOException {
		this.sendToClientWithJson(getRequest(),getResponse(), object);
	}
	
	protected void sendToClientWithHtml(Object object) throws IOException {
		this.sendToClientWithHtml(getRequest(),getResponse(), object);
	}
    

	//----------------------------------------------
	public static void test(){
    	
		ServletActionContext.getRequest();
    	
    }

}
