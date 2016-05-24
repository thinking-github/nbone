package org.nbone.util.text;

import java.io.Reader;
import java.io.StringWriter;
import java.text.FieldPosition;
import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.nbone.constant.CharsetConstant;

/**
 * @author  thinking
 * @version 1.0 
 * @since 2015-12-12
 * @see  java.text.MessageFormat
 * @see  java.text.Format
 */
public class VlocityFormat extends Format implements CharsetConstant {

	private static final long serialVersionUID = 2955656626149507881L;
	
	private String pattern = "";
	
	private  static  VelocityEngine ve;
	
    public VlocityFormat(String pattern) {
    	this.pattern = pattern;
    }

    /**
     * @param pattern 模板字符串
     * @param context 动态绑定变量
     * @return
     */
    public static String format(String pattern,Map<String, Object> contextMap) {
    	return format(pattern, new VelocityContext(contextMap));
    }
   
    public static String format(String pattern, VelocityContext context) {
    	VlocityFormat temp = new VlocityFormat(pattern);
    	return temp.format(context);
    }
    
    
    /**
     * 
     * @param reader  模板字符流
     * @param context 动态绑定变量
     * @return
     */
    public static String format(Reader reader, Map<String, Object> contextMap) {
    	return format(reader, new VelocityContext(contextMap));
    }
   
    
    public static String format(Reader reader, VelocityContext context) {
    	StringWriter writer = new StringWriter();
		VlocityFormat.ve.evaluate(context, writer,"logTag",reader);
    	return writer.toString();
    }
    
	
	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		StringWriter writer = new StringWriter();
		VlocityFormat.ve.evaluate((Context) obj, writer,"logTag", this.pattern);
		StringBuffer out  = new StringBuffer(writer.toString());
		return out;
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		return null;
	}
	
	
	static {
		 VelocityEngine ve = new VelocityEngine();
		 ve.init();
		 
		 VlocityFormat.ve = ve;
	}
	
	
	
	
	public static void main(String[] args) {
		 VelocityContext ctx = new VelocityContext();
		 ctx.put("name", "velocity");
		 ctx.put("date1", (new Date()).toString());
		 String content = "";
		 content += "Welcome  $name  to Javayou.com! ";
		 content += "---- $date1 ------  ";
		 
		 
		 System.out.println(VlocityFormat.format(content, ctx));
		 System.out.println(MessageFormat.format("你好数字{0}-{1}", "33","99"));
		 
	}

}
