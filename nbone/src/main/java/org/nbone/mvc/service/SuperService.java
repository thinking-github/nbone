package org.nbone.mvc.service;

import java.io.Serializable;

/**
 * Supper Service interface
 * @author thinking
 * @since 2015年12月12日下午1:45:26
 *
 * @param <T>
 */
public interface SuperService<T> {
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public int save(T object);
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public int update(T object);
	
	/**
	 * 有选择的更新，只更新含有值得属性 Selective
	 * @param object
	 * @return
	 */
	public int updateNotNull(T object);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public int delete(Serializable id);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public T get(Serializable id);

}
