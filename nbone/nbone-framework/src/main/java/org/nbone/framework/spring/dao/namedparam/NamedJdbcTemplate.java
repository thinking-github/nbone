package org.nbone.framework.spring.dao.namedparam;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.sun.javafx.collections.MappingChange;
import org.nbone.framework.spring.data.domain.PageImpl;
import org.nbone.mvc.domain.GroupQuery;
import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.SqlBuilder;
import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.annotation.FieldLevel;
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
	 * @param fieldNames java字段名称 可为空,为空返回全部字段
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public <T> Page<T> getForPage(Object object,String[] fieldNames,int pageNum ,int pageSize,String... afterWhere){
		SqlModel<T> sqlModel = buildSqlModel(object,fieldNames,null,-1,false,afterWhere);
		return processPage(sqlModel, object, pageNum, pageSize);
		
	}
	/**
	 * 单表数据分页
	 * @param object
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public <T> Page<T> queryForPage(Object object,String[] fieldNames,int pageNum ,int pageSize,String... afterWhere){
		SqlModel<T> sqlModel = buildSqlModel(object,fieldNames,null,SqlConfig.PrimaryMode,false,afterWhere);
		return processPage(sqlModel, object, pageNum, pageSize);
	}

	/**
	 * 单表数据分页 （支持多种组合查询）
	 * @param object
	 * @param pageNum
	 * @param pageSize
	 * @param sqlConfig
	 * @param <T>
	 * @return
	 */
	public <T> Page<T> queryForPage(Object object,int pageNum ,int pageSize,SqlConfig sqlConfig){
		SqlModel<Map<String,?>> sqlModel  = sqlBuilder.objectModeSelectSql(object,sqlConfig);
		return processPage(sqlModel, object, pageNum, pageSize,sqlConfig);
	}
	/**
	 * 单表数据分页
	 * @param object
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public <T> Page<T> findForPage(Object object,int pageNum ,int pageSize,String... afterWhere){
		SqlModel<Object> sqlModel = buildSqlModel(object,null,null,SqlConfig.PrimaryMode,false,afterWhere);
		
		return processPage(sqlModel, object, pageNum, pageSize);
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
	 * @param group  分组对象， 可为空
	 * @param limit  限制返回数量
	 * @param afterWhere where 之后的语句 增加查询条件 /order by
	 * @param <T>
	 * @return
	 */
	public <T> List<T> getForLimit(Object object,GroupQuery group,int limit, String... afterWhere){
		SqlModel<Object> sqlModel = buildSqlModel(object,null,group,-1,false,afterWhere);
		return processLimit(sqlModel, object, group,limit);

	}
	public <T> List<T> queryForLimit(Object object,GroupQuery group,int limit,String... afterWhere){
		SqlModel<Object> sqlModel = buildSqlModel(object,null,group,SqlConfig.PrimaryMode,false,afterWhere);
		return processLimit(sqlModel, object,group, limit);
	}

	private SqlConfig buildSqlConfig(String[] fieldNames, GroupQuery group,int model,boolean dbFieldMode,String... afterWhere){
		SqlConfig sqlConfig = new SqlConfig(model);
		sqlConfig.setFieldNames(fieldNames);
		sqlConfig.setGroupQuery(group);
		sqlConfig.setDbFieldMode(dbFieldMode);
		sqlConfig.setAfterWhere(afterWhere);
		return sqlConfig;
	}
	private <T> SqlModel<T> buildSqlModel(Object object,String[] fieldNames, GroupQuery group,int model,boolean dbFieldMode,String... afterWhere){
		SqlConfig sqlConfig = buildSqlConfig(fieldNames,group,model,dbFieldMode,afterWhere);
		SqlModel<T> sqlModel = (SqlModel<T>) sqlBuilder.selectSql(object,sqlConfig);
		return sqlModel;
	}


	@SuppressWarnings("unchecked")
	private <T> Page<T>  processPage(SqlModel<?> sqlModel,Object object,int pageNum ,int pageSize,String... afterWhere){
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
	private <T> List<T>  processLimit(SqlModel<Object> sqlModel,Object object,GroupQuery group, int pageSize,String... afterWhere){
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
