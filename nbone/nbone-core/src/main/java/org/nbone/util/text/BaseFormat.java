package org.nbone.util.text;

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

	
	
    public static String format(String pattern, Object... arguments) {
    	return MessageFormat.format(pattern, arguments);
    }
    
    
    public static String format(String pattern, List<? extends Object> list) {
    	return MessageFormat.format(pattern, list.toArray());
    }
    
    
    /**
     * 
     * @param source
     * @param pos
     * @return  raw source
     */
	@Override
	public Object parseObject(String source, ParsePosition pos) {
		return source;
	}

}
