package org.nbone.web.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author thinking  
 * @since 2014-7-16
 * @version 1.0
 * @see org.apache.struts2.RequestUtils
 * 
 */
public class RequestUtils {
	
	public final static String SERVLET_PATH = "javax.servlet.include.servlet_path"; 
    /**
     * Retrieves the current request servlet path.
     * Deals with differences between servlet specs (2.2 vs 2.3+)
     *
     * @param request the request
     * @return the servlet path
     */
    public static String getServletPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        
        String requestUri = request.getRequestURI();
        // Detecting other characters that the servlet container cut off (like anything after ';')
        if (requestUri != null && servletPath != null && !requestUri.endsWith(servletPath)) {
            int pos = requestUri.indexOf(servletPath);
            if (pos > -1) {
                servletPath = requestUri.substring(requestUri.indexOf(servletPath));
            }
        }
        
        if (null != servletPath && !"".equals(servletPath)) {
            return servletPath;
        }
        
        int startIndex = request.getContextPath().equals("") ? 0 : request.getContextPath().length();
        int endIndex = request.getPathInfo() == null ? requestUri.length() : requestUri.lastIndexOf(request.getPathInfo());

        if (startIndex > endIndex) { // this should not happen
            endIndex = startIndex;
        }

        return requestUri.substring(startIndex, endIndex);
    }

    /**
     * Gets the uri from the request
     *
     * @param request The request
     * @return The uri
     */
    public static String getUri(HttpServletRequest request) {
        // handle http dispatcher includes.
        String uri = (String) request.getAttribute(SERVLET_PATH);
        if (uri != null) {
            return uri;
        }

        uri = getServletPath(request);
        if (uri != null && !"".equals(uri)) {
            return uri;
        }

        uri = request.getRequestURI();
        return uri.substring(request.getContextPath().length());
    }
    
    
    //XXX: new add
    public static String getContextPath(HttpServletRequest request) {
    	return request.getContextPath();
    }
    /**
     * 返回服务的信息包含协议/IP地址/端口  <br>
     * http://localhost:8080;
     * @param request
     * @return
     */
    public static String getServerInfo(HttpServletRequest request) {
    	String serverInfo = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
    	return serverInfo;
    }
    
    /**
     * 返回服务的基本路径 <br>
     * http://localhost:8080/hst/ (含有上下文信息) <br>
     * http://localhost:8080/ (未使用上下文信息)
     * @param request
     * @return
     */
    public static String getBasePath(HttpServletRequest request) {
    	String path = request.getContextPath();
    	//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    	String basePath = getServerInfo(request) + path;
    	if(!basePath.endsWith("/")){
    		basePath = basePath +"/";
    	}
    	return basePath;
    }
    
    /**
     * 192.168.0.1:8080
     * @param request
     * @return
     */
    public static String getLocalServerInfo(HttpServletRequest request) {
    	return request.getLocalAddr() + ":" + request.getLocalPort() ;
    }
    
    /**
     * localhost:8080
     * @param request
     * @return
     */
    public static String getLocalServerNameInfo(HttpServletRequest request) {
    	return request.getLocalName() + ":" + request.getLocalPort() ;
    }


}
