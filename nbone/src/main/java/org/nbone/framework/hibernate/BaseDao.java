package org.nbone.framework.hibernate;

import java.io.Serializable;
import java.util.List;


public interface BaseDao<T> {
	
	void save(T entity);
	
	void saveOrUpdate(T entity);
	
    void update(final T entity);
    
	void delete(T entity);
	
	void delete(Serializable id);
	
	T get(Serializable id);
	
	List<T> getAll();
}
