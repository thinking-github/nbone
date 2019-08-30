package org.nbone.persistence;

import java.util.HashMap;
import java.util.Map;

import org.nbone.persistence.criterion.QueryOperator;
/**
 * 
 * @author thinking
 * @version 1.0 
 */
public class SqlOperations {
	
	private Map<String, SqlOperation> sqlOperation = new HashMap<String, SqlOperation>();
	

	public SqlOperations() {
	}
	
	public SqlOperations(String fieldName, String operType) {
		this.addOperation(fieldName, operType);
	}

	/**
	 * 
	 * @param fieldName
	 * @param operType
	 * @return
	 */
	public SqlOperations addOperation(String fieldName, String operType) {
		SqlOperation sqlOperation = new SqlOperation(fieldName, operType);
		this.sqlOperation.put(fieldName, sqlOperation);
		return this;
	}
	
	public SqlOperations addOperationBetween(String fieldName, Object beginValue, Object endValue) {
		SqlOperation sqlOperation = new SqlOperation(fieldName, beginValue,endValue);
		this.sqlOperation.put(fieldName, sqlOperation);
		return this;
	}

	public SqlOperations addOperationBetween(String fieldName, Object[] values) {
		addOperationBetween(fieldName,values[0],values[1]);
		return this;
	}

	public SqlOperations addOperationIn(String fieldName, boolean isIn, Object values) {
		SqlOperation sqlPropertyDescriptor = new SqlOperation(fieldName);
		sqlPropertyDescriptor.setSpecialValue(values);
		if(isIn){
			sqlPropertyDescriptor.setOperType(QueryOperator.in);
		}else {
			sqlPropertyDescriptor.setOperType(QueryOperator.not_in);
		}
		this.sqlOperation.put(fieldName, sqlPropertyDescriptor);
		return this;
	}
	
	
	
	
	
	public SqlOperations addOperation(SqlOperation sqlPd) {
		if(sqlPd == null){
			return this;
		}
		String key = sqlPd.getFieldName();
		this.sqlOperation.put(key, sqlPd);
		return this;
	}

	
	public void addAll(SqlOperation[] sqlPds) {
		if(sqlPds == null){
			return;
		}
		for (int i = 0; i < sqlPds.length; i++) {
			addOperation(sqlPds[i]);
		}
	}
	
	
	public void addAll(Map<String, SqlOperation> sqlPds) {
		if(sqlPds == null){
			return;
		}
		this.sqlOperation.putAll(sqlPds);
	}
	public void addMapString(Map<String, String> sqlOperationMap) {
		if(sqlOperationMap == null){
			return;
		}
		for (Map.Entry<String, String> entry : sqlOperationMap.entrySet()) {
			addOperation(entry.getKey(),entry.getValue());
		}
	}
	public void remove(SqlOperation hqlPd) {
		if(hqlPd == null){
			return;
		}
		String key = hqlPd.getFieldName();
		this.sqlOperation.remove(key);
	}
	
	public Map<String, SqlOperation> getSqlOperationAsMap() {
		return sqlOperation;
	}
	
	public SqlOperation getSqlOperation(String fieldName) {
		if(sqlOperation.isEmpty()){
			return  null;
		}
		return sqlOperation.get(fieldName);
	}
	
	
	
	 
	 
	

}
