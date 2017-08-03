package org.nbone.framework.spring.hibernate.dao;

import java.util.Map;

import org.nbone.mx.datacontrols.datapage.PagerModel;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

@Repository
public class PagerHibernateDao extends HibernateDaoSupportX implements IPagerHibernateDao {

	@Override
	public PagerModel<Object> findByPage(String hql, Object[] datas, int offset,int pageSize) {
		return findByPage(hql, (Object)datas, offset, pageSize);
	}

	@Override
	public PagerModel<Object> findByPage(String hql, Map<String, Object> namesMap,int offset, int pageSize) {
		return findByPage(hql, (Object)namesMap, offset, pageSize);
	}

	@Override
	public PagerModel<Object> findByPage(String hql, String[] paramNames,Object[] values, int offset, int pageSize) {
		
		if (paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		int offset1 = offset != 0 ? offset : 1;
		HibernateCallback<PagerModel<Object>> pagecall = new PageHibernateCallback(hql, paramNames, values, offset1, pageSize);
		
		PagerModel<Object> pagem = super.getHibernateTemplate().executeWithNativeSession(pagecall);
		
		return pagem;
	}
	
	protected PagerModel<Object> findByPage(String hql, Object object, int offset,int pageSize){
		
		int offset1 = offset != 0 ? offset : 1;
		HibernateCallback<PagerModel<Object>> pagecall = new PageHibernateCallback(hql, object,offset1, pageSize);
		PagerModel<Object> pagem = super.getHibernateTemplate().executeWithNativeSession(pagecall);
		
		return pagem;
	}
	


}
