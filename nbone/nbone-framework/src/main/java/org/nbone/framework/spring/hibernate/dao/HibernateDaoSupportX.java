package org.nbone.framework.spring.hibernate.dao;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

//@Repository
public class HibernateDaoSupportX extends HibernateDaoSupport {
	
	private HibernateTemplate newHibernateTemplate;

	public HibernateTemplate getNewHibernateTemplate() {
		return newHibernateTemplate;
	}
	public void setNewHibernateTemplate(HibernateTemplate newHibernateTemplate) {
		this.newHibernateTemplate = newHibernateTemplate;
	}
	
	
	//***********************super**********************************
	
    /**
     * set SessionFactory
     * @see JdbcDaoSupport#setDataSource(DataSource)
     * @param sessionFactory
     */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

}
