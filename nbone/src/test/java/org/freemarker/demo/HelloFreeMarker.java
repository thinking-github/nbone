package org.freemarker.demo;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.demo.HelloVelocity;
import org.nbone.pojo.User;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class HelloFreeMarker {

	public static void main(String[] args) {
		Configuration config = new Configuration();
		try {
			config.setClassForTemplateLoading(HelloFreeMarker.class, "");
			// 去掉int型输出时的逗号, 例如: 123,456
	        // config.setNumberFormat("#");		// config.setNumberFormat("0"); 也可以
	        config.setNumberFormat("#0.#####");
	        config.setDateFormat("yyyy-MM-dd");
	        config.setTimeFormat("HH:mm:ss");
	        config.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
	        config.setDateTimeFormat("yyyy-MM-dd HH");
	        
			config.setSharedVariable("sharedChen", "sharedChen");
			config.setSharedVariable("name", "HelloFreeMarker----------------SharedVariable");
			
			Template myTemplate = config.getTemplate("hellofreemarker.ftl");
			
			Map<String,Object> dataModel = new HashMap<String, Object>();
			
			dataModel.put("name", "HelloFreeMarker");
			dataModel.put("date1", (new Date()).toString());
			dataModel.put("dateO", new Date());
			dataModel.put("staticUser", User.class);
			 
			 User user =  User.getUser();
			 
			 dataModel.put("user",user);
			 
			 List temp = new ArrayList();
			 temp.add("1");
			 temp.add("2");
			 temp.add("322");
			 dataModel.put("list", temp);
			 
			StringWriter sw = new StringWriter();
			myTemplate.process(dataModel, sw);
			
			System.out.println(sw.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
