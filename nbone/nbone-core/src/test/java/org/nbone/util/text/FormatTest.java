package org.nbone.util.text;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.nbone.test.domain.User;

public class FormatTest {

	public static void main(String[] args) {
		 //freemarker();
		 //velocityFormat();
		 //springelFormat();
		 joddFormat();
		
	}
	
	static void freemarker(){
		 Map<String, Object> ctx = new HashMap<String, Object>();
		 ctx.put("name", "Freemarker");
		 ctx.put("date1", (new Date()).toString());
		 String content = "";
		 content += "Welcome  ${name}  to Javayou.com! ";
		 content += "---- ${date1} ------  ";
	
		 System.out.println(FreemarkerFormat.format(content, ctx));
	}
	
	static void velocityFormat(){
		 VelocityContext ctx = new VelocityContext();
		 ctx.put("name", "velocity");
		 ctx.put("date1", (new Date()).toString());
		 String content = "";
		 content += "Welcome  $name  to Javayou.com! ";
		 content += "---- $date1 ------  ";
		 
		 System.out.println(VlocityFormat.format(content, ctx));
		 System.out.println(VlocityFormat.format("你好数字{0}-{1}", "33","99"));
		 System.out.println(VlocityFormat.format("你好数字{0}-{1}",Arrays.asList("66","99")));
		 
		 
		 MessageFormat msg = new MessageFormat("你好数字{0}-{1}");
		 String ss = msg.format(Arrays.asList("66","99000").toArray());
		 System.out.println(ss);
		 try {
			Object kk = msg.parseObject(ss);
			System.out.println(kk);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	static void springelFormat(){
		 Map<String, Object> ctx = new HashMap<String, Object>();
		 ctx.put("name", "SpringelFormat");
		 ctx.put("date1", (new Date()).toString());
		 String content = "Welcome ,${#name},${ #date1 } --  ${#name1}--${1+2} 1+2";
	
		 System.out.println(SpringelFormat.format(content, ctx));
		 System.out.println(SpringelFormat.format("1+3*2", Long.class));
	}
	
	static void joddFormat(){
		 Map<String, Object> ctx = new HashMap<String, Object>();
		 ctx.put("name", "joddFormat");
		 ctx.put("date1", (new Date()).toString());
		 String content = "Welcome ,${name},${date1} --  ${name1}--${1+2}";
		 
		 
		 String template = "Welcome ,${id}-${name} --  ${name1}--${1+2}";
		 User user = new User("001","thinking");
		 
	
		 System.out.println(JoddFormat.format(content, ctx));
		 System.out.println(JoddFormat.format(template, user));
	}
	

}
