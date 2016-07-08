package org.nbone.framework.hibernate.util;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.SqlPropertyDescriptor;
import org.nbone.persistence.util.SqlUtils;
import org.nbone.util.reflect.SimpleTypeMapper;
/**
 * 
 * @author thinking
 * @version 1.0 
 */
public class HibernateClassUtils {

	protected  static Log logger = LogFactory.getLog(HibernateClassUtils.class);
	
	public static String getFirstPartHQL(Object bean,SqlConfig hqlConfig) {
		String aliasName  = hqlConfig.getAliasName();
		//from org.nbone.User tempA  where 1 = 1 
		StringBuilder hqlsbPart  = new StringBuilder();
		hqlsbPart.append("from ").append(bean.getClass().getName()).append(" ").append(aliasName).append(" where 1 = 1 ");
		return hqlsbPart.toString();
		
	}
	
	public static String getSimpleHQL(Object bean) {
		SqlConfig hqlConfig = new SqlConfig();
		return getSimpleHQL(bean,hqlConfig);
	}
	
	public static String getSimpleHQL(Object bean,SqlConfig hqlConfig) {
		
		 StringBuilder hqlsb  = new StringBuilder();
		 String hqlsbPart = getFirstPartHQL(bean, hqlConfig);
		 hqlsb.append(hqlsbPart);
		
		 hqlConfig.setSqlMode(SqlConfig.PrimaryMode);
		 HqlQuery hqlQuery = getWhere(bean, hqlConfig,null);
		 String wherePart = hqlQuery.getHqlWhereAfter();
		 hqlsb.append(wherePart);
		
		return hqlsb.toString();
	}
	
	public static HqlQuery getHql(Object bean,SqlConfig hqlConfig) {
		return getHql(bean, hqlConfig, null);
	}
	
	private static HqlQuery getHql(Object bean,SqlConfig hqlConfig,String callbackAliasName) {
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
	
	protected static HqlQuery getWhere(Object bean,SqlConfig hqlConfig,String callbackAliasName) {
		if(hqlConfig == null){
			return null;
		}
        String tabAlias = hqlConfig.getAliasName();
        
        Map<String,SqlPropertyDescriptor> hqlpdsMap = hqlConfig.getSqlPds();
        List<Class<?>> pojoRefs = hqlConfig.getPojoRefs();
        int  hqlMode =  hqlConfig.getSqlMode();
        
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
				
				if(hqlMode==SqlConfig.PrimaryMode){
					String wherePart =toPrimaryMode(hqlConfig, fieldName, fieldValue, fieldType, aliasAndFieldName);
					hqlsb.append(wherePart);
				}else if(hqlMode==SqlConfig.MiddleMode){
					//String Value split "nbone1,nbone2,nbone3"
					String wherePart = toMiddleMode(hqlConfig,fieldName, fieldValue,fieldType,aliasAndFieldName);
					hqlsb.append(wherePart);
				}
				//highMode---
				else if (hqlMode==SqlConfig.HighMode){
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
		 StringBuilder queryOrder = SqlUtils.getOrderBy(hqlConfig,true);
		 if(queryOrder != null){
			 hqlsb.append(queryOrder);
		 }
		  
		  HqlQuery hqlQuery = new HqlQuery(null, hqlsb.toString(), hqlConfig);
		  hqlQuery.setNameKeyMap(namedParameters);
		
		return  hqlQuery;
	}

	

	

	//-----------------------------------------------------------------
	//toPrimaryMode 纯String 拼接含有sql注入风险
	//-----------------------------------------------------------------
	protected static String  toPrimaryMode(SqlConfig hqlConfig,String fieldName,Object fieldValue,Class<?> fieldType,
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
	//toMiddleMode 考虑到in语句 含有sql注入风险
	//-----------------------------------------------------------------
	protected static String toMiddleMode(SqlConfig hqlConfig,String fieldName,Object fieldValue,Class<?> fieldType,
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
	//toHighMode 占位符方式sql
	//-----------------------------------------------------------------
	protected static String toHighMode(SqlConfig hqlConfig,String fieldName,Object fieldValue,Class<?> fieldType, String aliasAndFieldName,
			                          Map<String,Object> namedParameters){
		SqlPropertyDescriptor hqlPropertyDescriptor = hqlConfig.getSqlPd(fieldName);
		 boolean iswhere  = hqlPropertyDescriptor.isHasWhere();
		
		 StringBuilder hqlsbPart = new StringBuilder();
			
		 if(iswhere) {
			StringBuilder hqlpart = SqlUtils.getHibernateWhere(hqlPropertyDescriptor, fieldValue, aliasAndFieldName, namedParameters);
		 	hqlsbPart.append(hqlpart);
		 }
			
		
		String result  = hqlsbPart.toString();
		return result;
	}
	

	

}
