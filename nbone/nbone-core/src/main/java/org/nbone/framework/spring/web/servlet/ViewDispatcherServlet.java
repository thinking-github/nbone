package org.nbone.framework.spring.web.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.servlet.DispatcherServlet;

public class ViewDispatcherServlet extends DispatcherServlet {

	private static final long serialVersionUID = -8945766024955551868L;
	
	protected String viewEngine;
	
	protected String framework = "SSM";
	
	protected String encoding;

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext sc = config.getServletContext();
		//sc.addFilter("characterEncodingFilter", CharacterEncodingFilter.class);
		
		this.viewEngine = sc.getInitParameter("viewEngine");
		this.framework = sc.getInitParameter("framework");
		this.encoding  = sc.getInitParameter("encoding");
		
		
		super.init(config);
		
		sc.log("========================================================================");
		sc.log("current WebApplication config set viewEngine: "+viewEngine+" .thinking");
		sc.log("current WebApplication config set framework: " +framework +" .thinking");
		sc.log("current WebApplication config set character encoding: "+ encoding +" .thinking");
		sc.log("========================================================================");
	}

	@Override
	public void setContextConfigLocation(String contextConfigLocation) {
		if(viewEngine == null){
			viewEngine = "jsp";
		}
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
