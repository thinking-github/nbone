package org.nbone.framework.spring;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.nbone.framework.spring.dao.config.JdbcComponentConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author thinking
 * @version 1.0 
 * @see org.springframework.context.support.ClassPathXmlApplicationContext
 * @see org.nbone.framework.spring.dao.config.JdbcComponentConfig
 */
public class NboneApplication  implements BeanFactoryPostProcessor,ApplicationContextAware,InitializingBean{
	
	private List<String> configLocations = new ArrayList<String>();
	
	private ApplicationContext applicationContext;
	
	protected final static String spring_core = "org/nbone/framework/spring/config/spring-core.xml";
	protected final static String spring_dao = "org/nbone/framework/spring/config/spring-dao.xml";
	
	/**
	 * @see org.nbone.framework.spring.support.ComponentFactory
	 */
	private boolean enableComponentFactory = true;
	
	
	/**
	 * spring dao Support 
	 * @see org.nbone.framework.spring.dao
	 */
	private boolean enableJdbcDao = false;
	
	private DataSource useDataSource;


	public boolean isEnableComponentFactory() {
		return enableComponentFactory;
	}


	public void setEnableComponentFactory(boolean enableComponentFactory) {
		this.enableComponentFactory = enableComponentFactory;
	}


	public boolean isEnableJdbcDao() {
		return enableJdbcDao;
	}


	public void setEnableJdbcDao(boolean enableJdbcDao) {
		this.enableJdbcDao = enableJdbcDao;
	}


	public DataSource getUseDataSource() {
		return useDataSource;
	}


	public void setUseDataSource(DataSource useDataSource) {
		this.useDataSource = useDataSource;
	}

	
	
	//-------------------------------------------
	//init
	private void initComponentFactory(){
		if(enableComponentFactory){
			configLocations.add(spring_core);
		}
	}
	
	private void initJdbcDao(){
		if(enableJdbcDao){
			configLocations.add(spring_dao);
		}
		//默认自动加载,设置自己的数据源
		if(useDataSource != null){
			
		}
		
	}
	//-------------------------------------------------
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
		ConfigurableListableBeanFactory ss = beanFactory;
		System.out.println(ss);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	
	@Override
	public void afterPropertiesSet() {
		initComponentFactory();
		initJdbcDao();
		
		if(configLocations.size() > 0){
			String[] configs = new String[configLocations.size()];
			configs = configLocations.toArray(configs);
			ApplicationContext ctx = new ClassPathXmlApplicationContext(configs,applicationContext);
		
			postNboneApplication(ctx);
			
		}
		
		
		
	}


	protected void postNboneApplication(ApplicationContext applicationContext){
	
		postJdbcDao(applicationContext);
		
		
	}
	
	//-----------------------------------------------------
	//post
	private void postJdbcDao(ApplicationContext applicationContext){
		if(enableJdbcDao && useDataSource != null){
			JdbcComponentConfig.setDataSource(applicationContext, useDataSource);
			
		}
		
	}


	
	
	

}
