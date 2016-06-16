package org.nbone.persistence.jdbc;

import javax.sql.DataSource;

import org.nbone.framework.spring.dao.JdbcDaoSupportX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;


@Repository(value="jdbcDao")
public class JdbcDao extends JdbcDaoSupportX {
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 将 bean dataSource 注入到
     * {@link JdbcDaoSupport#setDataSource(DataSource)}
	 * @param dataSource
	 */
    @Autowired
	public void setSuperDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
		this.jdbcTemplate=this.getJdbcTemplate();
	}
    
    /**
     * 用于测试
     * @return
     */
    public int queryForInt() {
    	String sql="select count(*) from meeting";
    	int count=jdbcTemplate.queryForInt(sql);
		return count;
		
	}
		

}
