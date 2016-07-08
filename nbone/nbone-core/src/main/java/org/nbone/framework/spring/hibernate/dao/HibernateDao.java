package org.nbone.framework.spring.hibernate.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.nbone.framework.hibernate.util.HibernateClassUtils;
import org.nbone.framework.hibernate.util.HqlQuery;
import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.SqlPropertyDescriptor;
import org.nbone.persistence.SqlPropertyDescriptors;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateDao extends HibernateDaoSupportX {
	
	//***********************super**********************************
	
	public Session getCurrentSession(){
		return  super.getSessionFactory().getCurrentSession();
	}
	public Session openSession(){
		return  super.getSessionFactory().openSession();
	}
	
	public List<?> queryForBean(Object bean) {
		String hql = HibernateClassUtils.getSimpleHQL(bean);
		return super.getHibernateTemplate().find(hql);
	}
	
	public List<Object> queryForBean(Object bean,SqlPropertyDescriptors sqlpds) {
		
		SqlConfig hqlConfig;
		if (sqlpds == null) {
			hqlConfig = new SqlConfig();
		}
		else {
			hqlConfig = SqlConfig.getHighMode();
			hqlConfig.setSqlPds(sqlpds);
		}
		return queryForBean(bean, hqlConfig);
	}
	
	public List<Object> queryForBean(Object bean,SqlConfig hqlConfig) {
 		HqlQuery hqlQuery = HibernateClassUtils.getHql(bean, hqlConfig);
 		String queryString = hqlQuery.getQueryString();
 		Map<String,Object> nameKeyMap = hqlQuery.getNameKeyMap();
		Session session = openSession();
		Query query = session.createQuery(queryString);
		
		if (nameKeyMap != null && nameKeyMap.size() > 0) {
			query.setProperties(nameKeyMap);
		}
		List<Object> resultList = query.list();
		SessionFactoryUtils.closeSession(session);
		return resultList;
	}
	
 

}
