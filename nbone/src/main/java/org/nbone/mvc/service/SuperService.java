/*
 * https://github.com/thinking-github/nbone
 */
package org.nbone.mvc.service;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Supper Service interface
 * @author thinking
 * @since 2015年12月12日下午1:45:26
 *
 * @param <T>
 * @param <IdType>
 */
public interface SuperService<T,IdType extends Serializable> {
	
	static Log logger = LogFactory.getLog(SuperService.class);
	/**
	 * 
	 * @param object
	 * @return 返回主键id
	 */
	public IdType save(T object);
	
	/**
	 * 
	 * <p></p>
	 * @param object
	 * @return 返回含有Id实体Bean
	 */
	public T add(T object);
	
	/**
	 * 
	 * <p></p>
	 * @param object
	 * @return 返回插入的行数
	 */
	public int insert(T object);
	
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
	public int delete(IdType id);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public T get(IdType id);

}
