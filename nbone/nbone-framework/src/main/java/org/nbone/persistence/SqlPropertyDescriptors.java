package org.nbone.persistence;

import java.util.HashMap;
import java.util.Map;

import org.nbone.persistence.criterion.QueryOperator;
/**
 * 
 * @author thinking
 * @version 1.0 
 */
public class SqlPropertyDescriptors {
	
	private Map<String,SqlPropertyDescriptor> sqlPds = new HashMap<String, SqlPropertyDescriptor>();
	
	
	
	
	public SqlPropertyDescriptors() {
	}
	
	public SqlPropertyDescriptors(String fieldName,String operType) {
		this.addSqlPd(fieldName, operType);
	}

	/**
	 * 
	 * @param fieldName
	 * @param operType
	 * @return
	 */
	public SqlPropertyDescriptors addSqlPd(String fieldName,String operType) {
		SqlPropertyDescriptor sqlPropertyDescriptor = new SqlPropertyDescriptor(fieldName, operType);
		this.sqlPds.put(fieldName, sqlPropertyDescriptor);
		return this;
	}
	
	public SqlPropertyDescriptors addSqlPdBetween(String fieldName,Object beginValue,Object endValue) {
		SqlPropertyDescriptor sqlPropertyDescriptor = new SqlPropertyDescriptor(fieldName, beginValue,endValue);
		this.sqlPds.put(fieldName, sqlPropertyDescriptor);
		return this;
	}
	public SqlPropertyDescriptors addSqlPdIn(String fieldName,Object[] values) {
		SqlPropertyDescriptor sqlPropertyDescriptor = new SqlPropertyDescriptor(fieldName,values);
		this.sqlPds.put(fieldName, sqlPropertyDescriptor);
		return this;
	}
	
	public SqlPropertyDescriptors addSqlPdNotIn(String fieldName,Object[] values) {
		SqlPropertyDescriptor sqlPropertyDescriptor = new SqlPropertyDescriptor(fieldName);
		sqlPropertyDescriptor.setOperType(QueryOperator.not_in);
		this.sqlPds.put(fieldName, sqlPropertyDescriptor);
		return this;
	}
	
	
	
	
	
	public SqlPropertyDescriptors addSqlPd(SqlPropertyDescriptor sqlPd) {
		if(sqlPd == null){
			return this;
		}
		String key = sqlPd.getFieldName();
		this.sqlPds.put(key, sqlPd);
		return this;
	}

	
	public void addSqlPds(SqlPropertyDescriptor[] sqlPds) {
		if(sqlPds == null){
			return;
		}
		for (int i = 0; i < sqlPds.length; i++) {
			addSqlPd(sqlPds[i]);
		}
	}
	
	
	public void addSqlPds(Map<String,SqlPropertyDescriptor> sqlPds) {
		if(sqlPds == null){
			return;
		}
		this.sqlPds.putAll(sqlPds);
	}
	public void removeHqlPd(SqlPropertyDescriptor hqlPd) {
		if(hqlPd == null){
			return;
		}
		String key = hqlPd.getFieldName();
		this.sqlPds.remove(key);
	}
	
	public Map<String,SqlPropertyDescriptor> getSqlPds() {
		return sqlPds;
	}
	
	public SqlPropertyDescriptor getSqlPd(String fieldName) {
		return sqlPds.get(fieldName);
	}
	
	
	
	 
	 
	

}
