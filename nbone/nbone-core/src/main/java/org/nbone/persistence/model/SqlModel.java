package org.nbone.persistence.model;
/**
 * 
 * @author thinking
 * @since 2015-12-12
 *
 */
public class SqlModel {
	/**
	 * sql语句
	 */
	private String sql ;
	/**
	 * sql占位符参数 ,可为空<br>
	 * 占位符是  ? 时,参数时可以是 Object[]/List <br>
	 * 占位符是 name时, 参数可以是 Map/javabean
	 */
	private Object parameter;
	

	public SqlModel(String sql, Object parameter) {
		this.sql = sql;
		this.parameter = parameter;
	}
	
	public SqlModel(String sql) {
		this.sql = sql;
	}


	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object getParameter() {
		return parameter;
	}

	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

}
