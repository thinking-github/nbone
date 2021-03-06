package org.nbone.framework.spring.dao.config;

import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.constants.CaseName;
import org.nbone.framework.spring.SpringEnv;
import org.nbone.framework.spring.dao.JdbcDaoSupportX;
import org.nbone.framework.spring.support.ComponentFactory;
import org.nbone.persistence.JdbcOptions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
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
@Configuration("jdbcComponent")
@ComponentScan(basePackages="org.nbone.framework.spring.dao")
@PropertySource(name="jdbcComponentConig",value="classpath:/org/nbone/framework/spring/dao/config/jdbc-comp.properties")
@Import(ComponentFactory.class)
public class JdbcComponentConfig implements ApplicationContextAware,InitializingBean{
	
	protected static Log logger  =  LogFactory.getLog(JdbcComponentConfig.class);
	final static String SystemProperties = "#!{@jdbcComponentConig}";
	@Autowired
	private ConfigurableEnvironment env;
	
	@Value(SystemProperties)
	private Properties config;
	
	@Value("${nbone.datasource.name:dataSource11}")
	private String dataSourceName;
	
	private ApplicationContext applicationContext;
	
	
	private DataSource dataSource;
	
	
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 双数据源时使用
	 */
	private DataSource dataSource1;
	/**
	 * 双数据源时使用
	 */
	private JdbcTemplate jdbcTemplate1;
	
	private boolean debug = false;
	private boolean showSql = false;

	/**
	 * 数据库命名规则
	 */
	private CaseName dbCaseName = CaseName.LOWER_UNDERSCORE;
	/**
	 *  resultMap id  "BaseResultMap"
	 */
	private String  mybatisMapperId = "BaseResultMap";
	
	

	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public DataSource getDataSource1() {
		return dataSource1;
	}

	public void setDataSource1(DataSource dataSource1) {
		this.dataSource1 = dataSource1;
	}

	public JdbcTemplate getJdbcTemplate1() {
		return jdbcTemplate1;
	}

	public void setJdbcTemplate1(JdbcTemplate jdbcTemplate1) {
		this.jdbcTemplate1 = jdbcTemplate1;
	}

	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isShowSql() {
		return showSql;
	}

	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
		JdbcOptions.showSql = showSql; 
	}


	public CaseName getDbCaseName() {
		return dbCaseName;
	}

	public void setDbCaseName(CaseName dbCaseName) {
		this.dbCaseName = dbCaseName;
	}

	public String getMybatisMapperId() {
		return mybatisMapperId;
	}

	public void setMybatisMapperId(String mybatisMapperId) {
		this.mybatisMapperId = mybatisMapperId;
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		ComponentFactory.setContext(applicationContext);

		setJdbcTemplate(this.applicationContext, jdbcTemplate);
		setDataSource(this.applicationContext, dataSource);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("========================================================================");
		logger.info("nbone JdbcComponent starting ....");
		logger.info("========================================================================");
		org.springframework.core.env.PropertySource<?> ps =  env.getPropertySources().get("jdbcComponentConig");
		if(ps != null){
			config = (Properties) ps.getSource();
		}
		String name  = env.getProperty("nbone.name");
		String dataSourceName = env.getProperty("nbone.datasource.name");
		System.out.println("========================="+name);
		System.out.println("========================="+dataSourceName);
		System.out.println("========================="+mybatisMapperId);
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
			logger.warn("dataSource is null.thinking.");
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
			logger.warn("jdbcTemplate is null.thinking.");
			return;
		}
		Map<String,JdbcDaoSupportX> supportMap = applicationContext.getBeansOfType(JdbcDaoSupportX.class);
		for (Map.Entry<String, JdbcDaoSupportX> entry: supportMap.entrySet()) {
			entry.getValue().setJdbcTemplate(jdbcTemplate);
			
		}
		
	}
	
	

}
