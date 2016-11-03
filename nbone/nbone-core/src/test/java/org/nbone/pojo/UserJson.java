package org.nbone.pojo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.nbone.test.domain.User;

public class UserJson extends User { 
     
	 private long longValue;
	 private double doubleValue;
	 private float floatValue;
	 private java.sql.Date sqlDate;
	 private Timestamp createTimestamp;
	 
	 
	 private Map extMap;
	 
	 private List extList;
	
	 
	public long getLongValue() {
		return longValue;
	}
	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}
	public double getDoubleValue() {
		return doubleValue;
	}
	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}
	public float getFloatValue() {
		return floatValue;
	}
	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
	}
	public java.sql.Date getSqlDate() {
		return sqlDate;
	}
	public void setSqlDate(java.sql.Date sqlDate) {
		this.sqlDate = sqlDate;
	}
	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}
	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	
	
	public Map getExtMap() {
		return extMap;
	}
	public void setExtMap(Map extMap) {
		this.extMap = extMap;
	}
	
	
	public List getExtList() {
		return extList;
	}
	public void setExtList(List extList) {
		this.extList = extList;
	}
	public static UserJson getUserJson(String id){
		
		UserJson user = new UserJson();
		user.setId(id);
		user.setName("thinking");
		
		return user;
		
	}
	 
	 
}