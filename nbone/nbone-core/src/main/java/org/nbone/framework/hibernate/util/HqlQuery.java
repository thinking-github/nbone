package org.nbone.framework.hibernate.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 
 * @author thinking
 * @version 1.0 
 */
@SuppressWarnings("rawtypes")
public class HqlQuery {
	protected  static Log logger = LogFactory.getLog(HqlQuery.class);
	/**
	 * HQL query String
	 */
	private String queryString;
	
	/**
	 * 
	 */
	private String hqlWhereBefore;
	/**
	 * where to end 
	 */
	private String hqlWhereAfter;
	
	private Map<String, Object> nameKeyMap;
	

	// parameter bind values...
	private List<Object> values;
	private List types;
	
	//current HqlConfig
	private HqlConfig hqlConfig;
	
	
	
	public HqlQuery(String queryString, HqlConfig hqlConfig) {
		this.queryString = queryString;
		this.hqlConfig = hqlConfig;
	}

    public HqlQuery(String hqlWhereBefore,String hqlWhereAfter, HqlConfig hqlConfig) {
    	this.hqlWhereBefore = hqlWhereBefore;
		this.hqlWhereAfter = hqlWhereAfter;
		this.hqlConfig = hqlConfig;
	}
	//XXX:thinking
	public String getQueryString() {
		if(StringUtils.isNotBlank(queryString)){
			return queryString;
		}else{
			return hqlWhereBefore+hqlWhereAfter;
		}
		
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}


	public String getHqlWhereBefore() {
		return hqlWhereBefore;
	}
	public void setHqlWhereBefore(String hqlWhereBefore) {
		this.hqlWhereBefore = hqlWhereBefore;
	}
	public String getHqlWhereAfter() {
		return hqlWhereAfter;
	}
	public void setHqlWhereAfter(String hqlWhereAfter) {
		this.hqlWhereAfter = hqlWhereAfter;
	}
	
	public Map<String, Object> getNameKeyMap() {
		return nameKeyMap;
	}


	public void setNameKeyMap(Map<String, Object> map) {
		this.nameKeyMap = map;
	}


	public List<Object> getValues() {
		return values;
	}


	public void setValues(List<Object> values) {
		this.values = values;
	}


	public List getTypes() {
		return types;
	}


	public void setTypes(List types) {
		this.types = types;
	}


	public HqlConfig getHqlConfig() {
		return hqlConfig;
	}


	public void setHqlConfig(HqlConfig hqlConfig) {
		this.hqlConfig = hqlConfig;
	}

}
