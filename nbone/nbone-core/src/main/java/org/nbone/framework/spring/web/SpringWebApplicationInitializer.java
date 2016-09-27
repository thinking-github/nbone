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
import org.nbone.web.context.prepare.ThinkPrepareFilter;
import org.springframework.core.SpringVersion;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
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
	
	public final static String prefix  = "[nbone-application] initialize";
	
	
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
		
		//HiddenHttpMethodFilter
		initHiddenHttpMethodFilter(servletContext);
		
		//HttpPutFormContentFilter
		initHttpPutFormContentFilter(servletContext);
		
		//XXX：ThinkPrepareFilter 特殊情况下开启暂存 request response
		//initThinkPrepareFilter(servletContext);
		
		//RequestContextFilter
		initRequestContextFilter(servletContext);
		
		//GzipFilter
		initGzipFilter(servletContext);
		
		//HibernateLazy
		initHibernateOpenSessionInViewFilter(servletContext);
		
		
	}
	
    private void initialApplicationContext(ServletContext servletContext) {
        servletContext.setAttribute("appStartupDate",new Date());
        servletContext.setAttribute("nboneAppName","nbone");
        servletContext.setAttribute("nboneAppVersion", NboneVersion.getVersion("1.0.0"));
    }
	/**
	 * <p>默认关闭<p>
	 * 加载spring mvc 统一调度器
	 * @param servletContext
	 */
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
    /**
     * <P> 默认开启
     * <p> 加载字符编码过滤器,用于统一字符编码
     * @param servletContext
     * @param encoding
     */
	protected void initCharacterEncodingFilter (ServletContext servletContext,String encoding){
		servletContext.log(prefix +" CharacterEncodingFilter.thinking");
		
		//CharacterEncodingFilter  
		CharacterEncodingFilter characterEncodingFilter =  new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding(encoding);
		characterEncodingFilter.setForceEncoding(true);
		
		FilterRegistration filterRegistration =  servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
		
		filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
		
	}
	/**
	 * <p>默认关闭<p>
	 * 使用threadLocal暂存 request response
	 * @param servletContext
	 */
	protected void initThinkPrepareFilter (ServletContext servletContext){
		servletContext.log(prefix + " ThinkPrepareFilter.thinking");
		
		//ThinkPrepareFilter  
		ThinkPrepareFilter thinkingFilter =  new ThinkPrepareFilter();
		
		FilterRegistration filterRegistration =  servletContext.addFilter("thinkingFilter", thinkingFilter);
		
		filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
		
	}
	
	/**
	 * <p>默认关闭<p>
	 * 加载HiddenHttpMethodFilter用于支持  HTTP PUT、DELETE(post模拟 put/delete)
	 * 在这处理过程中 HttpServletRequest对象需要修改 故此过滤器放置位置需要靠前（放在过滤字符之后）
	 * @param servletContext
	 * @see HiddenHttpMethodFilter
	 */
	protected void initHiddenHttpMethodFilter(ServletContext servletContext){
		String enableHiddenHttpMethod  = servletContext.getInitParameter("enableHiddenHttpMethod");
		if(BooleanUtils.valueOf(enableHiddenHttpMethod)){
			//HiddenHttpMethodFilter  
			HiddenHttpMethodFilter hiddenHttpMethodFilter =  new HiddenHttpMethodFilter();
			
			FilterRegistration filterRegistration =  servletContext.addFilter("hiddenHttpMethodFilter", hiddenHttpMethodFilter);
			
			filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
		}
	}
	/**
	 * <p>默认关闭<p>
	 * 注意：需放在initHiddenHttpMethodFilter之后
	 * @param servletContext
	 * @see HttpPutFormContentFilter
	 */
	protected void initHttpPutFormContentFilter(ServletContext servletContext){
		String enablePutForm  = servletContext.getInitParameter("enablePutForm");
		if(BooleanUtils.valueOf(enablePutForm)){
			//HttpPutFormContentFilter  
			HttpPutFormContentFilter httpPutFormContentFilter =  new HttpPutFormContentFilter();
			
			FilterRegistration filterRegistration =  servletContext.addFilter("httpPutFormContentFilter", httpPutFormContentFilter);
			
			filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
		}
	}
	
	
	/**
	 * <p>默认关闭<p>
	 * 加载RequestContextFilter 用于将RequestContext存储于当前线程
	 * @param servletContext
	 */
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
	 * <p>默认关闭<p>
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
	 * <p>默认关闭<p>
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
