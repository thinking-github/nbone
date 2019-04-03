package org.nbone.persistence;

import java.util.Collection;
import java.util.List;

import org.nbone.persistence.annotation.FieldLevel;
import org.springframework.data.domain.Page;
/**
 * 查询基础接口
 * @author thinking
 * @version 1.0 
 * @since 2014-08-08
 */
public interface QueryOperations {
	
	
	/**
	 * 获取全部数据(数据量大时不建议使用)
	 * @param clazz 映射实体类型
	 * @return
	 */
	public <T> List<T> getAll(Class<T> clazz);
	
	/**
	 * Returns all instances of the type with the given IDs.
	 * @param clazz 映射实体类型
	 * @param ids
	 * @return
	 */
	public <T> List<T> getAll(Class<T> clazz,Collection<?> ids);
	/**
	 * Returns all instances of the type with the given IDs.
	 * @param clazz 映射实体类型
	 * @param ids
	 * @return
	 */
	public <T> List<T> getAll(Class<T> clazz,Object[] ids);
	/**
	 * 根据 entity bean含有参数的属性组装查询条件 hibernate get method(参数全部使用 等号 =)
	 * @param object 查询实体参数
	 * @param afterWhere group by /order  by 子句 参数可为null
	 * @return
	 */
	public <T> List<T> getForList(Object object,String... afterWhere);
	/**
	 * 根据 entity bean含有参数的属性组装查询条件 hibernate get method(参数全部使用 等号 =)
	 * @param object 查询实体参数
	 * @param fieldLevel 查询字段级别
	 * @param afterWhere group by /order  by 子句 参数可为null
	 * @return
	 */
	public <T> List<T> getForList(Object object,FieldLevel fieldLevel,String... afterWhere);
	
	/**
	 * 按照实体中的参数查询实体列表（特殊情况下不同得实现方式 number use = /String use like）
	 * @param object 查询实体参数
	 * @param afterWhere group by /order  by 子句 参数可为null
	 * @return  {@link List}
	 */
	public  <T> List<T> queryForList(Object object,String... afterWhere);
	/**
	 * 按照实体中的参数查询实体列表（特殊情况下不同得实现方式 number use = /String use like）
	 * @param object 查询实体参数
	 * @param fieldLevel 查询字段级别
	 * @param afterWhere group by /order  by 子句 参数可为null
	 * @return  {@link List}
	 */
	public  <T> List<T> queryForList(Object object,FieldLevel fieldLevel,String... afterWhere);
	
	/**
	 * 按照实体中的参数查询实体列表(支持字段查询符号操作 =  > < >= <=  is null is not null)
	 * @param object 查询实体参数
	 * @param sqlConfig
	 * @return
	 */
	public  <T> List<T> queryForList(Object object,SqlConfig sqlConfig);
	
	/**
	 * 按照实体中的参数查询实体列表（特殊情况下不同得实现方式）
	 * @param object 查询实体参数
	 * @return
	 */
	public  <T> List<T> findForList(Object object);

	/**
	 * 按照实体中的参数查询实体列表（特殊情况下不同得实现方式）
	 * @param object 查询实体参数
	 * @param fieldLevel 查询字段级别
	 * @param <T>
	 * @return
	 */
	public  <T> List<T> findForList(Object object,FieldLevel fieldLevel);
	
	
	/**
	 * 按照实体中的参数查询实体列表并分页  (参数全部使用 等号 =)
	 * @param object  查询实体参数
	 * @param pageNum 当前页码
	 * @param pageSize 页的大小
	 * @param afterWhere  group by/order by 子句
	 * @return
	 *
	 */
	public  <T> Page<T> getForPage(Object object,int pageNum, int pageSize,String... afterWhere);
	
	/**
	 * 按照实体中的参数查询实体列表并分页（特殊情况下不同得实现方式 number use = /String use like）
	 * @param object  查询实体参数
	 * @param pageNum 当前页码
	 * @param pageSize 页的大小
	 * @param afterWhere  group by/order by 子句
	 * @return
	 *
	 */
	public  <T> Page<T> queryForPage(Object object,int pageNum, int pageSize,String... afterWhere);

	/**
	 * 按照实体中的参数查询实体列表并分页（可以自定义操作符号类型 = > <  between  in ）
	 * @param object  查询实体参数
	 * @param pageNum 当前页码
	 * @param pageSize 页的大小
	 * @param sqlConfig 查询配置 可以自定义操作符号类型 = > <  between  in
	 * @param <T>
	 * @return
	 */
	public  <T> Page<T> queryForPage(Object object,int pageNum, int pageSize,SqlConfig sqlConfig);
	
	/**
	 * 按照实体中的参数查询实体列表并分页
	 * @param object 查询实体参数
	 * @param pageNum 当前页码
	 * @param pageSize 页的大小
	 * @param afterWhere  group by/order by 子句
	 * @return
	 * {@link #findForList(Object)}
	 */
	public  <T> Page<T> findForPage(Object object,int pageNum, int pageSize,String... afterWhere);

	/**
	 * 按照实体中的参数查询实体列表并限制返回数量  (参数全部使用 等号 =)
	 * @param object 查询实体参数
	 * @param limit 限制返回的大小
	 * @param afterWhere  group by/order by 子句
	 * @return
	 */
	public  <T> List<T> getForLimit(Object object, int limit,String... afterWhere);

	/**
	 * 按照实体中的参数查询实体列表并限制返回数量（特殊情况下不同得实现方式 number use = /String use like）
	 * @param object 查询实体参数
	 * @param limit 限制返回的大小
	 * @param afterWhere  group by/order by 子句
	 * @return
	 */
	public  <T> List<T> queryForLimit(Object object, int limit,String... afterWhere);


	/**
	 * 根据实体参数查询返回单个字段的列表(比返回整个实体数据提高效率)
	 * @param object 查询实体参数
	 * @param fieldName 要返回的单个字段名称 默认采用java property mapping
	 * @param requiredType 单个字段的目标类型
	 * @return
	 */
	public  <T> List<T> getForList(Object object,String fieldName,Class<T> requiredType);
	
	/**
	 * 根据实体参数查询返回含有数组中的字段的列表(比返回整个实体数据提高效率)
	 * @param object 查询实体参数
	 * @param fieldNames java字段名称
	 * @param dbFieldMode 是否启用数据库字段名称
	 * @return
	 */
	public  <T> List<T> getForListWithFieldNames(Object object,String[] fieldNames,boolean dbFieldMode);
	
	

}
