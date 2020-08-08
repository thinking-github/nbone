package org.nbone.util;

import java.math.BigDecimal;

import org.springframework.util.Assert;

/**
 * 
 * @author thinking
 * @version 1.0 
 */
public class ClassUtils {
	/** The package separator character '.' */
	private static final char PACKAGE_SEPARATOR = '.';

	/**
	 * 如果是lang package 省略java.lang
	 * @param className
	 * @return
	 */
	public static String getSmartClassName(String className){
		Assert.hasLength(className, "Class name must not be empty");
		int index = className.lastIndexOf(PACKAGE_SEPARATOR);
		if(index != -1){
		   String namespace = className.substring(0, index);
		   if(namespace.equals("java.lang")){
			   return className.substring(index+1);
		   }
	   }
        
		return className;
	}
	
	public static String getSmartClassName(Class<?> clazz){
		return getSmartClassName(clazz.getName());
	}
	

}
