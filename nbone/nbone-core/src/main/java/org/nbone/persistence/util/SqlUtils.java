package org.nbone.persistence.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.SqlPropertyDescriptor;
import org.nbone.persistence.SqlPropertyRange;
import org.nbone.persistence.criterion.QueryOperator;
import org.nbone.util.reflect.SimpleTypeMapper;

/**
 * 
 * @author thinking
 * @version 1.0 
 */
public class SqlUtils {
	
	/**
	 * "2014-01-01,kkk,000"  to (tempA.remark = '2014-01-01' or  tempA.remark = 'kkk' or  tempA.remark = '000')<br>
	 * "1,2,3"  to (tempA.remark = 1 or  tempA.remark = 2 or  tempA.remark = 3)
	 * @param name   Field name
	 * @param value  Field value
	 * @param clazz  target object class
	 * @return
	 */
    public static String stringSplit2In(String name, String value,Class<?> clazz)
    {
    	 String fh = "";
    	if(clazz == String.class){
    		    fh = "'";
    	}
        String arr[] = value.split(",");
        int length = arr.length;
        StringBuilder moreOrsb = new StringBuilder("( ");
        for(int i = 0; i < length-1; i++)
        {
            String did = arr[i];
            moreOrsb.append(name).append(" = ").append(fh).append(did).append(fh).append(" or ");
        }
        moreOrsb.append(name).append(" = ").append(fh).append(arr[length-1]).append(fh);
        moreOrsb.append(" )");
        return moreOrsb.toString();
    }
    
	/**
	 * "2014-01-01,kkk,000"  to (tempA.remark != '2014-01-01' and  tempA.remark != 'kkk' and  tempA.remark != '000')<br>
	 * "1,2,3"  to (tempA.remark != 1 and  tempA.remark != 2 and  tempA.remark != 3)
	 * @param name   Field name
	 * @param value  Field value
	 * @param clazz  target object class
	 * @return
	 */
    public static String stringSplit2Notin(String name, String value,Class<?> clazz)
    {
    	 String fh = "";
     	if(clazz == String.class){
     		    fh = "'";
     	}
         String arr[] = value.split(",");
         int length = arr.length;
         StringBuilder notinsb = new StringBuilder("( ");
         for(int i = 0; i < length-1; i++)
         {
             String did = arr[i];
             notinsb.append(name).append(" != ").append(fh).append(did).append(fh).append("  and ");
         }
         notinsb.append(name).append(" != ").append(fh).append(arr[length-1]).append(fh);
         notinsb.append(" )");
         return notinsb.toString();
    }
    
    public static StringBuilder list2In(String name,Collection<?> values){
		return list2In(name,values.toArray());
    }
    /**
     * [1,2,3] ---> id in (?,?,?)
     * @param name
     * @param values
     * @return
     */
    public static StringBuilder list2In(String name,Object[] values){
   	  StringBuilder sql = new StringBuilder(" ").append(name).append(" in ").append("(");
   	  int length = values.length;
   	  for (int i = 0; i <  length -1; i++) {
   		 sql.append(" ?, ");
	  }
      sql.append("? )");
	  return sql;
   }
    /**
     * [1,2,3] ---> id in (:name0,:name1,:name2)
     * @param name
     * @param values
     * @param namedParameters
     * @return
     */
    public static StringBuilder list2NamedIn(String name,Object[] values,Map<String,Object> namedParameters){
	      if(values == null || namedParameters ==null){
	    		return new StringBuilder();
	      }
     	  StringBuilder sql = new StringBuilder(" ").append(name).append(" in ").append("(");
     	  int length = values.length;
     	  String nameKey = ":"+name;
     	  for (int i = 0; i <  length -1; i++) {
     		 String nameKeyIndex = nameKey+i;
     		 String nameIndex = name+i;
     		 sql.append(" ").append(nameKeyIndex).append(", ");
     		 namedParameters.put(nameIndex, values[i]);
     		 
     	  }
     	  String nameKeyIndex = nameKey+(length -1);
     	  String nameIndex = name+(length -1);
	      sql.append(" ").append(nameKeyIndex).append(" )");
	      namedParameters.put(nameIndex, values[length -1]);
  	  return sql;
     }
    
