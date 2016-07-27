package org.nbone.framework.spring.web;

import java.util.Date;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Version;
import org.nbone.NboneVersion;
import org.nbone.constants.CharsetConstant;
import org.nbone.util.lang.BooleanUtils;
import org.springframework.core.SpringVersion;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

import net.sf.ehcache.constructs.web.filter.GzipFilter;

/**
 * <p> add classpath Servlet Container Startup  automatically  Initializer 
 * <p> note:  Servlet 3.0+ environments
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
		initialApplicationContext(servletContext);
		servletContext.log("Nbone Version: " + NboneVersion.getVersion(NboneVersion.version));
		servletContext.log("Spring Version: " + SpringVersion.getVersion());
		String encoding  = servletContext.getInitParameter("encoding");
		servletContext.log("===============================================================================");
		servletContext.log("current WebApplication config  set character encoding: "+ encoding +" .thinking");
		
		servletContext.log("===============================================================================");
		
		if(!StringUtils.hasText(encoding)){
			encoding = CharsetConstant.CHARSET_UTF8;
			servletContext.log("current WebApplication use default character encoding: "+ encoding  +" .thinking");
		}
		
		//CharacterEncodingFilter  
		initCharacterEncodingFilter(servletContext, encoding);
		//GzipFilter
		initGzipFilter(servletContext);
		
		//HibernateLazy
		initHibernateOpenSessionInViewFilter(servletContext);
		
		
	}
	
    private void initialApplicationContext(ServletContext servletContext) {
        servletContext.setAttribute("applicationStartupDate",new Date());
        servletContext.setAttribute("nboneApplicationName","nbone");
        servletContext.setAttribute("nboneApplicationVersion", NboneVersion.getVersion("1.0.0"));
    }
	
    
    
	protected void initCharacterEncodingFilter (ServletContext servletContext,String encoding){
		//CharacterEncodingFilter  
		CharacterEncodingFilter characterEncodingFilter =  new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding(encoding);
		characterEncodingFilter.setForceEncoding(true);
		
		FilterRegistration filterRegistration =  servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
		
		filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
		
	}
	/**
	 * 启用静态文件压缩
	 * @param servletContext
	 */
	protected void initGzipFilter(ServletContext servletContext){
		String enableGzip  = servletContext.getInitParameter("enableGzip");
		if(BooleanUtils.valueOf(enableGzip)){
			//GzipFilter
			GzipFilter gzipFilter = new GzipFilter();
			FilterRegistration filterRegistration =  servletContext.addFilter("gzipFilter", gzipFilter);
			filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "*.css","*.js","*.png","*.gif","*.html","*.htm");
		}
	}
	
	
	/**
	 * enable Hibernate Lazy
	 * @param servletContext
	 */
	protected void initHibernateOpenSessionInViewFilter(ServletContext servletContext){
		String enableHibernateLazy  = servletContext.getInitParameter("enableHibernateLazy");
		if(BooleanUtils.valueOf(enableHibernateLazy)){
			String version =  Version.getVersionString().trim();
			servletContext.log("Hibernate Version: "+ version);
			
			if(version.startsWith("3")){
				//enableHibernate3Lazy
				org.springframework.orm.hibernate3.support.OpenSessionInViewFilter openSessionInViewFilter =
						new org.springframework.orm.hibernate3.support.OpenSessionInViewFilter ();
				
				FilterRegistration filterRegistration =  servletContext.addFilter("openSessionInViewFilter", openSessionInViewFilter);
				filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
			}
			else if(version.startsWith("4")){
				
				//enableHibernate4Lazy
				OpenSessionInViewFilter openSessionInViewFilter = new OpenSessionInViewFilter();
				FilterRegistration filterRegistration =  servletContext.addFilter("openSessionInViewFilter", openSessionInViewFilter);
				filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
				
			}else{
				
			}
		
		}
	}
	

}
