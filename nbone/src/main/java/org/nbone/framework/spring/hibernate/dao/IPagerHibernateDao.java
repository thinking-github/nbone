package org.nbone.framework.spring.hibernate.dao;

import java.sql.SQLException;
import java.util.Map;

import org.nbone.mx.datacontrols.datapage.PagerModel;

/**
 * 分页IPagerHibernateDao 封装
 * @author thinking 2014-08-04
 *
 */
public interface IPagerHibernateDao {
	
    
	/**
	 * 分页函数
	 * @param hql     HQL字符串
	 * @param datas   预处理占位填充数据(?)
	 * @param offset  当前页
	 * @param pageSize 页的大小
	 * @return
	 * @throws SQLException
	 */
	public  PagerModel<Object> findByPage(String hql, Object[] datas,int offset, int pageSize);
	
	public  PagerModel<Object> findByPage(String hql,Map<String,Object> namesMap,int offset, int pageSize);
	
	public  PagerModel<Object> findByPage(String hql,String[] paramNames,Object[] values,int offset, int pageSize);
	

}
