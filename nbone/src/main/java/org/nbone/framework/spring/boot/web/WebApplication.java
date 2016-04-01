package org.nbone.framework.spring.boot.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * 简化Java Web 开发
 * <p>spring boot web</p>
 * @author thinking
 * @version 1.0 
 * @since 2016-04-01
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class WebApplication {
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(WebApplication.class, args);
	}

}
