package org.nbone.util.text;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.junit.Test;
import org.nbone.test.domain.User;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class FormatTest {

	@Test
	public void freemarker(){
		 Map<String, Object> ctx = new HashMap<String, Object>();
		 ctx.put("name", "Freemarker");
		 ctx.put("date1", (new Date()).toString());
		 String content = "";
		 content += "Welcome  ${name}  to Javayou.com! ";
		 content += "---- ${date1} ------  ";
	
		 System.out.println(FreemarkerFormat.format(content, ctx));
	}

	@Test
	public void velocityFormat(){
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
	@Test
	public void springelFormatTest(){
		 Map<String, Object> ctx = new HashMap<String, Object>();
		 ctx.put("name", "SpringelFormat");
		 ctx.put("date1", (new Date()).toString());
		 String content = "Welcome ,${#name},${ #date1 } --  ${#name1}--${1+2} 1+2";
	
		 System.out.println(SpringelFormat.format(content, ctx));
		 System.out.println(SpringelFormat.format("1+3*2", Long.class));
	}
	@Test
	public void expressionRuleMap() {
		String expression = "#name=='chen' && #age > 18 && #version > 1000";
		Map<String, Object> dataModel = new HashMap<>();
		dataModel.put("name", "chen");
		dataModel.put("age", 19);
		dataModel.put("version", 1001);

		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariables((Map<String, Object>) dataModel);
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(expression);
		Object string = exp.getValue(context);
		System.out.println(string);
		Object bool = exp.getValue(context, boolean.class);
		System.out.println(bool);
	}

	@Test
	public void joddFormat(){
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
