package org.nbone.persistence.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;


public class JdbcDao  {
	private JdbcTemplate jdbcTemplate;
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
