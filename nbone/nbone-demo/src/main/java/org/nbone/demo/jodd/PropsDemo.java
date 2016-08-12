package org.nbone.demo.jodd;

import java.io.File;
import java.io.IOException;

import org.springframework.util.ResourceUtils;

import jodd.props.Props;

public class PropsDemo {

	public static void main(String[] args) throws IOException {
		
		Props p = new Props();
		File file = ResourceUtils.getFile("classpath:jodd.properties");
	    p.load(file);
	    
	    String story = p.getValue("nbone.db.url");
	    String name = p.getValue("nbone.db.name");
	    String test = p.getValue("nbone.db.test");
	    String test1 = p.getValue("nbone.db.test1");
	    
	    
	    String mvcname = p.getValue("nbone.mvc.name");
	    String mvctest = p.getValue("nbone.mvc.test");
	    
	    System.out.println(story);
	    System.out.println(name);
	    System.out.println(test);
	    System.out.println(test1);
	    
	    System.out.println(mvcname);
	    System.out.println(mvctest);
	    
	    

	}

}
