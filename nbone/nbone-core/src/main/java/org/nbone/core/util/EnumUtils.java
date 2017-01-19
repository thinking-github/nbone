package org.nbone.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.lang.IEnum;
import org.springframework.util.ClassUtils;

/**
 * 
 * @author thinking
 *
 */
public class EnumUtils {

     private static  Log logger  = LogFactory.getLog(EnumUtils.class);
    /**
     * 获取枚举类的所有属性
     * 
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> IEnum<T>[] getEnums(Class<?> clazz) {
        if (IEnum.class.isAssignableFrom(clazz)) {
            Object[] enumConstants = clazz.getEnumConstants();
            return (IEnum<T>[]) enumConstants;
        }
        return null;
    }

    /**
     * 获取枚举类名称的所有属性
     * 
     * @param enumClass
     * @return
     */
    public static <T> IEnum<T>[] getEnums(String enumClazzName) {
        try {
        	Class<?> clazz = ClassUtils.forName(enumClazzName, ClassUtils.getDefaultClassLoader());
            return getEnums(clazz);
        } catch (ClassNotFoundException e) {
        	logger.error(e);
        }
		return null;
    }

    /**
     * 根据枚举编码获取枚举
     *
     * @param clazz the clazz
     * @param code the code
     * @return enum
     */
    @SuppressWarnings("unchecked")
    public static <T> T getEnum(Class<T> clazz, String code) {
        if (!IEnum.class.isAssignableFrom(clazz)) {
            return null;
        }
        IEnum<String>[] enumConstants = (IEnum[]) clazz.getEnumConstants();
        for (IEnum<String> enumConstant : enumConstants) {
            if (enumConstant.getCode().equalsIgnoreCase(code)) {
                return (T) enumConstant;
            }
        }
        return null;
    }
    /**
     * 根据枚举编码获取枚举
     *
     * @param clazz the clazz
     * @param code the code
     * @return enum
     */
    @SuppressWarnings("unchecked")
    public static <T> T getEnum(Class<T> clazz, Number code) {
        if (!IEnum.class.isAssignableFrom(clazz)) {
            return null;
        }
        IEnum<Number>[] enumConstants = (IEnum[]) clazz.getEnumConstants();
        for (IEnum<Number> enumConstant : enumConstants) {
            if (enumConstant.getCode().equals(code)) {
                return (T) enumConstant;
            }
        }
        return null;
    }

    /**
     * 根据枚举编码获取枚举
     *
     * @param clazzName the clazzName
     * @param code the code
     * @return enum
     */
    @SuppressWarnings("unchecked")
    public static <T> T getEnum(String clazzName, String code) {
        
		try {
			Class<?> clazz = ClassUtils.forName(clazzName, ClassUtils.getDefaultClassLoader());
			 return (T) getEnum(clazz, code);
			 
		} catch (ClassNotFoundException | LinkageError e) {
			logger.error(e);
		}
		return null;
    }


}
