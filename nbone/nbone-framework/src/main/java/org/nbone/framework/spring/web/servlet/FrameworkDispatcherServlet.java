package org.nbone.framework.spring.web.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.DispatcherServlet;
/**
 * 支持框架组件类型
 * <li> SSH  SpringMVC + spring + Hibernate
 * <li> SSM  SpringMVC + spring + Mybatis
 * <li> S2SH struts2 + spring + Hibernate
 * <li> S2SM struts2 + spring + Mybatis <br>
 * 
 * <li> SSM   jsp + SSM
 * <li> J-SSM jsp + SSM
 * <li> V-SSM velocity + SSM
 * <li> F-SSM freemarker + SSM
 * 
 * @author thinking
 *
 */
public class FrameworkDispatcherServlet extends DispatcherServlet {

	private static final long serialVersionUID = -8945766024955551868L;
	
	protected String framework = "SSM";
	
	protected String encoding = "UTF-8";
	
	private String viewEngine = "jsp";

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext sc = config.getServletContext();
		//sc.addFilter("characterEncodingFilter", CharacterEncodingFilter.class);
		initFramework(config,sc);
		
		super.init(config);

	}
	
	private void initFramework(ServletConfig config,ServletContext sc){
		String framework = sc.getInitParameter("framework");
		String encoding  = sc.getInitParameter("encoding");
		if(framework != null){
			String[] frameworks = StringUtils.split(framework, "-");
			if(frameworks.length == 1){
				this.framework = frameworks[0];
			}
			if(frameworks.length == 2){
				this.viewEngine = frameworks[0];
				this.framework = frameworks[1];
			}
			
		}
		if(encoding != null){
			this.encoding = encoding;
		}
		
		sc.log("========================================================================");
		sc.log("current WebApplication config set viewEngine: " + this.viewEngine +" .thinking");
		sc.log("current WebApplication config set framework: " + this.framework +" .thinking");
		sc.log("current WebApplication config set character encoding: "+ this.encoding +" .thinking");
		sc.log("========================================================================");
		
	}

	@Override
	public void setContextConfigLocation(String contextConfigLocation) {
	
		viewEngine = viewEngine.trim();
		String viewPath = "";
		if(viewEngine.equalsIgnoreCase("velocity") || viewEngine.equalsIgnoreCase("V")){
			//velocity
			viewPath = "classpath:/configuration/springmvc/springmvc-velocity.xml";
			
		}else if(viewEngine.equalsIgnoreCase("freemarker") || viewEngine.equalsIgnoreCase("F")){
			//freemarker
			viewPath = "classpath:/configuration/springmvc/springmvc-freemarker.xml";
			
		}else {
			//jsp
			viewPath = "classpath:/configuration/springmvc/springmvc-jsp.xml";
			
		}
		contextConfigLocation = contextConfigLocation +"," + viewPath;
		
		
		super.setContextConfigLocation(contextConfigLocation);
	}
	

}
