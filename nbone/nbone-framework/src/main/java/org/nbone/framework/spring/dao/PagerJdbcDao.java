package org.nbone.framework.spring.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.context.system.SystemContext;
import org.nbone.mx.datacontrols.datapage.PagerModel;
import org.nbone.persistence.JdbcConstants;
import org.nbone.persistence.support.PageSuport;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
/**
 * 
 * page Jdbc Dao 封装
 * @author thinking
 * @author xun 
 * @since 2014-08-04
 * @see org.nbone.framework.spring.dao.BaseJdbcDao
 *
 */
@Repository
@Primary
@Lazy
public class PagerJdbcDao implements IPagerJdbcDao{
   
	@Resource(name="baseJdbcDao")
	private BaseJdbcDao baseJdbcDao;
	
	
	public JdbcTemplate getJdbcTemplate(){
		//代理有时存在问题取不到值
		JdbcTemplate jdbcTemplate = baseJdbcDao.getJdbcTemplate();
		if(jdbcTemplate == null){
			jdbcTemplate = baseJdbcDao.getSuperJdbcTemplate();
		}
		
		return jdbcTemplate;
	}
	
	private Log logger = LogFactory.getLog(getClass());
	
	
	public PagerModel<Map<String,Object>> findByPage(String sql) throws SQLException {
		return findByPage(sql, null);
	}
	
	public PagerModel<Map<String,Object>> findByPage(String sql, Object[] datas) throws SQLException {
		return findByPage(sql, datas, SystemContext.getOffset(), SystemContext.getPageSize());
	}
	
	public PagerModel<Map<String,Object>> findByPage(String sql, Object[] datas,int[] types) throws SQLException {
		return findByPage(sql, datas,types,SystemContext.getOffset(), SystemContext.getPageSize());
	}
	
	
	
	public PagerModel<Map<String,Object>> findByPage(String sql,int offset,int pageSize) throws SQLException {
		return findByPage(sql, null, offset, pageSize);
	}

	public PagerModel<Map<String,Object>> findByPage(String sql, Object[] datas, int offset,int pageSize) throws SQLException {
		return findByPage(sql, datas, null, null, offset, pageSize);
	}
   
	public PagerModel<Map<String,Object>> findByPage(String sql, Object[] datas, int[] types,int offset, int pageSize) throws SQLException {
		return findByPage(sql, datas, types, null, offset, pageSize);
	}
   
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> PagerModel<T> findByPage(String sql, Object[] datas, int[] argTypes,RowMapper<T> rm, int offset, int pageSize) throws SQLException {
		PagerModel<T> pm = new PagerModel<T>();
		JdbcTemplate dao = this.getJdbcTemplate();
		String countSql = PageSuport.getCountSqlString(sql);
		Long total = 0L;
		total = (datas != null) ? ((argTypes != null) ? dao.queryForObject(countSql, datas, argTypes,Long.class) 
				: dao.queryForObject(countSql, datas,Long.class)) 
				: dao.queryForObject(countSql,Long.class);
		// if (total != null && total > 0) {
		// this.getJdbcTemplate().setMaxRows(total);
		// }
		pm.setTotal(total);
		List list_obj;
		String pageSql  = "";

		if (JdbcConstants.MYSQL.equals(SystemContext.CURRENT_DB_TYPE)) {
			pageSql = PageSuport.toMysqlPage(sql, offset, pageSize);
			
		}else  if (JdbcConstants.ORACLE.equals(SystemContext.CURRENT_DB_TYPE)) {
			
			pageSql = PageSuport.toOraclePage(sql, offset, pageSize);
		}
      
		if (datas != null) {
			list_obj = (argTypes != null) ? ((rm != null) ? dao.query(pageSql, datas, argTypes, rm)
					: dao.queryForList(pageSql, datas, argTypes))
					: ((rm != null) ? dao.query(pageSql, datas, rm)
					: dao.queryForList(pageSql, datas));
		} else {
			list_obj = (rm != null) ? dao.query(pageSql, rm) : dao.queryForList(pageSql);
		}
		pm.setPageNow(offset);
		pm.setPageSize(pageSize);
		pm.setRows(list_obj);
		if(logger.isDebugEnabled()){
			logger.debug("findByPage Sql: "+pageSql);
		}
		
		return pm;
	}
	
	


}
