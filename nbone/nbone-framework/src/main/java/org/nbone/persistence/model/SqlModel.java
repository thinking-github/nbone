package org.nbone.persistence.model;


import org.apache.commons.lang3.builder.ToStringStyle;
import org.nbone.context.system.SystemContext;
import org.nbone.mvc.domain.DomainQuery;
import org.nbone.persistence.JdbcConstants;
import org.nbone.persistence.JdbcOptions;
import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.mapper.EntityMapper;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.support.PageSuport;
import org.nbone.util.lang.ToStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 存储sql的Model
 *
 * @author thinking
 * @since 2015-12-12
 */
public class SqlModel<T> {

	protected Logger logger = LoggerFactory.getLogger("SqlModel");
	/**
	 * sql语句
	 */
	private String sql ;
	/**
	 * sql占位符参数 ,可为空<br>
	 * 占位符是 name时, 参数可以是 Map/Javabean
	 */
	private T parameter;
	
	/**
	 * 占位符是  ? 时,参数时可以是 Object[]/List <br>
	 */
	private Object[] parameterArray;
	
	/**
	 *实体映射信息
	 */
	private EntityMapper<?> entityMapper;

	/**
	 * 结果集映射
	 */
	private RowMapper<?> rowMapper;

	/**
	 * 追加条件 或者 group by/order by 子句
	 */
	private String[]  afterWhere;
	
	
	@SuppressWarnings("rawtypes")
	public static final  SqlModel EmptySqlModel = new SqlModel((String)null);
	
	
	public SqlModel(String sql) {
		this.sql = sql;
	}
	
	public SqlModel(String sql, T parameter) {
		this.sql = sql;
		this.parameter = parameter;
	}

	public SqlModel(String sql, EntityMapper<?> entityMapper) {
		this.sql = sql;
		this.entityMapper = entityMapper;
	}

	public SqlModel(String sql, T parameter, EntityMapper<?> entityMapper) {
		this.sql = sql;
		this.parameter = parameter;
		this.entityMapper = entityMapper;
	}

	public SqlModel(String sql, Object[] parameterArray, EntityMapper<?> entityMapper) {
		this.sql = sql;
		this.parameterArray = parameterArray;
		this.entityMapper = entityMapper;
	}

	public SqlModel(String sql, T parameter, EntityMapper<?> entityMapper, SqlConfig sqlConfig) {
		this.sql = sql;
		this.parameter = parameter;
		this.entityMapper = entityMapper;
		this.afterWhere = sqlConfig.getAfterWhere();
	}
	
	
	public static  <T> void  checkSqlModel(SqlModel<T> sqlModel) {
		if(sqlModel == null || sqlModel == EmptySqlModel){
		}
	}
	

	public String getSql() {
		String sql = getPlainSql();
		print(sql);
		return sql;
	}

	/**
	 * 	if(afterWhere != null && afterWhere.length > 0) {
	 		afterWhereString = afterWhere[0];
	 	}
	 * @return
	 */
	private String getPlainSql(){
		String afterWhereString = null;
		//外层where 之后语句
		if(afterWhere == null || afterWhere.length == 0) {
			//实体内where 之后语句
			if(parameter instanceof DomainQuery) {
				DomainQuery domainQuery = (DomainQuery) parameter;
				StringBuilder sBuilder = new StringBuilder();

				String appendWhere = domainQuery.appendWhere();
				appendWhere(sBuilder,appendWhere);

				String groupBy = domainQuery.groupBy();
				String having = domainQuery.having();
				String orderBy = domainQuery.orderBy();
				appendSql(sBuilder,groupBy);
				appendSql(sBuilder,having);
				appendSql(sBuilder,orderBy);

				return  sql+ " " + sBuilder.toString();
			}
		} else {
			StringBuilder sBuilder = new StringBuilder();
			for (String append : afterWhere) {
				 appendWhere(sBuilder,append);
			}
			afterWhereString = sBuilder.toString();
		}
		if(afterWhereString != null){
			return sql+ " " + afterWhereString;
		}

		return sql;
	}

	private StringBuilder appendSql(StringBuilder sBuilder,String append){
		if(sBuilder == null){
			return null;
		}

		if(append != null && append.length() > 0) {
			sBuilder.append(" ").append(append);
		}

		return  sBuilder;
	}

