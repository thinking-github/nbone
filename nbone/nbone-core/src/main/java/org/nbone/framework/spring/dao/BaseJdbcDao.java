package org.nbone.framework.spring.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Repository;


/**
 * 
 * 基础jdbcDao接口,定义通用的操作
 * @author xun 2010年05月27日
 * @author Thinking  2014-6-20
 * @version v1.1 
 * 
 */
@Repository("baseJdbcDao")
public class BaseJdbcDao extends JdbcDaoSupportX implements IBaseJdbcDao {
	
	
	/**
	 * 当扩展的方法无法实现新的需求是可用此方法获取Spring SuperJdbcTemplate
	 */
	public Connection getSuperConnection(){
		return this.getConnection();
	}
	
	/**
	 * 当扩展的方法无法实现新的需求是可用此方法获取Spring SuperJdbcTemplate
	 */
	public JdbcTemplate getSuperJdbcTemplate(){
		
		return this.getJdbcTemplate();
	}
	  //**************************************queryForBean*************************************************
      
	/**
	 * @see #queryForBeanWithSql(String, Object[], int[], Class)
	 */
	public  <T> T queryForBeanWithSql(String sql,Class<T> clazz) throws SQLException{
		  
		return  queryForBeanWithSql(sql, null, null, clazz);
		   
	  }
	/**
	 * @see #queryForBeanWithSql(String, Object[], int[], Class)
	 */
	  public <T> T queryForBeanWithSql(String sql,Object[] datas,Class<T> clazz) throws SQLException{
		  
		return queryForBeanWithSql(sql, datas, null, clazz);
			
	  }
	  
