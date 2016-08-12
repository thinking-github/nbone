package org.nbone.util.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.List;

/**
 * @author  thinking
 * @version 1.0 
 * @since 2015-12-12
 * @see  java.text.MessageFormat
 * @see  java.text.Format
 */
public abstract  class BaseFormat extends Format {

	private static final long serialVersionUID = 2889536124343249496L;
	
	protected String pattern = "";
	
    public BaseFormat(String pattern) {
		this.pattern = pattern;
	}

	public static String format(String pattern, Object... arguments) {
    	return MessageFormat.format(pattern, arguments);
    }
    
    
    public static String format(String pattern, List<? extends Object> list) {
    	return MessageFormat.format(pattern, list.toArray());
    }
    
    /**
     * 目前只使用第一个参数  (后两个参数用于特殊扩展时使用)
     * @author thinking
     * @param obj
     * @param toAppendTo
     * @param pos
     * @see Format#format(Object, StringBuffer, FieldPosition)
     */
    @Override
	public abstract StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos);


	/**
     * 
     * @param source
     * @param pos
     * @return  raw source
     * @see Format#parseObject(String, ParsePosition)
     */
	@Override
	public Object parseObject(String source, ParsePosition pos) {
		return source;
	}


	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
}
