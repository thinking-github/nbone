package org.nbone.persistence.model;

import java.util.Map;

import org.nbone.lang.BaseObject;
import org.nbone.persistence.JdbcParameter;
import org.nbone.persistence.exception.PersistenceBaseRuntimeException;
import org.nbone.persistence.mapper.TableMapper;
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
		if(JdbcParameter.showSql){
			System.out.println(toString());
			System.out.println(getPrintSqlParameter());
		}
		return sql;
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
		StringBuilder sqlSb = new StringBuilder("Jdbc sql : "+sql);
		
		return sqlSb.toString();
	}

}
