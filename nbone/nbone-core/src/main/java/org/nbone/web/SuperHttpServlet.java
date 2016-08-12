package org.nbone.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nbone.constants.ContentType;
import org.nbone.util.json.JSONOperUtils;
import org.springframework.util.Assert;
/**
 * 
 * @author thinking
 * @version 1.0
 * @since   2016-04-09
 *
 */
public abstract class SuperHttpServlet  implements ContentType {
	/**
	 * 数据转化标识
	 */
	public static final int  TransformationType_JSON = 0;
	public static final int  TransformationType_XML  = 1;
	public static final int  TransformationType_HTML = 2;
	
	
	/**
	 * 
	 * @param request            HttpServletRequest
	 * @param response           HttpServletResponse
	 * @param object             返回对象
	 * @param contentType        例如:"text/html"
	 * @param charset            例如:"utf-8"
	 * @param transformationType 数据转化标识
	 * @throws IOException
	 */
	public void sendToClient(HttpServletRequest request ,HttpServletResponse response,Object object,String contentType, String charset,int transformationType) throws IOException {
			
		if(contentType == null){
			contentType = TEXT_JSON;
		}
		if(charset == null){
			charset = CHARSET_UTF8;
		}
		
		Assert.notNull(response);
		
		//"text/html;charset=utf-8"
		String contentHead = (new StringBuilder()).append(contentType).append(SUB_CHARSET_PREFIX).append(charset).toString();
		response.setContentType(contentHead);
		PrintWriter writer = response.getWriter();
		String strData = "null";
		switch (transformationType) {
		
		case TransformationType_JSON:
			strData =  JSONOperUtils.pojoToJSON(object);
			
			break;
		case TransformationType_XML:
			//TODO:此方法还未实现
					
		    break;
		case TransformationType_HTML:
			strData = 	String.valueOf(object);
		    break;

		default:
			break;
		}
		
		writer.write(strData);
		writer.flush();
		writer.close();
			
	}
	
	protected void sendToClientWithJson(HttpServletRequest request ,HttpServletResponse response,Object object) throws IOException {
		this.sendToClient(request, response, object, TEXT_JSON, CHARSET_UTF8, TransformationType_JSON);
	}
	
	protected void sendToClientWithHtml(HttpServletRequest request ,HttpServletResponse response,Object object) throws IOException {
		this.sendToClient(request, response, object, TEXT_HTML, CHARSET_UTF8, TransformationType_HTML);
	}
		

}
