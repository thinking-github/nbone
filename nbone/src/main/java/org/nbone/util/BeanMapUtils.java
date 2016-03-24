package org.nbone.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class BeanMapUtils {
	
	public final static String CLASS = "class" ;
	
	/**
	 * bean convert Map
	 * @param bean
	 * @return
	 */
	public static Map  populateMap(Object bean){
		Map  map = null;
		try {
			map = BeanUtils.describe(bean);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		map.remove(CLASS);
		return map;
	}
	
	/**
	 * Map convert Bean
	 * @param bean
	 * @param map
	 */
	public static void  populateBean(Object bean,Map map){
			 try {
				BeanUtils.populate(bean, map);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
	}
	
	
	
	
	

}
