package org.nbone.framework.spring.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @author thinking
 * @since 2016-09-02
 * @version 1.0.1
 * @since spring 3.0
 * @see org.springframework.core.convert.support.StringToEnumConverterFactory
 *
 */
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

    @Override
    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnum<T>(targetType);
    }

    
    /**
     * 枚举标识编码转化成枚举
     * @author thinking
     *
     * @param <T>
     */
    private class StringToEnum<T extends Enum<?>> implements Converter<String, T> {

        private final Class<T> enumType;

        public StringToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
        	
        	if (source.length() == 0) {
				// It's an empty enum identifier: reset the enum value to null.
				return null;
			}
        	 T target = null;
            // 转换成数字
        	try {
				int ordinal = Integer.valueOf(source);
				if (ordinal == Integer.MIN_VALUE) {
					return null;
		        }
	            try {
	                Method getCode = enumType.getMethod("getCode");

	                T[] objects = enumType.getEnumConstants();
	                for (T ob : objects) {
	                    int temps = (int) getCode.invoke(ob);
	                    if (temps == ordinal) {
	                    	target = ob;
	                        break;
	                    }

	                }

	            } catch (NoSuchMethodException e) {
	                e.printStackTrace();
	            } catch (InvocationTargetException e) {
	                e.printStackTrace();
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            }
	            
	     
			} catch (NumberFormatException e) {
				// 使用字符的方式转换
				target = stringToEnum(source);
			}
      
            return target;
        }
        
        private T stringToEnum(String source){
        	T target = null;
            try {
                Method getCode = enumType.getMethod("getCode");

                T[] objects = enumType.getEnumConstants();
                for (T ob : objects) {
                    Object temp =  getCode.invoke(ob);
                    if (source.equals(temp)) {
                    	target = ob;
                        break;
                    }

                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            
			return target;
        }
        

    }
}