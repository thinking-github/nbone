package org.nbone.framework.spring.dao.config;

import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.nbone.framework.spring.dao.JdbcDaoSupportX;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
/**
 * <p> 详细使用方法见@Configuration javadoc
 * 
 * @author thinking
 * @version 1.0 
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.core.env.Environment
 * @see org.springframework.core.env.StandardEnvironment
 * @see org.springframework.context.annotation.AnnotationConfigApplicationContext
 * @see org.springframework.context.support.PropertySourcesPlaceholderConfigurer
 * @since spring 3.1
 * 
 */
@Configuration
@ComponentScan(basePackages="org.nbone.framework.spring.dao")
@PropertySource(name="jdbcComponentConig",value="classpath:/org/nbone/framework/spring/dao/config/jdbc-comp.properties")
@Lazy
public class JdbcComponentConfig implements ApplicationContextAware,InitializingBean{
	
	@Autowired
	private ConfigurableEnvironment env;
	
	@Value("#!{systemProperties ?: java.util.Properties()}")
	private Properties config;
	
	@Value("${nbone.datasource.name:dataSource11}")
	private String dataSourceName;
	
	private ApplicationContext applicationContext;
	
	
	private DataSource useDataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	
	public DataSource getUseDataSource() {
		return useDataSource;
	}

	public void setUseDataSource(DataSource useDataSource) {
		this.useDataSource = useDataSource;
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		setJdbcTemplate(this.applicationContext, jdbcTemplate);
		setDataSource(this.applicationContext, useDataSource);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.core.env.PropertySource<?> ps =  env.getPropertySources().get("jdbcComponentConig");
		config = (Properties) ps.getSource();
		String name  = env.getProperty("nbone.name");
		String dataSourceName = env.getProperty("nbone.datasource.name");
		System.out.println("========================="+name);
		System.out.println("========================="+dataSourceName);
	}


	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
	    return new PropertySourcesPlaceholderConfigurer();
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
	
	public static void setJdbcTemplate(ApplicationContext applicationContext,JdbcTemplate jdbcTemplate){
		if(applicationContext == null){
			return ;
		}
		if(jdbcTemplate == null){
			return;
		}
		Map<String,JdbcDaoSupportX> supportMap = applicationContext.getBeansOfType(JdbcDaoSupportX.class);
		for (Map.Entry<String, JdbcDaoSupportX> entry: supportMap.entrySet()) {
			entry.getValue().setJdbcTemplate(jdbcTemplate);
			
		}
		
	}
	
	
	

}
