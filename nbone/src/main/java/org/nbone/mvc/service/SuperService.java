/*
 * https://github.com/thinking-github/nbone
 */
package org.nbone.mvc.service;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.mvc.ISuper;

/**
 * Supper Service interface
 * @author thinking
 * @since 2015年12月12日下午1:45:26
 *
 * @param <T>
 * @param <IdType>
 */
public interface SuperService<T,IdType extends Serializable> extends ISuper<T, IdType> {
	
	static Log logger = LogFactory.getLog(SuperService.class);
	
	/**
	 * 有选择的更新，只更新含有值得属性 Selective
	 * @param object
	 * @return
	 */
	public int updateNotNull(T object);
	
	
	

}
