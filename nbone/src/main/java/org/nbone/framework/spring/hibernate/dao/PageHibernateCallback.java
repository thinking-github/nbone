package org.nbone.framework.spring.hibernate.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.nbone.framework.hibernate.util.HQueryUtils;
import org.nbone.mx.datacontrols.datapage.PagerModel;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * query page  HibernateCallback
 * @author thinking 2016-01-18
 *
 */
public class PageHibernateCallback implements HibernateCallback<PagerModel<Object>> {

	private String queryString;
	
	private int offset;
	private int pageSize;
	
	private String[] paramNames;
	private Object[] values;
	
	// index (?) and Map (:name) 
	private Object object;
	
	private int queryType = QUERY_PARAMETER_NOT;

	
	public final static int QUERY_PARAMETER_NOT = 0;
	public final static int QUERY_PARAMETER_INDEX = 1;
	public final static int QUERY_PARAMETER_MAP = 2;
	public final static int QUERY_PARAMETER_NAMED = 3;
	
	
	
	public PageHibernateCallback(String queryString, int offset, int pageSize) {
		this.queryString = queryString;
		this.offset = offset;
		this.pageSize = pageSize;
	}

	public PageHibernateCallback(String queryString,String[] paramNames, Object[] values, int offset, int pageSize) {
		this.queryString = queryString;
		this.offset = offset;
		this.pageSize = pageSize;
		this.paramNames = paramNames;
		this.values = values;
		this.queryType = QUERY_PARAMETER_NAMED;
	}

	public PageHibernateCallback(String queryString,Object object, int offset, int pageSize) {
		this.queryString = queryString;
		this.offset = offset;
		this.pageSize = pageSize;
		this.object = object;
		if(object instanceof Object[]) {
			this.queryType = QUERY_PARAMETER_INDEX;
		}
		else if (object instanceof Map){
			this.queryType = QUERY_PARAMETER_MAP;
		}
	}




	@Override
	public PagerModel<Object> doInHibernate(Session session)throws HibernateException, SQLException {
		PagerModel<Object> pm = new PagerModel<Object>();
		
		pageSize = pageSize > 0  ? pageSize : pm.getPageSize();
		pm.setPageSize(pageSize);
		
		//total number 
		String hqlcount = HQueryUtils.getCountQueryString(queryString);
		Query queryCountO  = session.createQuery(hqlcount);
		if(queryType == QUERY_PARAMETER_NAMED){
			
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					HQueryUtils.applyNamedParameterToQuery(queryCountO, paramNames[i], values[i]);
				}
			}
			
		}
		else if(queryType == QUERY_PARAMETER_INDEX) {
			Object[] values = null;
			if(object instanceof Object[]){
				values = (Object[]) object;
			}
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					queryCountO.setParameter(i, values[i]);
				}
			}
			
		}
		else if(queryType == QUERY_PARAMETER_MAP) {
			Map<String,Object> values = null;
			if(object instanceof Map){
				values = (Map) object;
			}
			queryCountO.setProperties(values);
					
		}
		else{}
	
		Long total = (Long) queryCountO.uniqueResult();
		pm.setTotal(total.intValue());
		
		//rows List
		Query queryObject = session.createQuery(queryString);
		
		if(queryType == QUERY_PARAMETER_NAMED){
			
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					HQueryUtils.applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
				}
			}
			
		}
		else if(queryType == QUERY_PARAMETER_INDEX) {
			Object[] values = null;
			if(object instanceof Object[]){
				values = (Object[]) object;
			}
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					queryObject.setParameter(i, values[i]);
				}
			}
			
		}
		else if(queryType == QUERY_PARAMETER_MAP) {
			Map<String,Object> values = null;
			if(object instanceof Map){
				values = (Map) object;
			}
			queryObject.setProperties(values);
					
		}
		else{}
		
		
		queryObject.setFirstResult((offset-1) * pageSize);
		queryObject.setMaxResults(pageSize);
		
		List resultList = queryObject.list();
		pm.setRows(resultList);
		return pm;
	}
	
	
	

    

}
