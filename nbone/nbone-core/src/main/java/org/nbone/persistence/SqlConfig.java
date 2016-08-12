package org.nbone.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author thinking
 * @version 1.0 
 */
public class SqlConfig {
	
    public static final int PrimaryMode = 0 ;
    public static final int MiddleMode = 1 ;
    public static final int HighMode = 2 ;
    
    public static final int ObjectMode = 3 ;
    
    private String aliasName = " tempA";  
	
	/**
	 * 高级时使用
	 */
    
    private SqlPropertyDescriptors  sqlPds = new SqlPropertyDescriptors();
    
    private List<SqlPropertyRange> sqlPropertyRanges = new ArrayList<SqlPropertyRange>(1);
    
    /**
     * 字段名称数组,用于按需返回字段
     */
    private String[] fieldNames;
    /**
     * 默认采用Java字段模式
     */
    private boolean dbFieldMode = false;
    
    
    /**
     * 此实体Bean 引用其他的实体Bean列表
     */
    private List<Class<?>> pojoRefs;
    /**
     * 默认为初级
     */
    private int sqlMode = PrimaryMode;
   
    /**
     * 中级时使用
     */
    //number String key/value
    private Map<String,String> inNumStrMap;
    private Map<String,String> notinNumStrMap;
    //String key
    private String[] inStringFields;
    private String[] notinStringFields;
    private Map<String,String> inStringFieldsMap;
    private Map<String,String> notinStringFieldsMap;
   
	private List dtField;
    /**
     * 初中高级通用
     */
    private String[] orderFieldASC;
    private String[] orderFieldDESC;

    public SqlConfig(){
    	this.sqlMode = PrimaryMode;
    }
    
    public SqlConfig(String aliasName) {
		this.aliasName = aliasName;
		this.sqlMode = PrimaryMode;
	}
    
    public SqlConfig(int hqlMode) {
		this.sqlMode = hqlMode;
	}

	/**
     *  MiddleMode Constructor
     * @param inStringFields
     * @param notinStringFields
     */
    public SqlConfig(String[] inStringFields, String[] notinStringFields) {
		this.inStringFields = inStringFields;
		this.notinStringFields = notinStringFields;
		
		this.inStringFieldsMap = stringArray2Map(inStringFields);
		this.notinStringFieldsMap = stringArray2Map(notinStringFields);
		
		this.sqlMode = MiddleMode;
	}

	/**
     * HighMode Constructor
     * @param hqlPds
     */
	public SqlConfig(Map<String,SqlPropertyDescriptor> hqlPds){
    	this.addSqlPds(hqlPds);
    	this.sqlMode = HighMode;
    }
	
	public SqlConfig(SqlPropertyDescriptors hqlPds){
    	this.sqlPds = hqlPds;
    	this.sqlMode = HighMode;
    }
	//-------------------------------------------------------------------------
	//factory method 
	//-------------------------------------------------------------------------
	public static SqlConfig getSqlConfig(int mode){
		return new SqlConfig(mode);
	}
	
	public static SqlConfig getHighMode(){
		return new SqlConfig((Map<String,SqlPropertyDescriptor>)null);
	}
	
	public static SqlConfig getMiddleMode(String[] inStringFields, String[] notinStringFields){
		return new SqlConfig(inStringFields, notinStringFields);
	}
	

	//-------------------------------------------------------------------------
	//特殊方法
	//-------------------------------------------------------------------------
	public Map<String,String> stringArray2Map(String[] strArray){
		Map<String,String>   resultMap = new HashMap<String, String>();
		if(strArray != null){
			for (int i = 0; i < strArray.length; i++) {
				String did = strArray[i];
				resultMap.put(did, did);
			}
		}
		//XXX to MiddleMode
		this.sqlMode = MiddleMode;
		return resultMap;
	}

	
	
	public SqlConfig addSqlPd(String fieldName,String operType) {
		this.sqlPds.addSqlPd(fieldName, operType);
		return this;
	}
	
	public SqlConfig addSqlPdBetween(String fieldName,Object beginValue,Object endValue) {
		this.sqlPds.addSqlPdBetween(fieldName, beginValue, endValue);
		return this;
	}
	public SqlConfig addSqlPdIn(String fieldName,Object[] values) {
		this.sqlPds.addSqlPdIn(fieldName, values);
		return this;
	}
	
	public SqlConfig addSqlPdNotIn(String fieldName,Object[] values) {
		this.sqlPds.addSqlPdNotIn(fieldName, values);
		return this;
	}
	
	
	
	public SqlConfig addSqlPd(SqlPropertyDescriptor hqlPd) {
		this.sqlPds.addSqlPd(hqlPd);
		return this;
	}
	
