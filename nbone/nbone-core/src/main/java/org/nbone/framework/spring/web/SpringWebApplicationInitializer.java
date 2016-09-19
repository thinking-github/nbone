package org.nbone.framework.spring.web;

import java.util.Date;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

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
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.DispatcherServlet;

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
		
		servletContext.log("===============================================================================");
		
		servletContext.log("Java EE 6 Servlet 3.0");
		servletContext.log("Nbone Version: " + NboneVersion.getVersion(NboneVersion.version));
		servletContext.log("Spring Version: " + SpringVersion.getVersion());
		String encoding  = servletContext.getInitParameter("encoding");
		servletContext.log("current WebApplication config  set character encoding: "+ encoding +" .thinking");
		if(!StringUtils.hasText(encoding)){
			encoding = CharsetConstant.CHARSET_UTF8;
			servletContext.log("current WebApplication use default character encoding: "+ encoding  +" .thinking");
		}
		
		servletContext.log("===============================================================================");
		
		//DispatcherServlet
		initDispatcherServlet(servletContext);
		
		//CharacterEncodingFilter  
		initCharacterEncodingFilter(servletContext, encoding);
		
		//RequestContextFilter
		initRequestContextFilter(servletContext);
		
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
	
    protected void initDispatcherServlet(ServletContext servletContext){
    	String enable  = servletContext.getInitParameter("enableDispatcherServlet");
    	if(BooleanUtils.valueOf(enable)){
    		//classpath*:/spring-mvc*.xml,/WEB-INF/spring-mvc*.xml
    		XmlWebApplicationContext appContext = new XmlWebApplicationContext();
    		appContext.setConfigLocation("classpath*:/spring/spring-mvc*.xml,/WEB-INF/spring/spring-mvc*.xml");
    		
    		
    		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(appContext));
    		dispatcher.setLoadOnStartup(1);
    		dispatcher.addMapping("/");
    		
    	}

    }
    
	protected void initCharacterEncodingFilter (ServletContext servletContext,String encoding){
		//CharacterEncodingFilter  
		CharacterEncodingFilter characterEncodingFilter =  new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding(encoding);
		characterEncodingFilter.setForceEncoding(true);
		
		FilterRegistration filterRegistration =  servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
		
		filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
		
	}
	
	protected void initRequestContextFilter(ServletContext servletContext){
		String enableRequestContextFilter  = servletContext.getInitParameter("enableRequestContext");
		if(BooleanUtils.valueOf(enableRequestContextFilter)){
			//RequestContextFilter  
			RequestContextFilter requestContextFilter =  new RequestContextFilter();
			
			FilterRegistration filterRegistration =  servletContext.addFilter("requestContextFilter", requestContextFilter);
			
			filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
		}
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
