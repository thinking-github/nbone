package org.nbone.framework.spring.dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 封装spring jdbc
 * @author Thinking  2014-6-20
 * @version 1.0
 *
 */

public interface IBaseJdbcDao {
	
	/**
	 * 当扩展的方法无法实现新的需求是可用此方法获取Spring SuperConnection
	 */
		
	public Connection getSuperConnection();
	/**
	 *  当扩展的方法无法实现新的需求是可用此方法获取Spring SuperJdbcTemplate
	 * @return
	 */
	public JdbcTemplate getSuperJdbcTemplate();
	/**
	 * 查询单行结果集，封装成Bean
	 */
    public <T> T  queryForBeanWithSql(String sql,Class<T> clazz) throws SQLException;
	
	public <T> T queryForBeanWithSql(String sql,Object[] datas,Class<T> clazz) throws SQLException;
	/**
	 * 查询单行结果集，封装成Bean
	 * @param sql
	 * @param datas
	 * @param argTypes
	 * @param clazz  mapingClass
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForBeanWithSql(String sql,Object[] datas, int[] argTypes,Class<T> clazz) throws SQLException;
	

	
	/**
	 * 查询单行结果集，封装成Map
	 */
    public Map<String,Object> queryForMapWithSql(String sql) throws SQLException;
	
	public Map<String,Object> queryForMapWithSql(String sql,Object[] datas) throws SQLException;
	/**
	 * 查询单行结果集，封装成Map
	 * @param sql
	 * @param datas
	 * @param argTypes
	 * @return
	 * @throws SQLException
	 */
	public Map<String,Object> queryForMapWithSql(String sql,Object[] datas, int[] argTypes) throws SQLException;
	
	
	
	/**
	 * 查询多行结果集，封装成List
	 */
	public List<Map<String,Object>> queryForListWithSql(String sql) throws SQLException;
	
	public List<Map<String,Object>> queryForListWithSql(String sql,Object[] datas) throws SQLException;
	
	public List<Map<String,Object>> queryForListWithSql(String sql,Object[] datas,int[] argTypes) throws SQLException;
	
	
	/**
	 * 根据 RowMapper映射类型返回
	 * @param sql
	 * @param rm
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForListWithSql(String sql,RowMapper<T> rm) throws SQLException;
	
	public <T> List<T> queryForListWithSql(String sql,Object[] datas,RowMapper<T> rm) throws SQLException;
	
	/**
	 * 查询多行结果集，封装成List
	 * @param sql
	 * @param datas
	 * @param argTypes
	 * @param rm
	 * @return  RowMapper is Null时返回list 封装对象为hashmap
	 *         <ul>
	 *         <li>key 表示列名</li>
	 *         <li>value 存储的是值</li>
	 *         </ul>
	 * @throws SQLException
	 */
	public <T> List<T> queryForListWithSql(String sql,Object[] datas,int[] argTypes,RowMapper<T> rm) throws SQLException;
	
	
	
	
	/**
	 * 单条更新或删除
	 * 
	 * @param sql update or delete 语句
	 *            
	 * @throws SQLException
	 * 
	 */

	public int updateWithSql(String sql) throws SQLException;
	

	public int updateWithSql(String sql, Object[] args) throws SQLException;
	
	/**
	 * 更新
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            更新参数 <b>Object[]</b>数组对象
	 * @param argTypes            
	 * @throws SQLException
	 */
	public int updateWithSql(String sql, Object[] args,int[] argTypes) throws SQLException;

	/**
	 * 批量更新或删除(小批量)
	 * 
	 * @param sqls  update or delete 语句数组
	 *           
	 * @throws SQLException
	 * 
	 */
	public void batchUpdate(String[] sqls) throws SQLException;

	/**
	 * 批量更新或删除(小批量)
	 * 
	 * @param list  update or delete 语句集合List类型对象
	 *           
	 * @throws SQLException
	 * 
	 */
	public void batchUpdate(List<String> list) throws SQLException;
	
	public int[] batchUpdateByParam(String sql, List<Object[]> list) throws SQLException;

	/**
	 * 带参数批量更新(中等)
	 * 
	 * @param sql
	 *            预编译sql 占位符 ？
	 * @param list
	 *            参数值集合
	 * @param sqlTypes
	 *            参数对应的java.sql.Types
	 * 
	 * @throws SQLException
	 * 
	 */
	public int[] batchUpdateByParam(String sql, List<Object[]> list, int[] sqlTypes)throws SQLException;

	/**
	 * 带参数批量更新(大批量)
	 * 
	 * @param sql
	 *            预编译sql 占位符 ？
	 * @param list
	 *            参数值集合
	 * @param sqlTypes
	 *            参数对应的java.sql.Types
	 * 
	 * @throws ApplicationException
	 */
	public void batchSqlUpdate(String sql, List<Object[]> list, int[] sqlTypes)throws SQLException;



	/**
	 * 运行sql语句 种类
	 * <ul>
	 * <li>insert</li>
	 * <li>delete</li>
	 * <li>update</li>
	 * <li>creat</li>
	 * <li>drop</li>
	 * </ul>
	 * @param sql
	 *            执行DDL语句
	 * @throws SQLException
	 * 
	 */
	public void execute(String sql) throws SQLException;
	
	public long queryForLong(String sql) throws SQLException;
	
	public int queryForInt(String sql) throws SQLException;
	
	
	
}
