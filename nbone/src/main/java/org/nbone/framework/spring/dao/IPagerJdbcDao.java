package org.nbone.framework.spring.dao;

import java.sql.SQLException;
import java.util.Map;

import org.nbone.mx.datacontrols.datapage.PagerModel;
import org.springframework.jdbc.core.RowMapper;

/**
 * 分页Jdbc Dao 封装
 * @author Thinking 2014-08-04
 *
 */
public interface IPagerJdbcDao {
	
	
    public PagerModel<Map<String,Object>> findByPage(String sql) throws SQLException ;
   
	public PagerModel<Map<String,Object>> findByPage(String sql, Object[] datas) throws SQLException ;
	
	public PagerModel<Map<String,Object>> findByPage(String sql, Object[] datas,int[] types) throws SQLException ;
	
	
	
	public PagerModel<Map<String,Object>> findByPage(String sql,int offset,int pageSize) throws SQLException ;
	
	
	public PagerModel<Map<String,Object>> findByPage(String sql, Object[] datas, int offset,int pageSize) throws SQLException;
	
	
	public PagerModel<Map<String,Object>> findByPage(String sql, Object[] datas,int[] types, int offset,int pageSize) throws SQLException ;
    
	/**
	 * 分页函数
	 * @param sql     SQL字符串
	 * @param datas   预处理占位填充数据(?)
	 * @param types   预处理占位数据的数据类型
	 * @param rm      RowMapper
	 * @param offset  当前页
	 * @param pageSize 页的大小
	 * @return
	 * @throws SQLException
	 */
	public <T> PagerModel<T> findByPage(String sql, Object[] datas, int[] types ,RowMapper<T> rm, int offset, int pageSize) throws SQLException;

}
