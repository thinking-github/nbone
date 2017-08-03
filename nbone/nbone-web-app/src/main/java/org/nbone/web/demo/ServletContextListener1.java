package org.nbone.web.demo;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletContextListener1 implements javax.servlet.ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String ss = sce.getServletContext().getInitParameter("contextConfigLocation");
		System.out.println("------------Serv letContextListener1---------");
		System.out.println("------------ServletContextListener1---------");
		System.out.println("------------ServletContextListener1---------");
		System.out.println("------------ServletContextListener1---------");
		System.out.println("------------ServletContextListener1---------");
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("--------ServletContextListener1-------------");
		
	}


}
