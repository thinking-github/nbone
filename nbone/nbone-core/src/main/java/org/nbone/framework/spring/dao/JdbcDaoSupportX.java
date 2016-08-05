package org.nbone.framework.spring.dao;

import javax.sql.DataSource;

import org.nbone.context.system.SystemContext;
import org.nbone.persistence.db.DBAdapter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
/**
 * @author thinking
 * @version 1.0 
 */
public class JdbcDaoSupportX extends JdbcDaoSupport implements ApplicationContextAware {
	
	private JdbcTemplate newJdbcTemplate;
	
	private ApplicationContext applicationContext;
	
	public void setNewJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.newJdbcTemplate = jdbcTemplate;
	}
	
    public JdbcTemplate getNewJdbcTemplate() {
		return newJdbcTemplate;
	}

   
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		this.initDataSource();
		initApplicationContext();
	}

	
	 /**
     * set  parent jdbc dataSource
     * @see JdbcDaoSupport#setDataSource(DataSource)
     * @param dataSource
     */
	protected void initDataSource(){
		try {
			JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
			super.setJdbcTemplate(jdbcTemplate);
			
		} catch (Exception e) {
			logger.warn("application not config  or config multiple JdbcTemplate.thinking");
			
			try {
				
				DataSource dataSource = applicationContext.getBean(DataSource.class);
				super.setDataSource(dataSource);
				
			} catch (Exception e2) {
				logger.warn("application not config or config multiple dataSource.thinking");
				try {
					DataSource dataSource = applicationContext.getBean("dataSource", DataSource.class);
					super.setDataSource(dataSource);
				} catch (Exception e3) {
					logger.warn(e3.getMessage());
					logger.warn("应用程序无法自动指定数据源,请手工指定数据源.thinking");
				}
				
			}
			
		}
		
	} 
	
	protected void initApplicationContext(){
		DataSource dataSource = getDataSource();
		if(dataSource != null){
			try {
				String dbType = DBAdapter.getDbName(dataSource);
				SystemContext.CURRENT_DB_TYPE = dbType;
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
	}
	

}
