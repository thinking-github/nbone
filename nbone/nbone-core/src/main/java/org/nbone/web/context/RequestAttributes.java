package org.nbone.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 * <p></p>
 * @author thinking
 * @version 1.0 
 * @since  2016年7月25日
 */
public class RequestAttributes {
	
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	
	public RequestAttributes(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}


	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

}
