package org.nbone.framework.hibernate.util;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.util.SqlUtils;
import org.nbone.util.reflect.SimpleTypeMapper;

public class HibernateClassUtils {

	protected  static Log logger = LogFactory.getLog(HibernateClassUtils.class);
	
	public static String getFirstPartHQL(Object bean,HqlConfig hqlConfig) {
		String aliasName  = hqlConfig.getAliasName();
		//from org.nbone.User tempA  where 1 = 1 
		StringBuilder hqlsbPart  = new StringBuilder();
		hqlsbPart.append("from ").append(bean.getClass().getName()).append(" ").append(aliasName).append(" where 1 = 1 ");
		return hqlsbPart.toString();
		
	}
	
	public static String getSimpleHQL(Object bean) {
		HqlConfig hqlConfig = new HqlConfig();
		return getSimpleHQL(bean,hqlConfig);
	}
	
	public static String getSimpleHQL(Object bean,HqlConfig hqlConfig) {
		
		 StringBuilder hqlsb  = new StringBuilder();
		 String hqlsbPart = getFirstPartHQL(bean, hqlConfig);
		 hqlsb.append(hqlsbPart);
		
		 hqlConfig.setHqlMode(HqlConfig.PrimaryMode);
		 HqlQuery hqlQuery = getWhere(bean, hqlConfig,null);
		 String wherePart = hqlQuery.getHqlWhereAfter();
		 hqlsb.append(wherePart);
		
		return hqlsb.toString();
	}
	
	public static HqlQuery getHql(Object bean,HqlConfig hqlConfig) {
		return getHql(bean, hqlConfig, null);
	}
	
	private static HqlQuery getHql(Object bean,HqlConfig hqlConfig,String callbackAliasName) {
		 if (bean == null ) {
			   throw  new IllegalArgumentException("bean must is  not null . ");
		 }
		 if (hqlConfig == null ) {
			   throw  new IllegalArgumentException("hqlConfig must is  not null . ");
		 }
		String hqlFirstPart = getFirstPartHQL(bean, hqlConfig);
		HqlQuery hqlQuery = getWhere(bean, hqlConfig, callbackAliasName);
		hqlQuery.setHqlWhereBefore(hqlFirstPart);
		return hqlQuery;
	}
	
