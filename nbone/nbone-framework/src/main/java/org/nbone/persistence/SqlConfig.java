package org.nbone.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import org.nbone.mvc.domain.GroupQuery;
import org.nbone.persistence.annotation.FieldLevel;
import org.nbone.persistence.util.SqlUtils;

/**
 * 
 * @author thinking
 * @version 1.0 
 */
@Data
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
     * 字段级别
     */
    private FieldLevel fieldLevel;
	/**
	 * 分组查询
	 */
    private GroupQuery groupQuery;
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

	public static String andInNumber(String dbFieldName,boolean isIn,String values) {
		return dbInNumber("and",dbFieldName,isIn,values);
	}
	public static String dbInNumber(String andOr,String dbFieldName,boolean isIn,String values) {
		if(values == null){
			return  null;
		}
		if(dbFieldName == null){
			throw  new IllegalArgumentException("dbFieldName  is null.");
		}
		if(andOr == null){ andOr = ""; }

		String operType = isIn ? " in " : " not in ";
		StringBuilder sqlsb= new StringBuilder();
		// and id in (1,2,3)
		sqlsb.append(" ").append(andOr).append(" ").append(dbFieldName).append(" ").append(operType).append(" (").append(values).append(")");
		return sqlsb.toString();
	}


	public static String in(String andOr,String dbFieldName, Class<?> nameType, Object values, boolean isIn){
        StringBuilder sql = SqlUtils.in(andOr,dbFieldName,nameType,values,isIn);
		return sql != null ? sql.toString():null;
	}

	public static String dbBetween(String andOr,String dbFieldName,String beginFieldName,String endFieldName,boolean is) {
		StringBuilder sql = SqlUtils.dbBetween(andOr,dbFieldName,beginFieldName,endFieldName,is);
		return sql != null ? sql.toString():null;
	}
	public static String dbBetween(String andOr,String dbFieldName,Object[] values,boolean is) {
		StringBuilder sql = SqlUtils.dbBetween(andOr,dbFieldName,values,is);
		return sql != null ? sql.toString(): null;
	}



	public static SqlConfig  build() {
		return new SqlConfig();
	}
	public SqlConfig withFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
		return this;
	}
	/**
	 *
	 * @param fieldName
	 * @param isIn
	 * @param values   object[] / List / number strings -> 1,2,3,4,5 / strings list-> 693fe72810,f84f369553
	 * @return
	 */
	public  SqlConfig  in(String fieldName,boolean isIn,Object values) {
		this.sqlMode = HighMode;
		this.sqlPds.addSqlPdIn(fieldName,isIn, values);
		return this;
	}

	/**
	 *
	 * @param fieldName
	 * @param isIn
	 * @param values  "1,2,3,4,5"
	 * @return
	 */
	public  SqlConfig  inNumber(String fieldName,boolean isIn,String values) {
		return in(fieldName,isIn,values);
	}

	public  SqlConfig  between(String fieldName,Object beginValue,Object endValue) {
		this.sqlMode = HighMode;
		this.sqlPds.addSqlPdBetween(fieldName,beginValue, endValue);
		return this;
	}



	
	public SqlConfig addSqlPd(String fieldName,String operType) {
		this.sqlPds.addSqlPd(fieldName, operType);
		return this;
	}
	
	public SqlConfig addSqlPdBetween(String fieldName,Object beginValue,Object endValue) {
		this.sqlPds.addSqlPdBetween(fieldName, beginValue, endValue);
		return this;
	}
	public SqlConfig addSqlPdIn(String fieldName,boolean isIn,Object[] values) {
		this.sqlPds.addSqlPdIn(fieldName,isIn, values);
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


	public void setInStringFields(String[] inStringFields) {
		this.inStringFields = inStringFields;
		//XXX: stringArray2Map
		this.inStringFieldsMap = this.stringArray2Map(inStringFields);
		this.setSqlMode(MiddleMode);
	}

	public void setNotinStringFields(String[] notinStringFields) {
		this.notinStringFields = notinStringFields;
		//XXX: stringArray2Map
		this.notinStringFieldsMap = this.stringArray2Map(notinStringFields);
		this.setSqlMode(MiddleMode);
	}


	/**
	 * 当使用jdbc时 使用表字段名称
	 * @param orderFieldASC
	 */
	public void setOrderFieldsASC(String... orderFieldASC) {
		this.orderFieldASC = orderFieldASC;
	}

	/**
	 * 当使用jdbc时 使用表字段名称
	 * @param orderFieldDESC
	 */
	public void setOrderFieldsDESC(String... orderFieldDESC) {
		this.orderFieldDESC = orderFieldDESC;
	}


}
