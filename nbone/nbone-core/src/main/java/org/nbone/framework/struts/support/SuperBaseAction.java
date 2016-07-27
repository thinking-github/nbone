package org.nbone.framework.struts.support;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.nbone.constants.ContentType;
import org.nbone.framework.AbstractHttpServlet;
import org.nbone.util.json.JSONOperUtils;
import org.springframework.util.Assert;

import com.opensymphony.xwork2.ActionSupport;

/**
 * struts2 Action超级基类，用于实现控制类的基本操作
 * @author thinking 
 * @version 1.0 
 * @since 2014-06-19
 */
public abstract class SuperBaseAction extends ActionSupport implements ContentType{
	
	private static final long serialVersionUID = -5667735251743302720L;
	//使用组合方式，不使用继承
	private AbstractHttpServlet supper;
	
	public SuperBaseAction(){
		
		supper = new AbstractHttpServlet(){};
		
	}
	
    /**
     * 发送数据至客户端
     * @param contentType        例如:"text/html"
     * @param charset            例如:"utf-8"
     * @param object             返回对象
     * @param transformationType  数据转化标识
     * @throws IOException
     */
	public void sendDirectToClient(String contentType, String charset,Object object,int transformationType) throws IOException {
		
		HttpServletRequest request   = ServletActionContext.getRequest(); 
		HttpServletResponse response = ServletActionContext.getResponse();
		
		supper.sendToClient(request, response, object, contentType, charset, transformationType);
	
	}
    /**
     * 
     * @param returnData
     * @throws IOException
     * @see {@link #sendDirectToClient(String, String, Object, int)}
     */
	public void sendDirectToClient(Object returnData) throws IOException {
		
		this.sendDirectToClient(TEXT_JSON, CHARSET_UTF8, returnData, AbstractHttpServlet.TransformationType_JSON);
		
	}

	 

}
