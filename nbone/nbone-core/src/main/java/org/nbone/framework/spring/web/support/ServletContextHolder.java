package org.nbone.framework.spring.web.support;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ServletContextAware;
/**
 * 以静态变量保存Spring ServletContext, 可在任何代码任何地方任何时候中取出ServletContext.<p>
 * get ServletContext bean<br> <p>
 * 
 * note: use  ServletContextHolder must IOC Spring
 * 
 * @author thinking
 * @version 1.0 
 * @see ServletContextAware
 * @see org.springframework.web.context.support.WebApplicationObjectSupport
 */
public class ServletContextHolder implements ServletContextAware {

	protected static Log logger =  LogFactory.getLog(ServletContextHolder.class);
	private static ServletContext context;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		context = servletContext;
		logger.info("Web Project ContextPath= " + context.getContextPath());
		
		logger.debug("注入servletContext到ServletContextHolder:"+ servletContext);
		
	}

	 public String getContextPath(){
		 return context.getContextPath();
	 }
}
