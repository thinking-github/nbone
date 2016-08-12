package org.nbone.persistence;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
/**
 * 查询基础接口
 * @author thinking
 * @version 1.0 
 * @since 2014-08-08
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
	 * 根据 entity bean含有参数的属性组装查询条件 hibernate get method(参数全部使用 等号 =)
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
	 * 按照实体中的参数查询实体列表并分页  (参数全部使用 等号 =)
	 * @param object
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * {@link #getForList(Object)}
	 */
	public  <T> Page<T> getForPage(Object object,int pageNum, int pageSize);
	
	/**
	 * 按照实体中的参数查询实体列表并分页（特殊情况下不同得实现方式 number use = /String use like）
	 * @param object
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * {@link #queryForList(Object)}
	 */
	public  <T> Page<T> queryForPage(Object object,int pageNum, int pageSize);
	
	/**
	 * 按照实体中的参数查询实体列表并分页
	 * @param object
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * {@link #findForList(Object)}
	 */
	public  <T> Page<T> findForPage(Object object,int pageNum, int pageSize);
	
	
	
	/**
	 * 根据实体参数查询返回单个字段的列表(比返回整个实体数据提高效率)
	 * @param object
	 * @param fieldName 要返回的单个字段名称
	 * @return
	 */
	public  <T> List<T> getForList(Object object,String fieldName);
	
	/**
	 * 根据实体参数查询返回单个字段的列表(比返回整个实体数据提高效率)
	 * @param object
	 * @param fieldName 要返回的单个字段名称
	 * @param requiredType 单个字段的目标类型
	 * @return
	 */
	public  <T> List<T> getForList(Object object,String fieldName,Class<T> requiredType);
	
	/**
	 * 根据实体参数查询返回含有数组中的字段的列表(比返回整个实体数据提高效率)
	 * @param object
	 * @param fieldNames java字段名称
	 * @param dbFieldMode 是否启用数据库字段名称
	 * @return
	 */
	public  <T> List<T> getForListWithFieldNames(Object object,String[] fieldNames,boolean dbFieldMode);
	
	

}
