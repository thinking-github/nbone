package org.nbone.framework.spring.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
/**
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候中取出ApplicaitonContext.<p>
 * get Spring ApplicationContext bean<br> <p>
 * 
 * note: use  ComponentFactory must IOC Spring
 * @author thinking
 * @version 1.0 
 * @since 2013-12-14
 *
 */
@Component
public class ComponentFactory implements ApplicationContextAware,InitializingBean,DisposableBean {
	
	private static Log logger =  LogFactory.getLog(ComponentFactory.class);
	private static ApplicationContext applicationContext;
    private static String message = "请先将ComponentFactory注入到spring container.thinking";

	public ComponentFactory() {
	}
    /**
     * {@inheritDoc}     
     */
	@Override
	public void setApplicationContext(ApplicationContext context)throws BeansException {
		applicationContext = context;
		logger.debug("注入ApplicationContext到SpringContextHolder:"+ applicationContext);
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
	 * 尽量使用含泛型的方法,方便使用不用强制类型转换
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
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clear() {
		applicationContext = null;
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
	/**
	 * 实现DisposableBean接口,在Context关闭时清理静态变量.
	 */
	@Override
	public void destroy() throws Exception {
		clear();
	}
	/**
	 * 实现InitializingBean接口,在Context初始化后加载自定义变量.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
    
	
}
