package org.nbone.persistence.model;

import org.nbone.persistence.mapper.TableMapper;
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
	 * 占位符是  ? 时,参数时可以是 Object[]/List <br>
	 * 占位符是 name时, 参数可以是 Map/Javabean
	 */
	private T parameter;
	
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
	
	
	
	public static  <T> boolean  checkSqlModel(SqlModel<T> sqlModel) {
		if(sqlModel == null || sqlModel == EmptySqlModel){
			return false;
		}
		return true;
	}
	

	public String getSql() {
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
	

}
