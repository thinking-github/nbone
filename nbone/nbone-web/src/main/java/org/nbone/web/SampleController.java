package org.nbone.web;

import java.util.List;
import java.util.Map;

import org.nbone.framework.spring.web.bind.annotation.JsonRequestParam;
import org.nbone.test.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {
	
    @RequestMapping("/test")
    @ResponseBody
    String home(@RequestHeader Map<String, String> mapHeader,@RequestHeader HttpHeaders httpHeaders) {
    	
    	//Object user  = ComponentFactory.getBean("user01");
    	
        return "Hello World----------------44!<br>00000000000000 <h2>00000000000099</h2/";
    }
    
    @RequestMapping("/save")
    @ResponseBody
    Object form(User user,@JsonRequestParam("json") List<Integer> nums,@JsonRequestParam("items") List<User> users) {
    	
    	
        return user;
    }
    
    //@Bean(name="user01")
    public User getUser(){
    	return new User();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}