	private StringBuilder appendWhere(StringBuilder stringBuilder,String appendWhere){
		if(appendWhere != null && appendWhere.length() > 0) {

		/*	String upper  = appendWhere.toUpperCase();
			if(!upper.startsWith("AND ") && !upper.startsWith(" AND ")){
				appendWhere = "and " +appendWhere;
			}*/

			if(sql.contains(" where ") || sql.contains(" WHERE ")){
				stringBuilder.append(" ").append(appendWhere);
			} else {
				if (StringUtils.startsWithIgnoreCase(appendWhere, "and ")
						|| StringUtils.startsWithIgnoreCase(appendWhere, " and ")) {
					stringBuilder.append(" where 1=1 ").append(appendWhere);
				} else {
					stringBuilder.append(" where ").append(appendWhere);
				}
			}
			return stringBuilder;
		}

		return  stringBuilder;
	}


	/**
	 * 
	 * @param pageNum
	 * @param pageSize
	 *  afterWhere order by 子句
	 * @return
	 */
	public String getPageSql(int pageNum ,int pageSize){
		String pageSql  = "";
		String plainSql  = getPlainSql();
		
		if (JdbcConstants.MYSQL.equals(SystemContext.CURRENT_DB_TYPE)) {
			pageSql = PageSuport.toMysqlPage(plainSql, pageNum, pageSize);
			
		}else if (JdbcConstants.ORACLE.equals(SystemContext.CURRENT_DB_TYPE)) {
			pageSql = PageSuport.toOraclePage(plainSql, pageNum, pageSize);
		}
		print(pageSql);
		return pageSql;
	}

	public String getOffsetPageSql(long offset ,int pageSize){
		String pageSql  = "";
		String plainSql  = getPlainSql();

		if (JdbcConstants.MYSQL.equals(SystemContext.CURRENT_DB_TYPE)) {
			pageSql = PageSuport.mysqlOffsetPage(plainSql, offset, pageSize);

		}
		print(pageSql);
		return pageSql;
	}
	
	
	public String getCountSql(){
		String tempSql  = getPlainSql();
		String countSql = PageSuport.getCountSqlString(tempSql);
		return countSql;
	}
	
	
	public void setSql(String sql) {
		this.sql = sql;
	}

	public T getParameter() {
		return parameter;
	}

	public void setParameter(T parameter) {
		this.parameter = parameter;
	}

	public Object[] getParameterArray() {
		return parameterArray;
	}

	public void setParameterArray(Object[] parameterArray) {
		this.parameterArray = parameterArray;
	}

	public EntityMapper<?> getEntityMapper() {
		return entityMapper;
	}

	public void setEntityMapper(EntityMapper<?> entityMapper) {
		this.entityMapper = entityMapper;
	}

	public FieldMapper getPrimaryKey() {
		return entityMapper.getPrimaryKeyFieldMapper();
	}
	public List<FieldMapper> getPrimaryKeys() {
		return entityMapper.getPrimaryKeyFields();
	}

	public RowMapper<?> getRowMapper() {
		if (rowMapper != null) {
			return rowMapper;
		}
		return entityMapper.getRowMapper();
	}
	public void setRowMapper(RowMapper<?> rowMapper) {
		this.rowMapper = rowMapper;
	}

	public String[] getAfterWhere() {
		return afterWhere;
	}

	public void setAfterWhere(String[] afterWhere) {
		this.afterWhere = afterWhere;
	}

	private void print(){
		if(JdbcOptions.showSql){
			//System.out.println(toString());
			//System.out.println(getPrintSqlParameter());

			logger.info(toString());
			logger.info(getPrintSqlParameter().toString());
		}
	}
	
	private void print(String printSql){
		if(JdbcOptions.showSql){
			//System.out.println(printSql);
			//System.out.println(getPrintSqlParameter());

			logger.info(printSql);
			logger.info(getPrintSqlParameter().toString());
		}
	}



	public StringBuilder getPrintSqlParameter(){
		
		StringBuilder print = new StringBuilder();
		if(parameter != null){
			if(parameter instanceof Map){
				print .append(">>>>>Jdbc Parameter Map :").append(parameter) ;
			}else{
				String pojoString = ToStringUtils.toString(parameter, ToStringStyle.MULTI_LINE_STYLE,true);
				print.append(">>>>>Jdbc Parameter POJO : ").append(pojoString);
			}
		}else{
			if(parameterArray != null){
				print.append(">>>>>Jdbc parameterArray : ").append(parameterArray);
			}
		}
		
		return print;
	}
	@Override
	public String toString() {
		StringBuilder sqlSb = new StringBuilder("Jdbc sql : "+ getPlainSql());
		
		return sqlSb.toString();
	}

}
