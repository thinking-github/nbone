/*
 * <p>Title: 方天服务平台系统</p>
 * <p>Copyright: Copyright (c) 2011</p>
 */
package org.nbone.web.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http相关信息的操作、获取和管理类
 * @author thinking
 * @author WF
 * 
 */
public class HttpUtil {

	/**
	 * 设置Cookie信息
	 * 
	 * @param resp
	 *            设置Response对象
	 * @param cName
	 *            设置Cookie的名称
	 * @param cValue
	 *            设置Cookie的值
	 * @param expiry
	 *            设置Cookie过期之前的时间，以秒计算。
	 *            如果不设置该值，则Cookie只在当前会话内有效，即在用户关闭浏览器之前有效，而且这些Cookie不会保存到磁盘上
	 */
	public static void setCookie(HttpServletResponse resp, String cName,
			String cValue, int expiry) {
		Cookie cookie = new Cookie(cName, cValue);
		cookie.setMaxAge(expiry);
		resp.addCookie(cookie);
	}

	/**
	 * 设置Cookie信息，默认保留到磁盘，期限为一年
	 * 
	 * @param resp
	 *            设置Response对象
	 * @param cName
	 *            设置Cookie的名称
	 * @param cValue
	 *            设置Cookie的值
	 */
	public static void setCookie(HttpServletResponse resp, String cName,
			String cValue) {
		int expiry = 60 * 60 * 24 * 365;
		Cookie cookie = new Cookie(cName, cValue);
		cookie.setMaxAge(expiry);
		resp.addCookie(cookie);
	}

	/**
	 * 设置修改指定Cookie的值函数
	 * 
	 * @param req
	 *            设置Request对象
	 * @param resp
	 *            设置Response对象
	 * @param cName
	 *            设置Cookie的名称
	 * @param cValue
	 *            设置Cookie的值
	 */
	public static void setCookieValue(HttpServletRequest req,
			HttpServletResponse resp, String cName, String cValue) {
		Cookie[] cookie = req.getCookies();
		for (int i = 0; i < cookie.length; i++) {
			if (cookie[i].getName() != null
					&& cookie[i].getName().equalsIgnoreCase(cName)) {
				cookie[i].setValue(cValue);
				break;
			}
		}
	}