	protected static HqlQuery getWhere(Object bean,HqlConfig hqlConfig,String callbackAliasName) {
		if(hqlConfig == null){
			return null;
		}
        String tabAlias = hqlConfig.getAliasName();
        
        Map<String,HqlPropertyDescriptor> hqlpdsMap = hqlConfig.getHqlPds();
        List<Class<?>> pojoRefs = hqlConfig.getPojoRefs();
        int  hqlMode =  hqlConfig.getHqlMode();
        
        if(callbackAliasName != null){
        	tabAlias = callbackAliasName;
        }
        
		StringBuilder hqlsb  = new StringBuilder();  
		
		Map<String,Object> namedParameters =  new HashMap<String, Object>();
		
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(bean);
		for (int i = 0; i < pds.length; i++) {
			PropertyDescriptor propertyDescriptor = pds[i];
			
			String fieldName = propertyDescriptor.getName();
			Class<?> fieldType  = propertyDescriptor.getPropertyType();
			if(fieldType == Class.class){
				continue;
			}
			Object fieldValue = null;
			
			String aliasAndFieldName  = tabAlias+"."+fieldName;
			try {
				fieldValue = PropertyUtils.getProperty(bean, fieldName);
			} catch (Exception e) {
				logger.error(e,e);
			}
			
			if(fieldValue != null){
				//is Number   
				//filter [0/0L/0.0/0.0F] is default value clear
				if(SimpleTypeMapper.isPrimitiveWithNumber(fieldType)){
					
					if("0".equals(fieldValue.toString()) || "0.0".equals(fieldValue.toString())){
						continue;
					 }
				}
				
				if(hqlMode==HqlConfig.PrimaryMode){
					String wherePart =toPrimaryMode(hqlConfig, fieldName, fieldValue, fieldType, aliasAndFieldName);
					hqlsb.append(wherePart);
				}else if(hqlMode==HqlConfig.MiddleMode){
					//String Value split "nbone1,nbone2,nbone3"
					String wherePart = toMiddleMode(hqlConfig,fieldName, fieldValue,fieldType,aliasAndFieldName);
					hqlsb.append(wherePart);
				}
				//highMode---
				else if (hqlMode==HqlConfig.HighMode){
					//--------------------------thinking
					if(hqlpdsMap != null && hqlpdsMap.containsKey(fieldName)) {
						String wherePart = toHighMode(hqlConfig, fieldName, fieldValue,fieldType,aliasAndFieldName,namedParameters);
						hqlsb.append(wherePart);
					}
					else{
						//to Primary
						String wherePart  = toPrimaryMode(hqlConfig, fieldName, fieldValue, fieldType, aliasAndFieldName);
						hqlsb.append(wherePart);
					}
					
				}
				//---TODO:
				else{}
				
				 //Object include Object
				if(pojoRefs != null && pojoRefs.size() > 0 ){
					for (int j = 0; j < pojoRefs.size(); j++) {
						if(fieldType == pojoRefs.get(j)){
							String callbackAliasName1  =  aliasAndFieldName;
							Object callBean = fieldValue;
							HqlQuery callquery = getWhere(callBean, hqlConfig, callbackAliasName1);
							if(callquery != null){
								hqlsb.append(callquery.getHqlWhereAfter());
							}
							break;
						}
					}
					
				}
				
			}
		
			
		  }
		  //---------------order by -------------------
		 String queryOrder = getOrderBy(hqlConfig);
		 hqlsb.append(queryOrder);
		  
		  HqlQuery hqlQuery = new HqlQuery(null, hqlsb.toString(), hqlConfig);
		  hqlQuery.setNameKeyMap(namedParameters);
		
		return  hqlQuery;
	}
	protected static String getOrderBy(HqlConfig hqlConfig) {
		String[] orderAscArray = hqlConfig.getOrderFieldASC();
	    String[] orderDescArray = hqlConfig.getOrderFieldDESC();
	    String aliasName =  hqlConfig.getAliasName();
	    
	    List<String> orderList = new ArrayList<String>();
	    List<String> orderAscList = getPartOrderBy(orderAscArray, aliasName, HqlPropertyDescriptor.ASC);
	    List<String> orderDescList = getPartOrderBy(orderDescArray, aliasName, HqlPropertyDescriptor.DESC);
	    orderList.addAll(orderAscList);
	    orderList.addAll(orderDescList);
	    
	    ////user.id DESC , user.age DESC
		int orderCount = orderList.size();
		if(orderCount > 0){
			StringBuilder orderHqlsb  = new StringBuilder(" order by ");  
			for (int i = 0; i < orderCount - 1; i++) {
				String oneOrder = orderList.get(i);
				orderHqlsb.append(oneOrder).append(" , ");
				
			}
			String oneOrder = orderList.get(orderCount - 1);
			orderHqlsb.append(oneOrder);
			return orderHqlsb.toString();
			
		}
		return "";
	}
	
	private static List<String> getPartOrderBy(String[] orderList,String aliasName,String orderType) {
		List<String> resultList = new ArrayList<String>();
		if(orderList == null || orderList.length == 0){
			return resultList;
		}
		if(orderType == null){
			orderType = HqlPropertyDescriptor.ASC;
		}
		orderType = orderType.toLowerCase();
		if(!orderType.equals(HqlPropertyDescriptor.ASC) && !orderType.equals(HqlPropertyDescriptor.DESC)){
			orderType = HqlPropertyDescriptor.ASC;
		}
		
		for (int i = 0; i < orderList.length; i++) {
			StringBuilder orderHqlsb  = new StringBuilder();
			String fieldName = orderList[i];
			orderHqlsb.append(aliasName).append(".").append(fieldName).append(" ").append(orderType);
			resultList.add(orderHqlsb.toString());
		}
		return resultList;
	}
	

