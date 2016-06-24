package org.nbone.framework.spring.web;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.constants.CharsetConstant;
import org.springframework.util.StringUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * <p> add classpath Servlet Container Startup  automatically  Initializer 
 * @author thinking
 * @version 1.0 
 * @since 2016-05-26
 * @see WebApplicationInitializer
 * @see org.springframework.web.SpringServletContainerInitializer
 * @see javax.servlet.ServletContainerInitializer
 */
public class SpringWebApplicationInitializer implements WebApplicationInitializer,CharsetConstant {

	protected  Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		String encoding  = servletContext.getInitParameter("encoding");
		servletContext.log("===============================================================================");
		servletContext.log("current WebApplication config  set character encoding: "+ encoding +" .thinking");
		
		servletContext.log("===============================================================================");
		
		if(!StringUtils.hasText(encoding)){
			encoding = CharsetConstant.CHARSET_UTF8;
			servletContext.log("current WebApplication use default character encoding: "+ encoding  +" .thinking");
		}
		
		
		//CharacterEncodingFilter  
		CharacterEncodingFilter characterEncodingFilter =  new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding(encoding);
		characterEncodingFilter.setForceEncoding(true);
		
		FilterRegistration filterRegistration =  servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
		
		filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
		
		
		
	}

}
