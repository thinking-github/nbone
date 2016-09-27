package org.nbone.web.restful;

import javax.annotation.PostConstruct;

import org.nbone.framework.spring.web.support.SuperController;
import org.nbone.test.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class SampleRestfulController extends SuperController implements ApplicationContextAware {
	
	@PostConstruct
	public void init(){
		System.out.println("===============");
	}
	
    @Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
    	System.out.println(arg0);
    	ApplicationContext parent = arg0.getParent();
    	System.out.println(parent);
	}



	@RequestMapping(method=RequestMethod.POST)
    @ResponseBody
    public Object save(User user) {
    	
        return user;
    }
    
    
    @RequestMapping(value="/{id}",method=RequestMethod.PUT)
    @ResponseBody
    public Object update(@PathVariable("id") String id,User user) {
    	
        return user;
    }
    
    @RequestMapping(value="/{id}",method=RequestMethod.GET)
    @ResponseBody
    public String get(@PathVariable("id") String id) {
    	
        return "get by id "+id;
    }
    
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    @ResponseBody
    public Object delete(@PathVariable("id") String id) {
    	
        return "delete by id " + id;
    }
    
    
    
    
    @RequestMapping(value="login",method=RequestMethod.POST)
    @ResponseBody
    public Object login(User user) {
    	
        return "login";
    }
    


}