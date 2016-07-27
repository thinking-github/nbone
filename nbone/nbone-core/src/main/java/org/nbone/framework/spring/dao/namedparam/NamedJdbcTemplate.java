package org.nbone.framework.spring.dao.namedparam;

import java.util.List;

import javax.sql.DataSource;

import org.nbone.context.system.SystemContext;
import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.JdbcConstants;
import org.nbone.persistence.SqlBuilder;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.model.SqlModel;
import org.nbone.persistence.support.PageSuport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
/**
 * 
 * @author thinking
 * @version 1.0 
 * @since spring 2.0
 */
public class NamedJdbcTemplate  extends NamedParameterJdbcTemplate{

	private  JdbcOperations jdbcTemplate;
	private SqlBuilder sqlBuilder;
	
	public NamedJdbcTemplate(DataSource dataSource,SqlBuilder sqlBuilder) {
		super(dataSource);
		this.jdbcTemplate = this.getJdbcOperations();
		this.sqlBuilder = sqlBuilder;
		
		if(sqlBuilder == null){
			this.sqlBuilder = new BaseSqlBuilder(JdbcFrameWork.SPRING_JDBC) {
				
			}; 
		}
	}

	public NamedJdbcTemplate(JdbcOperations classicJdbcTemplate,SqlBuilder sqlBuilder) {
		super(classicJdbcTemplate);
		this.jdbcTemplate = this.getJdbcOperations();
		this.sqlBuilder = sqlBuilder;
		
		if(sqlBuilder == null){
			this.sqlBuilder = new BaseSqlBuilder(JdbcFrameWork.SPRING_JDBC) {
				
			}; 
		}
		
	}
	
	
	/**
	 * 单表数据分页
	 * @param object
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Page<T> findByPage(Object object,int pageNum ,int pageSize){
		SqlModel<Object> sqlModel = sqlBuilder.buildSimpleSelectSql(object);
		if(SqlModel.checkSqlModel(sqlModel)){
			
			String sql = sqlModel.getSql();
			String countSql = PageSuport.getCountSqlString(sql);
			String pageSql  = "";
			
			if (JdbcConstants.MYSQL.equals(SystemContext.CURRENT_DB_TYPE)) {
				pageSql = PageSuport.toMysqlPage(sql, pageNum, pageSize);
				
			}else if (JdbcConstants.ORACLE.equals(SystemContext.CURRENT_DB_TYPE)) {
				pageSql = PageSuport.toOraclePage(sql, pageNum, pageSize);
			}
			
			RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
			SqlParameterSource paramSource = new BeanPropertySqlParameterSource(object);
			
			List<T> rows = jdbcTemplate.query(getPreparedStatementCreator(pageSql,paramSource), rowMapper);
			
			long  count  =  this.queryForLong(countSql, paramSource);
			
			Page<T> page  = new PageImpl<T>(rows, null,count);
			
			return page;
		}
		return new PageImpl<T>(null);
	}
	
	
	
	
	

}
