package org.nbone.util.text;

import java.io.Reader;
import java.io.StringWriter;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.nbone.constant.CharsetConstant;

/**
 * @author  thinking
 * @version 1.0 
 * @since 2015-12-12
 * @see org.springframework.ui.velocity.VelocityEngineUtils
 */
public class VlocityFormat extends BaseFormat implements CharsetConstant {

	private static final long serialVersionUID = 2955656626149507881L;
	
	private String pattern = "";
	
	private  static  VelocityEngine ve;
	
    public VlocityFormat(String pattern) {
    	this.pattern = pattern;
    }
   
    /**
     * @param pattern 模板字符串
     * @param context 动态绑定变量  VelocityContext,Map<String, Object> 
     * @return
     */
    public static String format(String pattern,Object dataModel) {
    	VlocityFormat temp = new VlocityFormat(pattern);
    	return temp.format(dataModel);
    }
    
    /**
     * 
     * @param reader  模板字符流
     * @param context 动态绑定变量   VelocityContext,Map<String, Object> 
     * @return
     */
    public static String format(Reader reader, Object dataModel) {
		
    	return VlocityFormat.format(VlocityFormat.ve, reader, dataModel);
    }
    
    
    
	
	@Override
	public StringBuffer format(Object dataModel, StringBuffer toAppendTo, FieldPosition pos) {
		
		String temp = VlocityFormat.format(VlocityFormat.ve, pattern, dataModel);
		
		if(this.pattern.equals(temp)){
			return new StringBuffer(temp).append(toAppendTo);
		}
		
		StringBuffer out  = new StringBuffer(temp);
		return out;
	}

	
	public static String format(VelocityEngine ve,String pattern, Object dataModel){
		if(ve == null){
			ve = VlocityFormat.ve;
		}
		Context context = createVelocityContext(dataModel);
	
		//raw return 
		if(context == null){
			return pattern;
		}
		
		StringWriter writer = new StringWriter();
		ve.evaluate(context, writer, "logTag", pattern);
		
		return writer.toString();
	}
	
	public static String format(VelocityEngine ve,Reader reader, Object dataModel){
		if(ve == null){
			ve = VlocityFormat.ve;
		}
		Context context = createVelocityContext(dataModel);
	
		//raw return 
		if(context == null){
			return reader.toString();
		}
		
		StringWriter writer = new StringWriter();
		ve.evaluate(context, writer, "logTag", reader);
		
		return writer.toString();
	}
	
	protected static  Context createVelocityContext(Object dataModel) {
		Context context = null;
		if(dataModel instanceof Context){
			
			context = (Context) dataModel;
			return context;
			
		}else if(dataModel instanceof Map){
			
			context = new VelocityContext((Map) dataModel);
			return context;
		}
		
		return null;
	}
	
	
	
	
	static {
		 VelocityEngine ve = new VelocityEngine();
		 ve.addProperty(Velocity.INPUT_ENCODING, CharsetConstant.CHARSET_UTF8);
		 ve.addProperty(Velocity.OUTPUT_ENCODING, CharsetConstant.CHARSET_UTF8);
		 //设置日志系统
		 ve.addProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, "");
		 
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

}
