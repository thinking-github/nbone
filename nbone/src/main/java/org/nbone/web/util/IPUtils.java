package org.nbone.web.util;

import javax.servlet.http.HttpServletRequest;

public class IPUtils {
	
	/**
	 * 通过serlvet框架HttpRequest获得客户端的Ip,可以穿透代理
	 * 
	 * @param request
	 *            没有克隆只能在web容器中使用.
	 * @return
	 */
	public  static String getRemoteVerityIP(HttpServletRequest request) {
		// 集群环境下负载均衡器的’x-forwarded-for‘的属性值应该设置为on否则只能获得代理服务器的ip不是客户端真实的ip
		String remoteAddr = request.getHeader("X-Real-IP");
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

}
