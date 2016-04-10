package org.nbone.util.lang;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
/**
 * object to String  format 
 * @author thinking
 * @version 1.0
 * @since   2016年4月4日
 *
 */
public class ToStringUtils {
	/**
	 * 多行模式格式化
	 * @param object
	 * @return
	 */
	public static String toStringMultiLine(Object object){
		return ToStringBuilder.reflectionToString(object,ToStringStyle.MULTI_LINE_STYLE);
	}
	/**
	 * 单行模式格式化
	 * @param object
	 * @return
	 */
	public static String toString(Object object){
		return ToStringBuilder.reflectionToString(object);
	}
	
	

}
