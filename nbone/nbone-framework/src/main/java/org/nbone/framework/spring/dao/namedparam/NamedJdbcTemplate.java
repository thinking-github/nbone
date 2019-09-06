package org.nbone.framework.spring.dao.namedparam;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.nbone.framework.spring.data.domain.PageImpl;
import org.nbone.mvc.domain.GroupQuery;
import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.SqlBuilder;
import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.exception.BuilderSQLException;
import org.nbone.persistence.model.SqlModel;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@SuppressWarnings("unused")
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
	 * @param sqlConfig java字段名称 可为空,为空返回全部字段
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public <T> Page<T> getForPage(Object object,SqlConfig sqlConfig,int pageNum ,int pageSize){
		if(sqlConfig == null){
			sqlConfig =  SqlConfig.EMPTY;
		}else {
			sqlConfig.setSqlMode(-1);
		}

		SqlModel<T> sqlModel = buildSqlModel(object,sqlConfig);
		return processPage(sqlModel, object, pageNum, pageSize);
	}
	/**
	 * 单表数据分页
	 * @param columnMap
	 * @param sqlConfig java字段名称 可为空,为空返回全部字段
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public <T> Page<T> getForPage(Map<String,?> columnMap,SqlConfig sqlConfig,int pageNum ,int pageSize){
		if(sqlConfig == null){
			sqlConfig =  SqlConfig.EMPTY;
		}

		SqlModel<Map<String, ?>> sqlModel = (SqlModel<Map<String,?>>) sqlBuilder.selectSql(columnMap,sqlConfig);
		return processPage(sqlModel, columnMap, pageNum, pageSize,sqlConfig);
	}
	/**
	 * 单表数据分页
	 * @param object
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public <T> Page<T> queryForPage(Object object,SqlConfig sqlConfig,int pageNum ,int pageSize){
		if(sqlConfig == null){
			sqlConfig = SqlConfig.EMPTY;
		}else {
			sqlConfig.setSqlMode(SqlConfig.PrimaryMode);
		}
		SqlModel<T> sqlModel = buildSqlModel(object,sqlConfig);
		return processPage(sqlModel, object, pageNum, pageSize);
	}

	/**
	 * 单表数据分页 （支持多种组合查询）
	 * @param object
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public <T> Page<T> findForPage(Object object,int pageNum ,int pageSize,SqlConfig sqlConfig){
		SqlModel<Map<String,?>> sqlModel  = sqlBuilder.objectModeSelectSql(object,sqlConfig);
		
		return processPage(sqlModel, object, pageNum, pageSize,sqlConfig);
	}
	
	public long queryForLong(String sql, SqlParameterSource paramSource) throws DataAccessException {
		Number number = queryForObject(sql, paramSource, Long.class);
		return (number != null ? number.longValue() : 0);
	}

	public long queryForLong(String sql, Map<String,?> paramMap) throws DataAccessException {
		Number number = queryForObject(sql, paramMap, Long.class);
		return (number != null ? number.longValue() : 0);
	}


	/**
	 *
	 * @param object 参数实体对象，不为空的作为参数查询
	 * @param sqlConfig  sqlConfig对象， 可为空
	 * @param limit  限制返回数量
	 * @param <T>
	 * @return
	 */
	public <T> List<T> getForLimit(Object object,SqlConfig sqlConfig,int limit){
		if(sqlConfig == null){
			sqlConfig = SqlConfig.EMPTY;
		}else {
			sqlConfig.setSqlMode(-1);
		}
		SqlModel<Object> sqlModel = buildSqlModel(object,sqlConfig);
		return processLimit(sqlModel, object, sqlConfig.getGroupQuery(),limit);
	}
	public <T> List<T> queryForLimit(Object object,SqlConfig sqlConfig,int limit){
		if(sqlConfig == null){
			sqlConfig = SqlConfig.EMPTY;
		}
		SqlModel<Object> sqlModel = buildSqlModel(object,sqlConfig);
		return processLimit(sqlModel, object,sqlConfig.getGroupQuery(), limit);
	}

	private <T> SqlModel<T> buildSqlModel(Object object,SqlConfig sqlConfig){
		SqlModel<T> sqlModel = (SqlModel<T>) sqlBuilder.selectSql(object,sqlConfig);
		return sqlModel;
	}


	@SuppressWarnings("unchecked")
	private <T> Page<T>  processPage(SqlModel<?> sqlModel,Object object,int pageNum ,int pageSize){
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
		//zero-based page index.
		PageRequest pageRequest = new PageRequest(pageNum-1,pageSize);
		Page<T> page  = new PageImpl<T>(rows, pageRequest,count);

		return page;

	}

	@SuppressWarnings("unchecked")
	private <T> Page<T>  processPage(SqlModel<?> sqlModel,Object object,int pageNum ,int pageSize,SqlConfig sqlConfig){
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
		Map<String,?> paramMap = (Map<String, ?>) sqlModel.getParameter();

		List<T> rows =  super.query(pageSql, paramMap,rowMapper);
		long  count  =  this.queryForLong(countSql, paramMap);
		//zero-based page index.
		PageRequest pageRequest = new PageRequest(pageNum-1,pageSize);
		Page<T> page  = new PageImpl<T>(rows, pageRequest,count);

		return page;

	}

	@SuppressWarnings("unchecked")
	private <T> List<T>  processLimit(SqlModel<Object> sqlModel,Object object,GroupQuery group, int pageSize){
		if(sqlModel == null){
			throw new BuilderSQLException("sqlModel is null.");
		}

		if(pageSize <= 0 ){
			pageSize = 10;
		}

		String pageSql  = sqlModel.getPageSql(1, pageSize);

		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();

		RowMapper<T> rowMapperGroup =  sqlBuilder.getRowMapper(group);
		if(rowMapperGroup != null){
			rowMapper = rowMapperGroup;
		}
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(object);

		List<T> rows = jdbcTemplate.query(getPreparedStatementCreator(pageSql,paramSource), rowMapper);
		return rows;

	}

}
