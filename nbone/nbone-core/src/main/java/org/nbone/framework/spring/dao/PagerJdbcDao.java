package org.nbone.framework.spring.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.context.system.SystemContext;
import org.nbone.mx.datacontrols.datapage.PagerModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
/**
 * 
 * page Jdbc Dao 封装
 * @author Thinking 2014-08-04
 *
 */
@Repository
public class PagerJdbcDao extends JdbcDaoSupportX implements IPagerJdbcDao{
   
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
   
	public <T> PagerModel<T> findByPage(String sql, Object[] datas, int[] types,RowMapper<T> rm, int offset, int pageSize) throws SQLException {
		PagerModel<T> pm = new PagerModel<T>();
		JdbcTemplate dao = this.getJdbcTemplate();
		String countSql = getCountSqlString(sql);
		Integer total = 0;
		total = (datas != null) ? ((types != null) ? dao.queryForInt(countSql, datas, types) 
				: dao.queryForInt(countSql, datas)) 
				: dao.queryForInt(countSql);
		// if (total != null && total > 0) {
		// this.getJdbcTemplate().setMaxRows(total);
		// }
		pm.setTotal(total);
		List list_obj = new ArrayList();
		StringBuilder sbSql = new StringBuilder();

		if (SystemContext.DB_TYPE_MYSQL.equals(SystemContext.currentUse_DB_TYPE)) {
			sbSql.append("select sys_tempp.* from ( " + sql+ " )  sys_tempp LIMIT " + pageSize * (offset-1) + ","+ pageSize);
		}

		if (SystemContext.DB_TYPE_ORACLE.equals(SystemContext.currentUse_DB_TYPE)) {

			sbSql.append("select system_temp.*  from (select  temp.*  ,rownum as rownums   from (");
			sbSql.append(sql).append(" ) temp ) system_temp  where  system_temp.rownums >");
			sbSql.append(pageSize * (offset - 1));  
			// 加了个等号,不然会少查一条记录
			sbSql.append(" and system_temp.rownums <= ");
			sbSql.append(pageSize * (offset));
		}
      
		if (datas != null) {
			list_obj = (types != null) ? ((rm != null) ? dao.query(sbSql.toString(), datas, types, rm)
					: dao.queryForList(sbSql.toString(), datas, types))
					: ((rm != null) ? dao.query(sbSql.toString(), datas, rm)
					: dao.queryForList(sbSql.toString(), datas));
		} else {
			list_obj = (rm != null) ? dao.query(sbSql.toString(), rm)
					: dao.queryForList(sbSql.toString());
		}
		pm.setPageNow(offset);
		pm.setPageSize(pageSize);
		pm.setRows(list_obj);
		if(logger.isDebugEnabled()){
			logger.debug("findByPage Sql: "+sbSql);
		}
		
		return pm;
	}
	
	
	public static String getCountSqlString(String sql) {
		return "select count(*) as total  from ( " + sql + " ) sysT";
	}


}
