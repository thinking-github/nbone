package org.nbone.persistence;

import java.util.List;
/**
 * 
 * @author thinking
 * @version 1.0 
 */
public interface QueryOperations {
	
	
	/**
	 * 获取数据(数据量大时不建议使用)
	 * @return
	 */
	public <T> List<T> getAll(Class<T> clazz);
	
	/**
	 * 根据 entity bean含有参数的属性组装查询条件 hibernate get method
	 * @param clazz
	 * @return
	 */
	public List<?> getForList(Object object);
	
	/**
	 * 按照实体中的参数查询实体列表（特殊情况下不同得实现方式）
	 * @param object
	 * @return  {@link List}
	 */
	public List<?> queryForList(Object object);
	/**
	 * 按照实体中的参数查询实体列表（特殊情况下不同得实现方式）
	 * @param object
	 * @return
	 */
	public List<?> findForList(Object object);

}
