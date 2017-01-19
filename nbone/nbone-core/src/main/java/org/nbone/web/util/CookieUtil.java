/**
 * 项目名称:东辆设备管理系统
 * Copyright (c) 2010 
 */

package org.nbone.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Cookie工具类
 * @author thinking
 * @author WF
 * @version 1.0
 * @since   2016年4月4日
 *
 */
public class CookieUtil {
	
	/**
	 * 按名称查询cookie值
	 * @param name
	 * @param request
	 * @return
	 */
	public static String getCookieValue(String name,HttpServletRequest request){
		String value = "";
		Cookie[] cookies = request.getCookies();
		if(null != cookies){
			for(Cookie cookie:cookies){
				if(cookie.getName().equalsIgnoreCase(name)){
					value = cookie.getValue();
					break;
				}
			}
		}
		return value;
	}
	
	/**
	 * 写cookie信息
	 * @param name
	 * @param value
	 * @param maxAge
	 */
	public static void setCookieValue(String name,String value,int maxAge,HttpServletResponse response){
		Cookie cookie = new Cookie(name,value);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
	
	/**
	 * 清楚cookie
	 * @param name
	 * @param response
	 */
	public static void removeCookie(String name , HttpServletResponse response ){
		setCookieValue(name,null,0,response);
	}
}
