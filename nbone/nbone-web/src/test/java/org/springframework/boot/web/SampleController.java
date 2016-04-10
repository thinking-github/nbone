package org.springframework.boot.web;

import java.util.Map;

import org.nbone.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.test.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@Configuration
@ComponentScan
//@PropertySource("classpath:/applicationconfig.properties")
@EnableAutoConfiguration
public class SampleController {
   
	@Autowired
	private UserService userService;
	
	@Autowired
	private Environment env;
	
	
    @RequestMapping("/")
    @ResponseBody
    String home(@RequestHeader Map<String, String> mapHeader) {
    	String name = userService.getName();
    	
    	System.out.println(env);
    	System.out.println(env.getProperty("server.port"));
    	System.out.println(name);
    	//Object user  = ComponentFactory.getBean("user01");
    	
        return "Hello World----------------44!<br>00000000000000 <h2>00000000000099</h2/";
    }
    
    @Bean(name="user01")
    public User getUser(){
    	return new User();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}