	//-----------------------------------------------------------------
	//toPrimaryMode
	//-----------------------------------------------------------------
	protected static String  toPrimaryMode(HqlConfig hqlConfig,String fieldName,Object fieldValue,Class<?> fieldType,
            String aliasAndFieldName){
		StringBuilder hqlsbPart = new StringBuilder();
		//is Number  
		if(SimpleTypeMapper.isPrimitiveWithNumber(fieldType)){
			
			hqlsbPart.append(" and ").append(aliasAndFieldName).append(" = ").append(fieldValue);
		}
		//is String
		else if(SimpleTypeMapper.isPrimitiveWithString(fieldType)){
				
			hqlsbPart.append(" and ").append(aliasAndFieldName).append(" like  '%").append(fieldValue).append("%'");
			
		
		}else{
			
		}
		String result  = hqlsbPart.toString();
		return result;
		
	}
	
	
	//-----------------------------------------------------------------
	//toMiddleMode
	//-----------------------------------------------------------------
	protected static String toMiddleMode(HqlConfig hqlConfig,String fieldName,Object fieldValue,Class<?> fieldType,
			                           String aliasAndFieldName){
		 String alias = hqlConfig.getAliasName();
		 StringBuilder hqlsbPart = new StringBuilder();
		 Map<String,String> inNumStrMap = hqlConfig.getInNumStrMap();
		 Map<String,String> notinNumStrMap = hqlConfig.getNotinNumStrMap();
		 
		 Map<String,String> InStringFieldsMap = hqlConfig.getInStringFieldsMap();
		 Map<String,String> notinStringFieldsMap = hqlConfig.getNotinStringFieldsMap();
		 
		 if(SimpleTypeMapper.isPrimitiveWithNumber(fieldType)){
			 boolean ifrun = true;
			 if(ifrun && inNumStrMap != null && inNumStrMap.containsKey(fieldName)){
				 String value = inNumStrMap.get(fieldName);
				 String in = SqlUtils.stringSplit2In(aliasAndFieldName, value,int.class);
				 hqlsbPart.append(" and ").append(in);
				 ifrun =false;
			 }
			 if(ifrun && notinNumStrMap != null && notinNumStrMap.containsKey(fieldName)){
				 String value = notinNumStrMap.get(fieldName);
				 String in = SqlUtils.stringSplit2Notin(aliasAndFieldName, value,int.class);
				 hqlsbPart.append(" and ").append(in);
				 
				 ifrun =false;
			 }
			 if(ifrun){
				 hqlsbPart.append(" and ").append(aliasAndFieldName).append(" = ").append(fieldValue);
			 }
			 
		 }else if(SimpleTypeMapper.isPrimitiveWithString(fieldType)){
			 String value = String.valueOf(fieldValue);
			 boolean ifrun = true;
			 if(ifrun && InStringFieldsMap != null && InStringFieldsMap.containsKey(fieldName)){
				 
				 String in = SqlUtils.stringSplit2In(aliasAndFieldName, value,String.class);
				 hqlsbPart.append(" and ").append(in);
				 ifrun =false;
			 }
			 if(ifrun && notinStringFieldsMap != null && notinStringFieldsMap.containsKey(fieldName)){
				 
				 String in = SqlUtils.stringSplit2Notin(aliasAndFieldName, value,String.class);
				 hqlsbPart.append(" and ").append(in);
				 
				 ifrun =false;
			 }
			 if(ifrun){
				 hqlsbPart.append(" and ").append(aliasAndFieldName).append(" like  '%").append(fieldValue).append("%'");
			 }
			 
		 }else{
			    if(logger.isDebugEnabled()){
			    	logger.warn("not config .....................");
			    }
		    }
		 
		 String result  = hqlsbPart.toString();
		 return result;
		
	}
	
	//-----------------------------------------------------------------
	//toHighMode
	//-----------------------------------------------------------------
	protected static String toHighMode(HqlConfig hqlConfig,String fieldName,Object fieldValue,Class<?> fieldType, String aliasAndFieldName,
			                          Map<String,Object> namedParameters){
		 Map<String,HqlPropertyDescriptor> hqlpdsMap = hqlConfig.getHqlPds();
		 HqlPropertyDescriptor hqlPropertyDescriptor = hqlpdsMap.get(fieldName);
		 boolean iswhere  = hqlPropertyDescriptor.isHasWhere();
		
		 StringBuilder hqlsbPart = new StringBuilder();
			
		 if(iswhere) {
			String hqlpart = getHighWherePart(hqlPropertyDescriptor,fieldValue,aliasAndFieldName,namedParameters);
		 	hqlsbPart.append(hqlpart);
		 }
			
		
		String result  = hqlsbPart.toString();
		return result;
	}
	