	public void addSqlPds(SqlPropertyDescriptor[] sqlPds) {
		this.sqlPds.addSqlPds(sqlPds);
	}
	
	public void addSqlPds(Map<String,SqlPropertyDescriptor> sqlPds) {
		this.sqlPds.addSqlPds(sqlPds);
	}
	
	public void setSqlPds(SqlPropertyDescriptors sqlPds) {
		this.sqlPds = sqlPds;
	}
	
	public void removeHqlPd(SqlPropertyDescriptor hqlPd) {
		this.sqlPds.removeHqlPd(hqlPd);
	}
	
	//-----------------------------Range propertity------------------------------------

	public SqlConfig addSqlPropertyRange (String leftField,String rightField,Object value){
		SqlPropertyRange sqlPropertyRange = new SqlPropertyRange(leftField, rightField, value);
		sqlPropertyRanges.add(sqlPropertyRange);
		return this;
	}

	//-------------------------------------------------------------------------
	//seter / geter
	//-------------------------------------------------------------------------
	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}


	public Map<String,SqlPropertyDescriptor> getSqlPds() {
		return this.sqlPds.getSqlPds();
	}
	
	public SqlPropertyDescriptor getSqlPd(String fieldName) {
		return this.sqlPds.getSqlPd(fieldName);
	}
	

	public List<SqlPropertyRange> getSqlPropertyRanges() {
		return sqlPropertyRanges;
	}

	public void setSqlPropertyRanges(List<SqlPropertyRange> sqlPropertyRanges) {
		this.sqlPropertyRanges = sqlPropertyRanges;
	}
	

	public String[] getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	public boolean isDbFieldMode() {
		return dbFieldMode;
	}

	public void setDbFieldMode(boolean dbFieldMode) {
		this.dbFieldMode = dbFieldMode;
	}

	public List<Class<?>> getPojoRefs() {
		return pojoRefs;
	}

	public void setPojoRefs(List<Class<?>> pojoRefs) {
		this.pojoRefs = pojoRefs;
	}


	public int getSqlMode() {
		return sqlMode;
	}

	public void setSqlMode(int hqlMode) {
		this.sqlMode = hqlMode;
	}


	public Map<String, String> getInNumStrMap() {
		return inNumStrMap;
	}

	public void setInNumStrMap(Map<String, String> inNumStrMap) {
		this.inNumStrMap = inNumStrMap;
	}

	public Map<String, String> getNotinNumStrMap() {
		return notinNumStrMap;
	}

	public void setNotinNumStrMap(Map<String, String> notinNumStrMap) {
		this.notinNumStrMap = notinNumStrMap;
	}

	public String[] getInStringFields() {
		return inStringFields;
	}

	public void setInStringFields(String[] inStringFields) {
		this.inStringFields = inStringFields;
		//XXX: stringArray2Map
		this.inStringFieldsMap = this.stringArray2Map(inStringFields);
		this.setSqlMode(MiddleMode);
	}

	public String[] getNotinStringFields() {
		return notinStringFields;
	}

	public void setNotinStringFields(String[] notinStringFields) {
		this.notinStringFields = notinStringFields;
		//XXX: stringArray2Map
		this.notinStringFieldsMap = this.stringArray2Map(notinStringFields);
		this.setSqlMode(MiddleMode);
	}

	public Map<String, String> getInStringFieldsMap() {
		return inStringFieldsMap;
	}

	public void setInStringFieldsMap(Map<String, String> inStringFieldsMap) {
		this.inStringFieldsMap = inStringFieldsMap;
	}

	public Map<String, String> getNotinStringFieldsMap() {
		return notinStringFieldsMap;
	}

	public void setNotinStringFieldsMap(Map<String, String> notinStringFieldsMap) {
		this.notinStringFieldsMap = notinStringFieldsMap;
	}

	public List getDtField() {
		return dtField;
	}

	public void setDtField(List dtField) {
		this.dtField = dtField;
	}

	public String[] getOrderFieldASC() {
		return orderFieldASC;
	}

	/**
	 * 当使用jdbc时 使用表字段名称
	 * @param orderFieldASC
	 */
	public void setOrderFieldASC(String... orderFieldASC) {
		this.orderFieldASC = orderFieldASC;
	}

	public String[] getOrderFieldDESC() {
		return orderFieldDESC;
	}

	/**
	 * 当使用jdbc时 使用表字段名称
	 * @param orderFieldDESC
	 */
	public void setOrderFieldDESC(String... orderFieldDESC) {
		this.orderFieldDESC = orderFieldDESC;
	}

    
	

}
