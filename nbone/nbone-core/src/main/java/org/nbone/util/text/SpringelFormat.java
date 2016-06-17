package org.nbone.util.text;

import java.text.FieldPosition;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Spring expression  language (SPEl)
 * <p>#{expression?:default value}
 * <p> 安全的用法 #!{expression?:default value}
 * <p>${property:default value}
 * @author  thinking
 * @version 1.0 
 * @since 2015-12-12
 * @see org.springframework.expression.common.ExpressionUtils
 */
public class SpringelFormat extends BaseFormat {

	private static final long serialVersionUID = -7495793606990372291L;
	
	private String pattern = "";
	
	protected static  ExpressionParser elParser;
	protected static  ParserContext   parserContext;
	protected static  ParserContext   TEMPLATE_EXPRESSION = ParserContext.TEMPLATE_EXPRESSION;
	
    public SpringelFormat(String pattern) {
    	this.pattern = pattern;
    }
	    
    /**
     * 
     * @param pattern 模板字符串
     * @param dataModel 动态绑定变量
     * @return
     */
    public static String format(String pattern,Object dataModel) {
    	SpringelFormat temp = new SpringelFormat(pattern);
    	return temp.format(dataModel);
    }
    
    /**
     * 
     * @param pattern spring 表达式    1+3
     * @param clazz   转换成目标类型
     * @return
     */
    public static <T> T format(String pattern,Class<T> clazz) {
    	Expression  expression =  elParser.parseExpression(pattern);
    	
    	T result  = expression.getValue(clazz);
    	
    	return result;
    }
    
    

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		StandardEvaluationContext context  = createEvaluationContext(obj);
		
		Expression  expression = elParser.parseExpression(pattern,parserContext);
		
		StringBuffer result  =expression.getValue(context, StringBuffer.class);
		
		return result;
	}
	
	
	public static String format(ExpressionParser ep,String pattern, Object dataModel){
		if(ep == null){
			ep = SpringelFormat.elParser;
		}
		StandardEvaluationContext context = createEvaluationContext(dataModel);
	
		//raw return 
		if(context == null){
			return pattern;
		}
		Expression  expression = ep.parseExpression(pattern,parserContext);
		String result  = expression.getValue(context, String.class);
		
		return result;
	}
	
	
	protected static  StandardEvaluationContext createEvaluationContext(Object dataModel) {
		StandardEvaluationContext context = null;
		if(dataModel instanceof StandardEvaluationContext){
			
			context = (StandardEvaluationContext) dataModel;
			return context;
			
		}else if(dataModel instanceof Map){
			
			context = new StandardEvaluationContext();
			context.setVariables((Map<String, Object>) dataModel);
			return context;
		}
		
		return new StandardEvaluationContext();
	}
	
	
	
	static{
		
		 ExpressionParser parser = new SpelExpressionParser();  
		 ParserContext tpc = new TemplateParserContext("${","}");
		 SpringelFormat.elParser = parser;
		 SpringelFormat.parserContext = tpc;
	}
	
	public static void main(String[] args) {
		 Map<String, Object> ctx = new HashMap<String, Object>();
		 ctx.put("name", "SpringelFormat");
		 ctx.put("date1", (new Date()).toString());
		 String content = "6666666666666,,${#name},${ #date1 } --  ${#name1}--${1+2} 1+2";
	
		 System.out.println(SpringelFormat.format(content, ctx));
		 System.out.println(SpringelFormat.format("1+3*2", Long.class));
		
		
	}

}
