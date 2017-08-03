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
 * @see ApplicationContextAware
 * @see org.springframework.context.support.ApplicationObjectSupport
 * @see org.springframework.beans.factory.BeanFactoryAware
 * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor
 *
 */
@Component
public class ComponentFactory implements ApplicationContextAware,InitializingBean,DisposableBean {
	
	protected static Log logger =  LogFactory.getLog(ComponentFactory.class);
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
		logger.info("注入ApplicationContext到SpringContextHolder:"+ applicationContext);
	}

	public static void setContext(ApplicationContext context) {
		applicationContext = context;
		logger.info("手动注入ApplicationContext到SpringContextHolder:"+ applicationContext);
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
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }
	
    
    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name
     * @return boolean
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     *
     */
    public static boolean isSingleton(String name)  {
        return applicationContext.isSingleton(name);
    }
    
    /**
     * @param name
     * @return Class 注册对象的类型
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     *
     */
    public static Class<?> getType(String name) {
        return applicationContext.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name
     * @return
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     *
     */
    public static String[] getAliases(String name) {
        return applicationContext.getAliases(name);
    }
	
	
	
	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clear() {
		applicationContext = null;
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
	
	//---------------------------------------------------------------------
	/**
	 * input bean Is Null New Instance
	 * @param object input object 
	 * @param clazz  input object of class
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static  <T> T beanIsNullNewInstance(Object object,Class<T> clazz) {
		if(object != null ) {
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
