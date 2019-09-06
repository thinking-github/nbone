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
	 * 批量添加
	 * @param objects 实体对象
	 * @param insertProperties 可选的插入字段（可为空）
	 * @param jdbcBatch true jdbc batch, false db batch
	 *
	 * @return
	 */
	public int[] batchInsert(Object[] objects,String[] insertProperties,boolean jdbcBatch);
	/**
	 * 批量添加
	 * @param objects 实体对象
	 * @param insertProperties 可选的插入字段（可为空）
	 * @param jdbcBatch true jdbc batch, false db batch
	 *
	 * @return
	 */
	public int[] batchInsert(Collection<?> objects,String[] insertProperties,boolean jdbcBatch);
	
	/**
	 * 批量更新
	 * @param objects
	 * @param  properties 要更新的属性字段列表 可为空 ，当为空更新全部字段
	 * @return
	 */
	public int[] batchUpdate(Object[] objects,String...properties);
	/**
	 * 批量更新
	 * @param objects
	 * @param  properties 要更新的属性字段列表 可为空 ，当为空更新全部字段
	 * @return
	 */
	public int[] batchUpdate(Collection<?> objects,String...properties);

	/**
	 * 批量更新
	 * @param objects
	 * @param  properties 要更新的属性字段列表 可为空 ，当为空更新全部字段
	 * @param  conditionProperties  条件属性  可为空， 当为空时使用主键作为条件
	 * @return
	 */
	public int[] batchUpdate(Object[] objects,String[] properties,String...conditionProperties);
	/**
	 * 批量更新
	 * @param objects
	 * @param  properties 要更新的属性字段列表 可为空 ，当为空更新全部字段
	 * @param  conditionProperties  条件属性  可为空， 当为空时使用主键作为条件
	 * @return
	 */
	public int[] batchUpdate(Collection<?> objects,String[] properties,String... conditionProperties);

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
