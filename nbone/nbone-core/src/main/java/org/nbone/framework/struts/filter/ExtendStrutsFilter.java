package org.nbone.framework.struts.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
import org.nbone.context.system.SystemContext;
import org.nbone.web.util.EasyuiUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 扩展struts2 StrutsPrepareAndExecuteFilter 
 *<li> 将Spring  applicationContext 装载到系统全局上下文中（SystemContext）
 *<li> 设置数据库类型
 *<li> 集成  easy ui 的分页的全局传递
 *
 * @author Thinkiing  2014-6-19
 * @version 1.0 
 */

public class ExtendStrutsFilter extends StrutsPrepareAndExecuteFilter {
	
	/**
	 * 编码
	 */
	protected String encoding = null; 
	/**
	 * 判别类TRUE表示
	 */
	protected boolean ignore = true;
	
	
	
	
	
	public void init(FilterConfig filterConfig) throws ServletException {
		//将Spring applicationContext装载到自己的SystemContext中
		ServletContext sc = filterConfig.getServletContext();
		sc.log("Initializing 【ExtendStrutsFilter】 integration...");
		
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		SystemContext.setAppicationContext(ac);
		SystemContext.currentUse_DB_TYPE = SystemContext.DB_TYPE_ORACLE;
		
		super.init(filterConfig);
		
		sc.log("... initialized 【ExtendStrutsFilter】 integration successfully.Thinking");
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		
		String uri = req.getRequestURI();
		integrationEasyui(req);
		
        super.doFilter(request, response, chain);

//     设置字符集
//		if (ignore || (request.getCharacterEncoding() == null)) {
//			String encoding = selectEncoding(request);
//			if (encoding != null)
//				request.setCharacterEncoding(encoding);
        
	}
	
 

		protected String selectEncoding(ServletRequest request) {
			return (this.encoding);
		}
		/**
		 * 集成Easyui
		 * @param request
		 */
		protected void integrationEasyui(HttpServletRequest request) {
			//设置分页信息
			int currentOffset = EasyuiUtils.getOffset(request);
			int currentPageSize = EasyuiUtils.getPageSize(request);
			
			SystemContext.setOffset(currentOffset);
	 		SystemContext.setPageSize(currentPageSize);
	 		
	 		request.setAttribute(EasyuiUtils.PAGE, currentOffset);
	 		request.setAttribute(EasyuiUtils.ROWS, currentPageSize);
	 		
		}
		

	
}
