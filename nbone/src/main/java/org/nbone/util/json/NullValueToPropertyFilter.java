package org.nbone.util.json;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
/**
 * java object to json 将null值转化成目标值
 * <li> Collection or object[] is null -> []
 * <li> pojo or map is null -> {}
 * @author thinking
 * @since 2016-04-04
 *
 */
public class NullValueToPropertyFilter implements net.sf.json.util.PropertyFilter {

	@Override
	public boolean apply(Object source, String name, Object value) {
		
		if(value == null){
			Class<?> targetClass = null;
			try {
				targetClass = 	PropertyUtils.getPropertyType(source, name);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			if(targetClass.isAssignableFrom(Collection.class)){
				value = new ArrayList<Object>();
				
			} else if (targetClass.isArray()){
				value = new Object[0];
				
			}else if (targetClass.isAssignableFrom(Map.class)){
				value = new HashMap<String,Object>();
				
			}
			
		}
		
		return false;
	}

}