	public <T> T queryForBeanWithSql(String sql,Object[] datas, int[] argTypes,Class<T> clazz) throws SQLException{
		  List<T> list = null;
		  RowMapper<T> rm = new BeanPropertyRowMapper<T>(clazz);
		  try {
			  JdbcTemplate dao = this.getJdbcTemplate();
			  if(datas != null && argTypes != null){
				  list = dao .query(sql, datas, argTypes,rm);
			  }
			  else if(datas != null && argTypes ==null){
				  list = dao.query(sql, datas,rm);
			  }
			  else if(datas == null){
				  list = dao.query(sql,rm);
			  }
			  else{
				  
			  }
			
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
		int size = 0;
		T returnObj = null ;
		if(list != null && (size=list.size()) > 0){
			returnObj = list.get(0);
		}
		if(size ==0){
			logger.warn("queryForBeanWithSql Return result Size 0 .Thinking");
		}
		if(size > 1){
			logger.warn("queryForBeanWithSql Return result Size > 1 .Thinking");
		}
		  
		  return returnObj;
	  }
	
	  //**********************************queryForMap**********************************************
	  /**
	   * @see #queryForMapWithSql(String, Object[], int[])
	   */
	  public Map<String,Object> queryForMapWithSql(String sql) throws SQLException{
		  
		  return queryForMapWithSql(sql, null, null);
		  
	  }
		/**
		 * @see #queryForMapWithSql(String, Object[], int[])
		 */
	  public Map<String,Object> queryForMapWithSql(String sql,Object[] datas) throws SQLException{
		  
		  return queryForMapWithSql(sql, datas, null);
		  
	  }
		
	  public Map<String,Object> queryForMapWithSql(String sql,Object[] datas, int[] argTypes) throws SQLException{
		  List<Map<String,Object>> list = null;
		  Map<String,Object> map = null;
		  try {
			  JdbcTemplate dao = this.getJdbcTemplate();
			  if(datas != null && argTypes != null){
				  list = dao .queryForList(sql, datas, argTypes);
			  }
			  else if(datas != null && argTypes ==null){
				  list = dao.queryForList(sql, datas);
			  }
			  else if(datas == null){
				  list = dao.queryForList(sql);
			  }
			  else{
				  
			  }
			
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
		int size = 0;
		if(list != null && (size=list.size()) > 0){
			map = list.get(0);
		}
		if(size ==0){
			logger.warn("QueryForMapWithSql Return result Size 0 .Thinking");
		}
		if(size > 1){
			logger.warn("QueryForMapWithSql Return result Size > 1 .Thinking");
		}
		  
		  return map;
	  }
    //***********************************queryForList**********************************************
	public List<Map<String,Object>> queryForListWithSql(String sql) throws SQLException {
		List<Map<String,Object>> list = null;
		try {
			list = this.getJdbcTemplate().queryForList(sql);
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
		return list;
	}
	
	public List<Map<String,Object>> queryForListWithSql(String sql,Object[] datas) throws SQLException{
		List<Map<String,Object>> list = null;
		try {
			list = this.getJdbcTemplate().queryForList(sql, datas);
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
		return list;
	}
	
	public List<Map<String,Object>>  queryForListWithSql(String sql,Object[] datas,int[] argTypes) throws SQLException{
		List<Map<String,Object>>  list = null;
		try {
			list = this.getJdbcTemplate().queryForList(sql, datas, argTypes);
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
		return list;
	}
	
	
	
	public <T> List<T> queryForListWithSql(String sql, RowMapper<T> rm) throws SQLException{
		List<T> list = null;
		try {
			list = this.getJdbcTemplate().query(sql, rm);
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
		return list;
		
	}

	public <T> List<T> queryForListWithSql(String sql,Object[] datas,RowMapper<T> rm) throws SQLException{
		List<T> list = null;
		try {
			list = this.getJdbcTemplate().query(sql,datas,rm);
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
		return list;
		
	}
	public <T> List<T> queryForListWithSql(String sql,Object[] datas,int[] argTypes,RowMapper<T> rm) throws SQLException{
		List<T> list = null;
		try {
			list = this.getJdbcTemplate().query(sql,datas,argTypes,rm);
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
		return list;
	}
    //**********************************update*****************************************	
	/**
	 * @see #updateWithSql(String, Object[], int[])
	 */
	public int updateWithSql(String sql) throws SQLException {
	
		return updateWithSql(sql, null, null);
		
	}
	/**
	 * @see #updateWithSql(String, Object[], int[])
	 */
	public int updateWithSql(String sql, Object[] args) throws SQLException {
		
		return updateWithSql(sql, args, null);
		
	}
	
	public int updateWithSql(String sql, Object[] args,int[] argTypes) throws SQLException{
		
		int rows = 0;
		try {
			JdbcTemplate dao = this.getJdbcTemplate();
			if(args != null && argTypes != null ){
				rows = dao.update(sql, args, argTypes);
			}
			else if(args != null && argTypes == null ){
				rows = dao.update(sql, args);
			}
			else if(args == null){
				rows = dao.update(sql);
			}
			else{
				
			}
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
		return rows;
	}

	public void batchUpdate(String[] sqls) throws SQLException {
		try {
			this.getJdbcTemplate().batchUpdate(sqls);
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
	}

	public void batchUpdate(List<String> list) throws SQLException {
		try {
			String[] sqls = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				sqls[i] = list.get(i);
			}
			this.getJdbcTemplate().batchUpdate(sqls);
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
	}
	
	/**
	 * @see #batchUpdateByParam(String, List, int[])
	 */
	public int[] batchUpdateByParam(String sql, List<Object[]> list) throws SQLException{

		return batchUpdateByParam(sql, list,null);
	}

	public int[] batchUpdateByParam(String sql, final List<Object[]> list, final int[] sqlTypes)
			throws SQLException {
		try {
			final int size  = list.size();
			BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i)throws SQLException {
					
					Object[] obj = (Object[]) list.get(i);
					int paramsSzie  = obj.length;
					
					for (int j = 0; j < paramsSzie; j++) {
						if(sqlTypes == null){
							ps.setObject(j + 1, obj[j]);
						}
						else{
							ps.setObject(j + 1, obj[j], sqlTypes[j]);
						}
					}
				}

				public int getBatchSize() {
					return size;
				}
			};
			return this.getJdbcTemplate().batchUpdate(sql, setter);
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
	}
    //BatchSize==100
	public void batchSqlUpdate(String sql, List<Object[]> list, int[] sqlTypes)
			throws SQLException {
		int size  = list.size();
		
		if(size > 100){
			logger.warn(new StringBuilder().append("批量更新数据偏大:").append("[").append(size).append("]."));
		}
		try {
			BatchSqlUpdate bsu = new BatchSqlUpdate(this.getDataSource(), sql,sqlTypes);
			bsu.setBatchSize(size);
			for (int i = 0; i < size; ++i) {
				bsu.update(list.get(i));
			}
			bsu.flush();
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
	}



	public void execute(String sql) throws SQLException {
		try {
			this.getJdbcTemplate().execute(sql);
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
	}

	public long queryForLong(String sql) throws SQLException {
		long count;
		try {
			count = this.getJdbcTemplate().queryForLong(sql);
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
		return count;
	}
	
	public int queryForInt(String sql) throws SQLException{
		int count;
		try {
			count = this.getJdbcTemplate().queryForInt(sql);
		} catch (DataAccessException e) {
			throw new SQLException(e);
		}
		return count;
		
	}
	
}
