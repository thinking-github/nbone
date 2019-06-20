package org.nbone.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
/**
 * 
 * 以实体Bean的方式实现对象的批量的  增/删/改/查
 * @author thinking
 * @since 2015-12-12
 */
public interface BatchSqlSession extends SqlSession {
	
	/**
	 * 批量添加
	 * @param objects
	 * @return
	 */
	public int[] batchInsert(Object[] objects,boolean jdbcBatch);
	
	/**
	 * 批量添加
	 * @param objects
	 * @return
	 */
	public int[] batchInsert(Collection<?> objects,boolean jdbcBatch);
	
	/**
	 * 批量更新
	 * @param objects
	 * @param  propertys 更新的属性字段 可为空
	 * @return
	 */
	public int[] batchUpdate(Object[] objects,String...propertys);
	
	/**
	 * 批量更新
	 * @param objects
	 * @param  propertys 更新的属性字段 可为空
	 * @return
	 */
	public int[] batchUpdate(Collection<?> objects,String...propertys);
	/**
	 * 批量删除(不用in语句实现)
	 * @param clazz
	 * @param  ids
	 * @return
	 */
	public <T> int[] batchDelete(Class<T> clazz,Serializable[] ids);
	
	/**
	 * 批量删除(不用in语句实现)
	 * @param clazz
	 * @return
	 */
	public <T> int[] batchDelete(Class<T> clazz,List<Serializable> ids);
	

}
