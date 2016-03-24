package org.nbone.persistence;

import java.io.Serializable;
import java.util.List;
/**
 * 以实体Bean的方式实现单个对象的 增/删/改/查
 * @author thinking
 * @since 2015-12-12
 *
 */
public interface SqlSession {
	
	/**
	 * 单表操作增加一条记录
	 * @param object
	 * @return
	 */
	public int insert(Object object);
	
	/**
	 * 以主键为条件修改一条记录
	 * @param object
	 * @return
	 */
	public int update(Object object);
	
	/**
	 * 单表操作删除一条记录, id值一定要含有值,否则不做删除
	 * @param object
	 * @return
	 */
	public int delete(Object object);
	
	/**
	 * 单表操作删除一条记录
	 * @param clazz
	 * @param id
	 * @return
	 */
	public int delete(Class<?> clazz, Serializable id);
	/**
	 * 根据主键获取一条记录   <br>
	 * 参照 hibernate get method
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T> T get(Class<T> clazz, Serializable id);
	/**
	 * 根据 entity bean含有参数的属性组装查询条件 hibernate get method
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getByBeanParams(Class<T> clazz);
	
	
}
