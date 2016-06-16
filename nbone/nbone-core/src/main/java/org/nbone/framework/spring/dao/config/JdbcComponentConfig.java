package org.nbone.framework.spring.dao.config;

import java.util.Map;

import javax.sql.DataSource;

import org.nbone.framework.spring.dao.JdbcDaoSupportX;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
/**
 * <p> 详细使用方法见@Configuration javadoc
 * 
 * @author thinking
 * @version 1.0 
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.context.annotation.AnnotationConfigApplicationContext
 * @since spring 3.1
 * 
 */
@Configuration
@ComponentScan(basePackages="org.nbone.framework.spring.dao")
@PropertySource("classpath:/org/nbone/framework/spring/dao/config/jdbc-comp.properties")
public class JdbcComponentConfig implements ApplicationContextAware,InitializingBean{
	
	@Autowired
	private Environment env;
	
	private ApplicationContext applicationContext;
	
	
	private DataSource useDataSource;
	
	
	public DataSource getUseDataSource() {
		return useDataSource;
	}

	public void setUseDataSource(DataSource useDataSource) {
		this.useDataSource = useDataSource;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		setDataSource(this.applicationContext, useDataSource);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String name  = env.getProperty("nbone.name");
		String dataSourceName = env.getProperty("nbone.datasource.name");
		System.out.println("========================="+name);
		System.out.println("========================="+dataSourceName);
	}

	
	public static void setDataSource(ApplicationContext applicationContext,DataSource dataSource){
		if(applicationContext == null){
			return ;
		}
		if(dataSource == null){
			return;
		}
		Map<String,JdbcDaoSupportX> supportMap = applicationContext.getBeansOfType(JdbcDaoSupportX.class);
		for (Map.Entry<String, JdbcDaoSupportX> entry: supportMap.entrySet()) {
			entry.getValue().setDataSource(dataSource);
			
		}
		
	}
	

}
