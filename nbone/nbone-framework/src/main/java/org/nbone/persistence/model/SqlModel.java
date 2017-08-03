package org.nbone.persistence.model;


import java.util.Map;

import org.nbone.context.system.SystemContext;
import org.nbone.lang.BaseObject;
import org.nbone.mvc.domain.DomainQuery;
import org.nbone.persistence.JdbcConstants;
import org.nbone.persistence.JdbcOptions;
import org.nbone.persistence.exception.PersistenceBaseRuntimeException;
import org.nbone.persistence.mapper.TableMapper;
import org.nbone.persistence.support.PageSuport;
import org.nbone.util.lang.ToStringUtils;
import org.springframework.jdbc.core.RowMapper;

/**
 * 存储sql的Model
 * @author thinking
 * @since 2015-12-12
 *
 */
public class SqlModel<T> {
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
	private  TableMapper<?> tableMapper;
	
	/**
	 * group by/order by 子句
	 */
	private String  afterWhere;
	
	
	@SuppressWarnings("rawtypes")
	public static final  SqlModel EmptySqlModel = new SqlModel((String)null);
	
	
	public SqlModel(String sql) {
		this.sql = sql;
	}
	
	public SqlModel(String sql, T parameter) {
		this.sql = sql;
		this.parameter = parameter;
	}
	
	public SqlModel(String sql, T parameter,TableMapper<?> tableMapper) {
		this.sql = sql;
		this.parameter = parameter;
		this.tableMapper = tableMapper;
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
	
	public String getPlainSql(){
		//外层where 之后语句
		if(afterWhere == null || afterWhere.length() == 0) {
			//实体内where 之后语句
			if(parameter instanceof DomainQuery) {
				DomainQuery domainQuery = (DomainQuery) parameter;
				StringBuilder sBuilder = new StringBuilder(); 
				String groupBy = domainQuery.groupBy();
				if(groupBy!= null && groupBy.length() > 0) {
					sBuilder.append(" ").append(groupBy);
				}
				String having = domainQuery.having();
				if(having != null && having.length() > 0) {
					sBuilder.append(" ").append(having);
				}
				String orderBy = domainQuery.orderBy();
				if(orderBy != null && orderBy.length() > 0) {
					sBuilder.append(" ").append(orderBy);
				}
				afterWhere = sBuilder.toString();
			}else{
				
				return sql;
				
			}
		}
		return sql+ " " + afterWhere;
	}

	/**
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param afterWhere order by 子句
	 * @return
	 */
	public String getPageSql(int pageNum ,int pageSize){
		String pageSql  = "";
		String tempSql  = getPlainSql() ;
		
		if (JdbcConstants.MYSQL.equals(SystemContext.CURRENT_DB_TYPE)) {
			pageSql = PageSuport.toMysqlPage(tempSql, pageNum, pageSize);
			
		}else if (JdbcConstants.ORACLE.equals(SystemContext.CURRENT_DB_TYPE)) {
			pageSql = PageSuport.toOraclePage(tempSql, pageNum, pageSize);
		}
		print(pageSql);
		return pageSql;
	}
	
	
	public String getCountSql(){
		String countSql = PageSuport.getCountSqlString(sql);
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

	public TableMapper<?> getTableMapper() {
		return  tableMapper;
	}

	public void  setTableMapper(TableMapper<?> tableMapper) {
		this.tableMapper = tableMapper;
	}
	
	public String[] getPrimaryKeys() {
		return tableMapper.getPrimaryKeys();
	}
	
	public RowMapper<?> getRowMapper(){
		return tableMapper.getRowMapper();
	}
	
	public String getAfterWhere() {
		return afterWhere;
	}

	public void setAfterWhere(String afterWhere) {
		this.afterWhere = afterWhere;
	}

	private void print(){
		if(JdbcOptions.showSql){
			System.out.println(toString());
			System.out.println(getPrintSqlParameter());
		}
	}
	
	private void print(String printSql){
		if(JdbcOptions.showSql){
			System.out.println(printSql);
			System.out.println(getPrintSqlParameter());
		}
	}
	
	
	public StringBuilder getPrintSqlParameter(){
		
		StringBuilder print = new StringBuilder();
		if(parameter != null){
			if(parameter instanceof Map){
				print .append("Jdbc Parameter Map :").append(parameter) ;
			}else{
				print.append("Jdbc Parameter POJO : ").append(ToStringUtils.toStringMultiLine(parameter));
			}
		}else{
			if(parameterArray != null){
				print.append("Jdbc parameterArray : ").append(parameterArray);
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
