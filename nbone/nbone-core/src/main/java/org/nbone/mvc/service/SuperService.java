/*
 * https://github.com/thinking-github/nbone
 */
package org.nbone.mvc.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.nbone.mvc.ISuper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Supper Service interface
 * @author thinking
 * @since 2015年12月12日下午1:45:26
 *
 * @param <T>
 * @param <IdType>
 */
public interface SuperService<T,IdType extends Serializable> extends ISuper<T, IdType> {
	
	static Logger logger = LoggerFactory.getLogger(SuperService.class);
	
	
	
	public void batchInsert(T[] objects);
	
	public void batchInsert(Collection<T> objects);
	
	
	public void batchUpdate(T[] objects);
	
	public void batchUpdate(Collection<T> objects);
	
	
	public void batchDelete(Class<T> clazz,Serializable[] ids);
	
	public void batchDelete(Class<T> clazz,List<Serializable> ids);


}
