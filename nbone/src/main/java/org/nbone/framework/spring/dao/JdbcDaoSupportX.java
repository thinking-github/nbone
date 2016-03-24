package org.nbone.framework.spring.dao;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

//@Repository
public class JdbcDaoSupportX extends JdbcDaoSupport {
	
	private JdbcTemplate newJdbcTemplate;
	
	public void setNewJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.newJdbcTemplate = jdbcTemplate;
	}
	
    public JdbcTemplate getNewJdbcTemplate() {
		return newJdbcTemplate;
	}

    /**
     * set  parent jdbc dataSource
     * @see JdbcDaoSupport#setDataSource(DataSource)
     * @param dataSource
     */
	@Resource(name = "dataSource")
	public void setSuperDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}
	
	
	
	

}