	protected static String getHighWherePart(HqlPropertyDescriptor hqlPropertyDescriptor,Object fieldValue,
			                              String aliasAndFieldName, Map<String,Object> namedParameters){
		StringBuilder hqlsb  = new StringBuilder();  
		String operType = hqlPropertyDescriptor.getOperType();
		String fieldName  = hqlPropertyDescriptor.getFieldName();
		boolean isBetween = hqlPropertyDescriptor.isBetween();
		Object specialValue = hqlPropertyDescriptor.getSpecialValue();
		String keyName = ":"+fieldName;
		if(isBetween){
			Object beginValue = hqlPropertyDescriptor.getBeginValue();
			Object endValue =	hqlPropertyDescriptor.getEndValue();
			String beginValueMark = hqlPropertyDescriptor.getBeginValueMark();
			String endValueMark = hqlPropertyDescriptor.getEndValueMark();
			
			String beginKey = "begin"+fieldName;
			String endKey   = "end"+fieldName;
			String beginkeyName = ":"+beginKey;
			String endkeyName =   ":"+endKey;
			
			namedParameters.put(beginKey, beginValue);
			namedParameters.put(endKey, endValue);
			
			hqlsb.append(" and ");
			hqlsb.append(" ( ");
			hqlsb.append(aliasAndFieldName).append(beginValueMark).append(beginkeyName);
			hqlsb.append(" and ");
			hqlsb.append(aliasAndFieldName).append(endValueMark).append(endkeyName);
			hqlsb.append(" ) ");
			
		}else{
			// and  user.id=5
			if(HqlPropertyDescriptor.OperTypeSet.contains(operType)){
				
				hqlsb.append(" and ").append(aliasAndFieldName).append(operType).append(keyName);
				String keyName1 = fieldName;
				namedParameters.put(keyName1, fieldValue);
				
			}else if(HqlPropertyDescriptor.in.equals(operType)|| HqlPropertyDescriptor.not_in.equals(operType) ){
				//hibernate custom index keyName
				hqlsb.append(" and ").append(aliasAndFieldName).append(" ").append(operType).append("(").append(keyName).append(")");
				//XXX: special Value
				String keyName1 = fieldName;
				namedParameters.put(keyName1, fieldValue);
				if(specialValue != null ){
					namedParameters.put(keyName1, specialValue);
				}
				
				
			}else if(HqlPropertyDescriptor.like.equals(operType)){
				hqlsb.append(" and ").append(aliasAndFieldName).append(" like  '%").append(fieldValue).append("%'");
				
			}else if(HqlPropertyDescriptor.left_like.equals(operType)){
				
				hqlsb.append(" and ").append(aliasAndFieldName).append(" like  '%").append(fieldValue).append("'");
				
			}else if(HqlPropertyDescriptor.right_like.equals(operType)){
				
				hqlsb.append(" and ").append(aliasAndFieldName).append(" like  '").append(fieldValue).append("%'");
				
			}else if(HqlPropertyDescriptor.is_null.equals(operType) || HqlPropertyDescriptor.is_not_null.equals(operType) ){
				
				hqlsb.append(" and ").append(aliasAndFieldName).append(" ").append(operType);
				
			}
			else {
				 Class<?> clazz = fieldValue.getClass();
				 if(SimpleTypeMapper.isPrimitiveWithString(clazz)){
					 
					 hqlsb.append(" and ").append(aliasAndFieldName).append(" like  '%").append(fieldValue).append("%'");
					 
				 }else if(SimpleTypeMapper.isPrimitiveWithNumber(clazz)){
					 
					 hqlsb.append(" and ").append(aliasAndFieldName).append(" = ").append(fieldValue);
					 
				 }else{
					 //not is  number/String
					//hibernate custom index keyName
					 hqlsb.append(" and ").append(aliasAndFieldName).append(" = ").append(keyName);
					 
					 String keyName1 = fieldName;
					 namedParameters.put(keyName1, fieldValue);
				 }
			}
			
			
		}
		
		return hqlsb.toString();
	}
	

}
