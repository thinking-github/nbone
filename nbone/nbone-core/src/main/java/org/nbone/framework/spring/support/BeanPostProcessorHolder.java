package org.nbone.framework.spring.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 针对单个Bean实例化前置后置回调
 * @author thinking
 * @version 1.0 
 * @since 2015-12-14
 */
public class BeanPostProcessorHolder implements BeanPostProcessor {
	protected static Log logger =  LogFactory.getLog(BeanPostProcessorHolder.class);
	
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
