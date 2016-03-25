package org.nbone.framework.spring.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
/**
 * get Spring ApplicationContext bean<br>
 * 
 * note: use  ComponentFactory must IOC Spring
 * @author Thinking
 * @version 1.0 2014-05-14
 *
 */
@Component
public class ComponentFactory implements ApplicationContextAware {
	
	private static Log logger =  LogFactory.getLog(ComponentFactory.class);
	private static ApplicationContext applicationContext;
    private static String message = "请先将ComponentFactory注入到spring container.thinking";

	public ComponentFactory() {
	}

	public void setApplicationContext(ApplicationContext context)throws BeansException {
		applicationContext = context;
	}

	public static ApplicationContext getContext() {
		return applicationContext;
	}
	public static  Object getBean(String id){
		if(id == null){
         return null;
		}
		Assert.notNull(applicationContext,message);
		Object bean =  applicationContext.getBean(id);
		
		return bean;
	}
	
	/**
	 * 以后尽量使用含泛型的方法,方便使用不用强制类型转换
	 * @param name
	 * @param requiredType
	 * @return
	 */
	public static  <T> T getBean(String name,Class<T> requiredType){
		if(name == null){
         return null;
		}
		Assert.notNull(applicationContext,message);
		T bean =  applicationContext.getBean(name, requiredType);
		
		return bean;
	}
	
	/**
	 * byClass<br>
	 * Bean 的定义有且只有一个 多个或者没有抛出异常
	 * @param requiredType
	 * @return
	 */
	public static  <T> T getBean(Class<T> requiredType){
		if(requiredType == null){
         return null;
		}
		Assert.notNull(applicationContext,message);
		T bean =  applicationContext.getBean(requiredType);
		
		return bean;
	}
	
	
	
	
	/**
	 * input bean Is Null New Instance
	 * @param object input object 
	 * @param clazz  input object of class
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static  <T> T beanIsNullNewInstance(Object object,Class<T> clazz){
		if(object != null ){
			return (T) object;
		}
		T instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			logger.error("clazz.newInstance exception", e);
		}
		
		return instance;
	}
    
	
}
