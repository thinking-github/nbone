package org.nbone.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 
 * @author thinking
 * @version 1.0 
 * @see org.apache.commons.beanutils.BeanUtils
 */
public class BeanMapUtils {
	
	public final static String CLASS = "class" ;
	
	private static final Log logger = LogFactory.getLog(BeanUtils.class);
	
	/**
	 * bean convert Map
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object>  populateMap(Object bean){
		Map<String,Object>  map = null;
		try {
			map = BeanUtils.describe(bean);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		} catch (NoSuchMethodException e) {
			logger.error(e);
		}
		map.remove(CLASS);
		return map;
	}
	
	/**
	 * Map convert Bean
	 * @param bean
	 * @param map
	 */
	public static void  populateBean(Object bean,Map<String,Object> map){
		try {
			BeanUtils.populate(bean, map);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		}
	}
	
	
	
	
	

}