    public static StringBuilder getHibernateWhereIn(SqlPropertyDescriptor hqlPropertyDescriptor,Object fieldValue,
            String aliasAndFieldName, Map<String,Object> namedParameters){
	    	String operType = hqlPropertyDescriptor.getOperType();
			String fieldName  = hqlPropertyDescriptor.getFieldName();
			Object specialValue = hqlPropertyDescriptor.getSpecialValue();
    		StringBuilder hqlsb  = new StringBuilder();  
    		String keyName = ":"+fieldName;
    		
	    	if(SqlPropertyDescriptor.in.equalsIgnoreCase(operType)|| SqlPropertyDescriptor.not_in.equalsIgnoreCase(operType) ){
				//hibernate custom index keyName
				hqlsb.append(" and ").append(aliasAndFieldName).append(" ").append(operType).append("(").append(keyName).append(")");
				//XXX: special Value
				namedParameters.put(fieldName, fieldValue);
				if(specialValue != null ){
					namedParameters.put(fieldName, specialValue);
				}
		
			}
    	
			return hqlsb;
    }
    public static StringBuilder getHibernateWhere(SqlPropertyDescriptor sqlPropertyDescriptor,Object fieldValue,
            String aliasAndFieldName, Map<String,Object> namedParameters){
    		StringBuilder hqlsb  = new StringBuilder();  
    		if(sqlPropertyDescriptor == null){
        		return hqlsb;
        	}
    		String operType = sqlPropertyDescriptor.getOperType();
    		if(sqlPropertyDescriptor.isBetween()){
    			
    			StringBuilder betweensql =  getWhereBetween(sqlPropertyDescriptor, fieldValue, aliasAndFieldName, namedParameters);
    			hqlsb.append(betweensql);
    			
    		}else if(SqlPropertyDescriptor.in.equalsIgnoreCase(operType)|| SqlPropertyDescriptor.not_in.equalsIgnoreCase(operType)){
    			
    			StringBuilder insql = getHibernateWhereIn(sqlPropertyDescriptor, fieldValue, aliasAndFieldName, namedParameters);
    			hqlsb.append(insql);
    			
    		}else{
    			StringBuilder sql = getCommonWherePart(sqlPropertyDescriptor, fieldValue, aliasAndFieldName, namedParameters);
        		hqlsb.append(sql);
        	
    		}
    		
			return hqlsb;
    }
    
    
    public static StringBuilder getSpringJdbcWhereIn(SqlPropertyDescriptor sqlPropertyDescriptor,Object fieldValue,
            String aliasAndFieldName, Map<String,Object> namedParameters){
    	String operType = sqlPropertyDescriptor.getOperType();
		String fieldName  = sqlPropertyDescriptor.getFieldName();
		Object specialValue = sqlPropertyDescriptor.getSpecialValue();
		
		Object[] values = null;
		if(specialValue instanceof Collection){
			values = ((Collection) specialValue).toArray();
		}else if (specialValue instanceof Object[]) {
			values = (Object[]) specialValue;
		}
		StringBuilder hqlsb  = new StringBuilder();
		
		if(values != null){
			if(SqlPropertyDescriptor.in.equalsIgnoreCase(operType)|| SqlPropertyDescriptor.not_in.equalsIgnoreCase(operType) ){
				hqlsb.append(" and ").append(list2NamedIn(fieldName, values, namedParameters));  
			}
		}
    	
		return hqlsb;
    }
    
    public static StringBuilder getSpringJdbcWhere(SqlPropertyDescriptor sqlPropertyDescriptor,Object fieldValue,
            String aliasAndFieldName, Map<String,Object> namedParameters){
    	StringBuilder hqlsb  = new StringBuilder();  
    	if(sqlPropertyDescriptor == null){
    		return hqlsb;
    	}
    	String operType = sqlPropertyDescriptor.getOperType();
    	if(sqlPropertyDescriptor.isBetween()){
    		
    		StringBuilder betweensql = getWhereBetween(sqlPropertyDescriptor, fieldValue, aliasAndFieldName, namedParameters);
    		hqlsb.append(betweensql);
    		
    	}else if(SqlPropertyDescriptor.in.equalsIgnoreCase(operType)|| SqlPropertyDescriptor.not_in.equalsIgnoreCase(operType)){
    		
    		StringBuilder insql = getSpringJdbcWhereIn(sqlPropertyDescriptor, fieldValue, aliasAndFieldName, namedParameters);
        	hqlsb.append(insql);
        	
    	}else{
    		StringBuilder sql = getCommonWherePart(sqlPropertyDescriptor, fieldValue, aliasAndFieldName, namedParameters);
        	hqlsb.append(sql);
    	}
    	
		return hqlsb;
    }
    
    
    
