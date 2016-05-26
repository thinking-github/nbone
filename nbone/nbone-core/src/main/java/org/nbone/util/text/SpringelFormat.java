package org.nbone.util.text;

import java.text.FieldPosition;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpringelFormat extends BaseFormat {

	private static final long serialVersionUID = -7495793606990372291L;
	
	private String pattern = "";
	
	protected static  ExpressionParser elParser;
	
    public SpringelFormat(String pattern) {
    	this.pattern = pattern;
    }
	                    
    public static String format(String pattern,Object dataModel) {
    	SpringelFormat temp = new SpringelFormat(pattern);
    	return temp.format(dataModel);
    }

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		StandardEvaluationContext context  = new StandardEvaluationContext();  
		
		
		context.setVariables((Map<String, Object>) obj);
		Expression  expression = elParser.parseExpression(pattern);
		
		String result  =expression.getValue(context, String.class);
		
		return new StringBuffer(result);
	}
	
	
	static{
		
		 ExpressionParser parser = new SpelExpressionParser();  
		 SpringelFormat.elParser = parser;
	}
	
	public static void main(String[] args) {
		 Map<String, Object> ctx = new HashMap<String, Object>();
		 ctx.put("name", "SpringelFormat");
		 ctx.put("date1", (new Date()).toString());
		 String content = "#name";
	
		 System.out.println(SpringelFormat.format(content, ctx));
		
		
	}

}
