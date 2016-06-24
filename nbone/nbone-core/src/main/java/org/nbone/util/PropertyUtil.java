package org.nbone.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

/**
 * 
 * @author thinking
 * @version 1.0 
 * @see  org.apache.commons.beanutils.PropertyUtils
 */
public class PropertyUtil {
	
	private static final Log logger = LogFactory.getLog(BeanUtils.class);
	
	 public static Object getProperty(Object bean, String name){
		 Object value = null;
		try {
			value = PropertyUtils.getProperty(bean, name);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		} catch (NoSuchMethodException e) {
			logger.error(e);
		}
		return value;
	}
	 
	 public static void setProperty(Object bean, String name, Object value){
		try {
			PropertyUtils.setProperty(bean, name, value);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		} catch (NoSuchMethodException e) {
			logger.error(e);
		}
	}
	

}