	/**
	 * 获取Cookie对象
	 * 
	 * @param req
	 *            设置Request对象
	 * @param name
	 *            设置Cookie的名称
	 * @return Cookie 返回指定的Cookie对象
	 */
	public static Cookie getCookie(HttpServletRequest req, String name) {
		Cookie[] cookies = req.getCookies();
		if (cookies == null || name == null || name.length() == 0) {
			return null;
		}
		Cookie cookie = null;
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(name)) {
				cookie = cookies[i];
				if (req.getServerName().equals(cookie.getDomain())) {
					break;
				}
			}
		}
		return cookie;
	}

	/**
	 * 删除指定的cookie
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param cookie
	 *            cookie对象
	 */
	public static void deleteCookie(HttpServletResponse response, Cookie cookie) {
		if (cookie != null) {
			// Invalidate the cookie
			cookie.setPath("/");
			cookie.setValue("");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	/**
	 * 获取Cookie对象的值
	 * 
	 * @param req
	 *            设置Request对象
	 * @param cName
	 *            设置Cookie的名称
	 * @return String 返回指定的Cookie对象的值
	 */
	public static String getCookieValue(HttpServletRequest req, String cName) {
		Cookie cookie = getCookie(req, cName);
		String cvalue = null;
		if (cookie != null) {
			cvalue = cookie.getValue();
		}
		return cvalue;
	}

	/**
	 * 设置当前浏览器防止IE缓存函数
	 * 
	 * @param request
	 *            设置Request对象
	 * @param response
	 *            设置Response对象
	 */
	public static void setNoCache(HttpServletRequest request,
			HttpServletResponse response) {

		if (request.getProtocol().compareTo("HTTP/1.0") == 0) {
			response.setHeader("Pragma", "no-cache");
		} else if (request.getProtocol().compareTo("HTTP/1.1") == 0) {
			response.setHeader("Cache-Control", "no-cache");
		}
		response.setDateHeader("Expires", 0);
	}

	/**
	 * 获取当前URL
	 * 
	 * @param request
	 *            设置Request对象
	 * @return String 返回当前URL
	 */
	public static String getCurrentURL(HttpServletRequest request) {
		return request.getRequestURL().toString();
	}

	/**
	 * 获取当前完整URL，包括参数
	 * 
	 * @param request
	 *            设置Request对象
	 * @return String 返回当前完整URL，包括参数等
	 */
	public static String getCurrentFullURL(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		String query = request.getQueryString();
		if (query != null && !"".equals(query)) {
			url.append('?');
			url.append(query);
		}

		return url.toString();
	}

	/**
	 * 获取当前URL中指定的参数字符串
	 * 
	 * @param request
	 *            设置Request对象
	 * @param paramNames
	 *            设置指定的URL参数名称，多个以逗号隔开
	 * @return String 返回当前URL中指定的参数字符串
	 */
	public static String getCurrentURLParam(HttpServletRequest request,
			String paramNames) {
		return getURLParam(getCurrentFullURL(request), paramNames);
	}

	/**
	 * 获取当前URL中所有的参数字符串
	 * 
	 * @param request
	 *            设置Request对象
	 * @return String 返回当前URL中所有的参数字符串
	 */
	public static String getCurrentURLAllParam(HttpServletRequest request) {
		return request.getQueryString();
	}

	/**
	 * 获取URL中指定的参数字符串
	 * 
	 * @param url
	 *            设置URL字符串
	 * @param paramNames
	 *            指定参数名，多个以逗号隔开
	 * @return String 返回URL中指定的参数字符串
	 */
	public static String getURLParam(String url, String paramNames) {
		if (paramNames == null || "".equals(paramNames)) {
			return null;
		}
		String[] pn;
		String params = "";
		String[] allparams = getURLAllParam(url).split("&");
		for (int i = 0; i < allparams.length; i++) {
			pn = paramNames.split(",");
			for (int j = 0; j < pn.length; j++) {
				if (pn[j].equals(allparams[i].split("=")[0])) {
					params += allparams[i] + "&";
				}
			}
		}
		if (!params.equals("")) {
			params = params.substring(0, params.length() - 1);
		}
		return params;
	}

	/**
	 * 获取URL中所有的参数字符串
	 * 
	 * @param url
	 *            设置URL字符串
	 * @return String 返回URL中所有的参数字符串
	 */
	public static String getURLAllParam(String url) {
		if (url == null || "".equals(url)) {
			return null;
		}
		String params = "";
		String[] p = url.split("\\?");
		if (p.length == 2) {
			params = p[1];
		} else if (p.length > 2) {
			for (int i = 1; i < p.length; i++) {
				params += p[i] + "?";
			}
			if (!params.equals("")) {
				params = params.substring(0, params.length() - 1);
			}
		}

		return params;
	}

	/**
	 * 获取当前远程请求的真实IP，包括处理使用代理的请求
	 * 
	 * @param request
	 *            设置Request对象
	 * @return String 返回当前远程请求的真实IP，包括处理使用代理的请求
	 */
	public static String getRemoteVerityIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

	/**
	 * 获取当前远程请求的真实MAC地址
	 * 
	 * @param request
	 *            设置Request对象
	 * @return String 返回结果
	 */
	public static String getRemoteMac(HttpServletRequest request) {
		/** IP Address */
		String address = getRemoteVerityIP(request);
		/** MAC Address */
		String mac;

		String tmpStr = "";
		String tmpMAC = "";
		try {
			Process process = Runtime.getRuntime()
					.exec("nbtstat -a " + address);
			InputStreamReader isReader = new InputStreamReader(process
					.getInputStream());
			LineNumberReader lnReader = new LineNumberReader(isReader);
			for (int i = 1; i < 100; i++) {
				tmpStr = lnReader.readLine();
				if (tmpStr != null) {
					if (tmpStr.indexOf("MAC Address") > 1) {
						tmpMAC = tmpStr.substring(
								tmpStr.indexOf("MAC Address") + 14, tmpStr
										.length());
						break;
					}
				}
			}
		} catch (IOException ex) {
			return null;
		}
		if (tmpMAC.length() < 17) {
			return null;
		}
		mac = tmpMAC.substring(0, 2) + "-" + tmpMAC.substring(3, 5) + "-"
				+ tmpMAC.substring(6, 8) + "-" + tmpMAC.substring(9, 11) + "-"
				+ tmpMAC.substring(12, 14) + "-" + tmpMAC.substring(15, 17);
		return mac;
	}

	/**
	 * 判断一个地址是不是域名，如果是IP返回false
	 * 
	 * @param address
	 *            地址字符串
	 * @return 是否是域名
	 */
	public static boolean isDomain(String address) {
		int di = address.indexOf(".");
		if (di < 0) {
			return false;
		}
		String one = address.substring(0, di);
		return !one.matches("^[-\\+]?\\d+$");
	}

	/**
	 * 导出文件到web
	 * @param request request对象
	 * @param response response对象
	 * @param is InputStream输入流
	 * @param fileName 文件名(包含后缀)
	 * @throws IOException 抛出文件异常
	 */
	public static void exportFile(HttpServletRequest request,HttpServletResponse response, InputStream is,
			String fileName) throws IOException {
		if (!(response.isCommitted())){
			response.reset();
		}
		
		response.setContentType("application/unknown;charset=utf-8");
		response.setContentType("text/plain");

		//解决火狐文件名乱码问题
		String agent=request.getHeader("user-agent");
		if (agent.indexOf("MSIE") != -1) {
			String temp;
			temp = URLEncoder.encode(fileName, "UTF-8");
			if(temp.length()>150){
				temp=new String(fileName.getBytes("GBK"),"ISO-8859-1");
			}
			response.addHeader("Content-Disposition", "attachment; filename=\"" +temp+ "\"");
		}else{
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("utf-8"),"iso-8859-1") + "\"");
		}
		
		ServletOutputStream outs = response.getOutputStream();
		BufferedInputStream buf = new BufferedInputStream(is);
		int readBytes = 0;
		while ((readBytes = buf.read()) != -1) {
			outs.write(readBytes);
		}
		outs.flush();
		
		response.setStatus(200);
		response.flushBuffer();
		
		if (outs != null) {
			outs.close();
		}
		if (buf != null) {
			buf.close();
		}
		if (is != null){
			is.close();
		}
	}
	
	/**
	 * 导出文件到web
	 * @param request request对象
	 * @param response response对象
	 * @param txt 要导出的文本内容
	 * @param fileName 文件名(包含后缀名)
	 * @throws IOException 抛出文件异常
	 */
	public static void exportFile(HttpServletRequest request,HttpServletResponse response, String txt,
			String fileName) throws IOException {
		if (!(response.isCommitted())){
			response.reset();
		}
		
		response.setContentType("application/unknown;charset=utf-8");
		response.setContentType("text/plain");

		//解决火狐文件名乱码问题
		String agent=request.getHeader("user-agent");
		if (agent.indexOf("MSIE") != -1) {
			String temp;
			temp = URLEncoder.encode(fileName, "UTF-8");
			if(temp.length()>150){
				temp=new String(fileName.getBytes("GBK"),"ISO-8859-1");
			}
			response.addHeader("Content-Disposition", "attachment; filename=\"" +temp+ "\"");
		}else{
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("utf-8"),"iso-8859-1") + "\"");
		}

		ServletOutputStream outs = response.getOutputStream();
		outs.write(txt.getBytes());
		outs.flush();
		
		response.setStatus(200);
		response.flushBuffer();
		
		if (outs != null) {
			outs.close();
		}
	}
	
}
