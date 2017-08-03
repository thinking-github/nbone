package org.nbone.persistence.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;


public class JdbcDao  {
	private JdbcTemplate jdbcTemplate;
    /**
     * 用于测试
     * @return
     */
    public long queryForInt() {
    	String sql="select count(*) from meeting";
    	long count = jdbcTemplate.queryForObject(sql, Long.class);
		return count;
		
	}
		

}
