package org.nbone.framework.struts.support;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.nbone.constant.ContentType;
import org.nbone.util.json.JSONOperUtils;
import org.springframework.util.Assert;

import com.opensymphony.xwork2.ActionSupport;

/**
 * struts2 Action超级基类，用于实现控制类的基本操作
 * @author Thinking 
 * @version 1.0 2014-06-19
 */
public abstract class SuperBaseAction extends ActionSupport implements ContentType{
	
	private static final long serialVersionUID = -5667735251743302720L;
	
	/**
	 * 数据转化标识
	 */
	public static final int  transformationType_JSON = 0;
	public static final int  transformationType_XML = 1;
    /**
     * 发送数据至客户端
     * @param contentType        例如:"text/html"
     * @param charset            例如:"utf-8"
     * @param returnData          返回对象
     * @param transformationType  数据转化标识
     * @throws IOException
     */
	public void sendDirectToClient(String contentType, String charset,Object returnData,int transformationType) throws IOException {
		
		if(contentType == null){
			contentType = TEXT_JSON;
		}
		if(charset == null){
			charset = CHARSET_UTF8;
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		Assert.notNull(response);
		String charsetPrefix = ";charset=";
		//"text/html;charset=utf-8"
		String contentHead = (new StringBuilder()).append(contentType).append(charsetPrefix).append(charset).toString();
		response.setContentType(contentHead);
		PrintWriter writer = response.getWriter();
		String strData = "null";
		switch (transformationType) {
		
		case transformationType_JSON:
			strData =  JSONOperUtils.pojoToJSON(returnData);
			
			break;
		case transformationType_XML:
			//TODO:此方法还未实现
					
		    break;

		default:
			break;
		}
		
		writer.write(strData);
		writer.flush();
	}
    /**
     * 
     * @param returnData
     * @throws IOException
     * @see {@link #sendDirectToClient(String, String, Object, int)}
     */
	public void sendDirectToClient(Object returnData) throws IOException {
		
		this.sendDirectToClient(TEXT_JSON, CHARSET_UTF8, returnData, transformationType_JSON);
		
	}

	 

}