    public static  StringBuilder getWhereBetween(SqlPropertyDescriptor sqlPropertyDescriptor,Object fieldValue,
            String aliasAndFieldName, Map<String,Object> namedParameters){
    	StringBuilder hqlsb  = new StringBuilder();  
		String fieldName  = sqlPropertyDescriptor.getFieldName();
		boolean isBetween = sqlPropertyDescriptor.isBetween();
		String placeholderPrefix = ":";
		String placeholderSuffix =" ";
		if(isBetween){
			Object beginValue = sqlPropertyDescriptor.getBeginValue();
			Object endValue =	sqlPropertyDescriptor.getEndValue();
			String beginValueMark = sqlPropertyDescriptor.getBeginValueMark();
			String endValueMark = sqlPropertyDescriptor.getEndValueMark();
			
			String beginKey = "begin"+fieldName;
			String endKey   = "end"+fieldName;
			
			namedParameters.put(beginKey, beginValue);
			namedParameters.put(endKey, endValue);
			
			hqlsb.append(" and ");
			hqlsb.append(" ( ");
			hqlsb.append(aliasAndFieldName).append(" ").append(beginValueMark).append(" ");
			hqlsb.append(placeholderPrefix).append(beginKey).append(placeholderSuffix);
			hqlsb.append(" and ");
			hqlsb.append(aliasAndFieldName).append(" ").append(endValueMark).append(" ");
			hqlsb.append(placeholderPrefix).append(endKey).append(placeholderSuffix);
			hqlsb.append(" ) ");
		
		}
    	
		return hqlsb;
    }
    /**
     * 
     * 此方法没有设置in语句
     * @param hqlPropertyDescriptor
     * @param fieldValue
     * @param aliasAndFieldName
     * @param namedParameters
     * @return
     */
	public static StringBuilder getCommonWherePart(SqlPropertyDescriptor sqlPropertyDescriptor,Object fieldValue,
            String aliasAndFieldName, Map<String,Object> namedParameters){
			StringBuilder hqlsb  = new StringBuilder();  
			String placeholderPrefix = ":";
			String placeholderSuffix =" ";
			if(sqlPropertyDescriptor == null){
				return hqlsb;
			}
			String operType = sqlPropertyDescriptor.getOperType();
			String fieldName  = sqlPropertyDescriptor.getFieldName();
		
			// and  user.id=5
			if(SqlPropertyDescriptor.OperTypeSet.contains(operType)){
			
				hqlsb.append(" and ").append(aliasAndFieldName).append(" ").append(operType).append(" ").append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
				namedParameters.put(fieldName, fieldValue);
			
			}else if(SqlPropertyDescriptor.like.equalsIgnoreCase(operType)){
				hqlsb.append(" and ").append(aliasAndFieldName).append(" like  '%").append(fieldValue).append("%'");
			
			}else if(SqlPropertyDescriptor.left_like.equalsIgnoreCase(operType)){
			
				hqlsb.append(" and ").append(aliasAndFieldName).append(" like  '%").append(fieldValue).append("'");
			
			}else if(SqlPropertyDescriptor.right_like.equalsIgnoreCase(operType)){
			
				hqlsb.append(" and ").append(aliasAndFieldName).append(" like  '").append(fieldValue).append("%'");
			
			}else if(SqlPropertyDescriptor.is_null.equalsIgnoreCase(operType) || SqlPropertyDescriptor.is_not_null.equalsIgnoreCase(operType) ){
			
				hqlsb.append(" and ").append(aliasAndFieldName).append(" ").append(operType);
			
			}
			else {
				StringBuilder tempSql =todefaultOperType(fieldName, fieldValue, aliasAndFieldName, namedParameters);
				hqlsb.append(tempSql);
			}
			
			return hqlsb;
			
	}
	
