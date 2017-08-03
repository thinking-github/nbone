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
import org.nbone.constants.CharsetConstant;

/**
 * @author  thinking
 * @version 1.0 
 * @since 2015-12-12
 * @see org.springframework.ui.velocity.VelocityEngineUtils
 */
public class VlocityFormat extends BaseFormat implements CharsetConstant {

	private static final long serialVersionUID = 2955656626149507881L;
	
	private  static  VelocityEngine ve;
	
    public VlocityFormat(String pattern) {
    	super(pattern);
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
			
			context = new VelocityContext((Map<?,?>) dataModel);
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

}
