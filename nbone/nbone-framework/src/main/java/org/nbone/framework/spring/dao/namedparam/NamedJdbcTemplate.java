package org.nbone.framework.spring.dao.namedparam;

import java.util.List;

import javax.sql.DataSource;

import org.nbone.framework.spring.data.domain.PageImpl;
import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.SqlBuilder;
import org.nbone.persistence.annotation.FieldLevel;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.exception.BuilderSQLException;
import org.nbone.persistence.model.SqlModel;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
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
	public <T> Page<T> getForPage(Object object,int pageNum ,int pageSize,String... afterWhere){
		String afterWhereString = null;
		if(afterWhere != null && afterWhere.length > 0) {
			afterWhereString = afterWhere[0];
		}
		SqlModel<Object> sqlModel = sqlBuilder.selectSql(object,(FieldLevel)null,afterWhereString);
		return processPage(sqlModel, object, pageNum, pageSize);
		
	}
	/**
	 * 单表数据分页
	 * @param object
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public <T> Page<T> queryForPage(Object object,int pageNum ,int pageSize,String... afterWhere){
		String afterWhereString = null;
		if(afterWhere != null && afterWhere.length > 0) {
			afterWhereString = afterWhere[0];
		}
		SqlModel<Object> sqlModel = sqlBuilder.simpleSelectSql(object,null,afterWhereString);
		return processPage(sqlModel, object, pageNum, pageSize);
	}
	/**
	 * 单表数据分页
	 * @param object
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	
	public <T> Page<T> findForPage(Object object,int pageNum ,int pageSize,String... afterWhere){
		String afterWhereString = null;
		if(afterWhere != null && afterWhere.length > 0) {
			afterWhereString = afterWhere[0];
		}
		SqlModel<Object> sqlModel = sqlBuilder.simpleSelectSql(object,null,afterWhereString);
		
		return processPage(sqlModel, object, pageNum, pageSize);
	}
	
	public long queryForLong(String sql, SqlParameterSource paramSource) throws DataAccessException {
		Number number = queryForObject(sql, paramSource, Long.class);
		return (number != null ? number.longValue() : 0);
	}
	
	@SuppressWarnings("unchecked")
	private <T> Page<T>  processPage(SqlModel<Object> sqlModel,Object object,int pageNum ,int pageSize,String... afterWhere){
		if(sqlModel == null){
			throw new BuilderSQLException("sqlModel is null.");
		}
		if(pageNum <= 0 ){
			pageNum = 1;
		}
		if(pageSize <= 0 ){
			pageSize = 10;
		}
		
		String countSql = sqlModel.getCountSql();
		String pageSql  = sqlModel.getPageSql(pageNum, pageSize);
		
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(object);
		
		List<T> rows = jdbcTemplate.query(getPreparedStatementCreator(pageSql,paramSource), rowMapper);
		
		long  count  =  this.queryForLong(countSql, paramSource);
		
		Page<T> page  = new PageImpl<T>(rows, null,count);
		
		return page;
		
	}

}