	public static StringBuilder  todefaultOperType(String fieldName,Object fieldValue,String aliasAndFieldName, Map<String,Object> namedParameters){
		StringBuilder hqlsb  = new StringBuilder();  
		String placeholderPrefix = ":";
		String placeholderSuffix =" ";
		Class<?> clazz = fieldValue.getClass();
		if(SimpleTypeMapper.isPrimitiveWithString(clazz)){
		
			hqlsb.append(" and ").append(aliasAndFieldName).append(" like  '%").append(fieldValue).append("%'");
		
		}else if(SimpleTypeMapper.isPrimitiveWithNumber(clazz)){
		
			hqlsb.append(" and ").append(aliasAndFieldName).append(" = ").append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
			namedParameters.put(fieldName, fieldValue);
		}else{
			//not is  number/String
			//hibernate custom index keyName
			hqlsb.append(" and ").append(aliasAndFieldName).append(" = ").append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
		
			namedParameters.put(fieldName, fieldValue);
	   }
		
		return hqlsb;
	}
	/**
	 * eg:beginTime <= value <= endTime (beginTime <= value and   endTime >= value)
	 * @param sqlPropertyRange
	 * @param namedParameters
	 * @return
	 */
	public static StringBuilder getPropertyRange(SqlPropertyRange sqlPropertyRange, Map<String,Object> namedParameters) {
		StringBuilder sqlsb  = new StringBuilder();
		if(sqlPropertyRange  == null ){
			return sqlsb;
		}
		String placeholderPrefix = ":";
		String placeholderSuffix =" ";
		String dbleftField  = sqlPropertyRange.getDbLeftField();
		String dbrightField  = sqlPropertyRange.getDbRightField();
		String beginValueMark = sqlPropertyRange.getLeftValueMark();
		String endValueMark = sqlPropertyRange.getRightValueMark();
		String named  = sqlPropertyRange.getLeftField()+"Range";
		Object value  = sqlPropertyRange.getValue();
		namedParameters.put(named, value);
		
		sqlsb.append(" and ");
		sqlsb.append(" ( ");
		sqlsb.append(dbleftField).append(" ").append(beginValueMark).append(" ").append(placeholderPrefix).append(named).append(placeholderSuffix);
		sqlsb.append(" and ");
		sqlsb.append(dbrightField).append(" ").append(endValueMark).append(" ").append(placeholderPrefix).append(named).append(placeholderSuffix);
		sqlsb.append(" ) ");
		
		
		return sqlsb;
	}
		
	/**
	 * 
	 * @param sqlConfig
	 * @return
	 */
	public static StringBuilder getOrderBy(SqlConfig sqlConfig,boolean isAliasName) {
		String[] orderAscArray = sqlConfig.getOrderFieldASC();
	    String[] orderDescArray = sqlConfig.getOrderFieldDESC();
	    if(orderAscArray == null  && orderDescArray == null){
	    	return new StringBuilder();
	    }
	    
	    List<String> orderList = new ArrayList<String>();
	    List<String> orderAscList = getPartOrderBy(orderAscArray, sqlConfig, isAliasName ,QueryOperator.ASC);
	    List<String> orderDescList = getPartOrderBy(orderDescArray, sqlConfig, isAliasName, QueryOperator.DESC);
	    orderList.addAll(orderAscList);
	    orderList.addAll(orderDescList);
	    
	    //user.id DESC , user.age DESC
		int orderCount = orderList.size();
		if(orderCount > 0){
			StringBuilder orderHqlsb  = new StringBuilder(" order by ");  
			for (int i = 0; i < orderCount - 1; i++) {
				String oneOrder = orderList.get(i);
				orderHqlsb.append(oneOrder).append(" , ");
				
			}
			String oneOrder = orderList.get(orderCount - 1);
			orderHqlsb.append(oneOrder);
			return orderHqlsb;
			
		}
		return new StringBuilder();
	}
	
	private static List<String> getPartOrderBy(String[] orderList,SqlConfig sqlConfig,boolean isAliasName,String orderType) {
		List<String> resultList = new ArrayList<String>();
		if(orderList == null || orderList.length == 0){
			return resultList;
		}
		if(orderType == null){
			orderType = QueryOperator.ASC;
		}
		orderType = orderType.toLowerCase();
		if(!orderType.equals(QueryOperator.ASC) && !orderType.equals(QueryOperator.DESC)){
			orderType = QueryOperator.ASC;
		}
		String aliasName = "";
		if(isAliasName){
			
			aliasName = sqlConfig.getAliasName()+".";
		}
		
		for (int i = 0; i < orderList.length; i++) {
			StringBuilder orderHqlsb  = new StringBuilder();
			String fieldName = orderList[i];
			orderHqlsb.append(aliasName).append(fieldName).append(" ").append(orderType);
			resultList.add(orderHqlsb.toString());
		}
		return resultList;
	}
	
	
    
    
    
    //Like Match Char _ %
    public static boolean isLikeMatchChar(String value)
    {
      boolean result = false;
      if ((value.contains("_")) || (value.contains("%"))) {
        return true;
      }
      return result;
    }

    public static String dealLikeMatchChar(String value)
    {
      if (isLikeMatchChar(value)) {
        value = value.replace("_", "\\_");
        value = value.replace("%", "\\%");
      }
      return value;
    }
    
	public static void main(String[] args) {
		String ss = "kk,oo";
		String ss4int = "1,2,3";
		System.out.println(stringSplit2In("user.id", ss, String.class));
		System.out.println(stringSplit2In("user.id", ss4int,Integer.class));
		
		System.out.println(stringSplit2Notin("user.id", ss4int,String.class));
		System.out.println(stringSplit2Notin("user.id", ss4int,Integer.class));
		
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuilder sql = list2NamedIn("id",new Object[]{1,2,3},map);
		
		System.out.println(sql);
		
		
	}

}
