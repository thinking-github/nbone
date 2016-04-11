package org.apache.velocity.demo;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.nbone.test.domain.User;

public class HelloVelocity {
	
	 static void init() {
		 System.out.println("=======================");
	 }
	 public static void main(String[] args) {
	 VelocityEngine ve = new VelocityEngine();
	 ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
	 ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	 
	 ve.init();
	 
	 String path = ve.getProperty(Velocity.FILE_RESOURCE_LOADER_PATH) + File.separator;
	 Object path1 = ve.getProperty(Velocity.RESOURCE_LOADER) + File.separator;
	 
	 ve.setApplicationAttribute("app_chen", "app--------------------------");
	 Template t = ve.getTemplate("org/apache/velocity/demo/hellovelocity.vm");
	 //Template t = ve.getTemplate("D:/hellovelocity.vm");
	 VelocityContext ctx = new VelocityContext();
	 
	 ctx.put("name", "velocity");
	 ctx.put("date1", (new Date()).toString());
	 ctx.put("dateO", new Date());
	 ctx.put("staticUser", User.class);
	 
	 User user =  User.getUser();
	 
	 ctx.put("user",user);
	 
	 List temp = new ArrayList();
	 temp.add("1");
	 temp.add("2");
	 temp.add("322");
	 ctx.put("list", temp);
	 
	 StringWriter sw = new StringWriter();
	 
	 t.merge(ctx, sw);
	 
	 System.out.println(sw.toString());
	 }
	}