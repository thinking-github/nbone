package org.nbone.framework;

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
public abstract class AbstractHttpServlet  implements ContentType {
	/**
	 * 数据转化标识
	 */
	public static final int  transformationType_JSON = 0;
	public static final int  transformationType_XML = 1;
	
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
		
		case transformationType_JSON:
			strData =  JSONOperUtils.pojoToJSON(object);
			
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
	
	protected void sendToClientWithJson(HttpServletRequest request ,HttpServletResponse response,Object object) throws IOException {
		this.sendToClient(request, response, object, TEXT_JSON, CHARSET_UTF8, transformationType_JSON);
	}
		

}
