package org.nbone.persistence;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
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
	 * Returns all instances of the type with the given IDs.
	 * @param clazz
	 * @param ids
	 * @return
	 */
	public <T> List<T> getAll(Class<T> clazz,Collection<?> ids);
	/**
	 * Returns all instances of the type with the given IDs.
	 * @param clazz
	 * @param ids
	 * @return
	 */
	public <T> List<T> getAll(Class<T> clazz,Object[] ids);
	
	/**
	 * 根据 entity bean含有参数的属性组装查询条件 hibernate get method(全部使用 等号 =)
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getForList(Object object);
	
	/**
	 * 按照实体中的参数查询实体列表（特殊情况下不同得实现方式 number use = /String use like）
	 * @param object
	 * @return  {@link List}
	 */
	public  <T> List<T> queryForList(Object object);
	/**
	 * 按照实体中的参数查询实体列表(支持字段查询符号操作 =  > < >= <=  is null is not null)
	 * @param object
	 * @param sqlConfig
	 * @return
	 */
	public  <T> List<T> queryForList(Object object,SqlConfig sqlConfig);
	
	/**
	 * 按照实体中的参数查询实体列表（特殊情况下不同得实现方式）
	 * @param object
	 * @return
	 */
	public  <T> List<T> findForList(Object object);
	
	/**
	 * 按照实体中的参数查询实体列表并分页
	 * @param object
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public  <T> Page<T> findForPage(Object object,int pageNum, int pageSize);
	

}
