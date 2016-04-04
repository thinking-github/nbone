package org.nbone.util.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/**
 * 参数化类型工具(泛型)
 * @author thinking
 * @since 2016-04-04
 * @see org.codehaus.jackson.type.TypeReference
 * @see com.alibaba.fastjson.TypeReference
 *
 */
public class ParameterizedTypeUtils {
	
    
    public static Type[] getActualTypeArguments(Class<?> clazz){
    	
    	Type superClass = clazz.getGenericSuperclass();
    	
    	if (superClass instanceof Class<?>) { // sanity check, should never happen
              throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }

    	Type[] type = ((ParameterizedType) superClass).getActualTypeArguments();
    	
		return type;
    	
    }
    
    public static Type getActualTypeArgument(Class<?> clazz){
    	
    	return getActualTypeArguments(clazz)[0];
    	
    }

}
