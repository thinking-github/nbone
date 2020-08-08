package org.nbone.mvc;



import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 超级通用接口(供BaseDao和BaseService继承使用)
 * @author thinking entity
 * @version 1.0
 * @since   2016年4月4日
 *
 * @param <T>
 * @param <Id>
 */
@SuppressWarnings("unused")
public interface ISuper<T,Id extends Serializable> {
	
	/**
	 * 保存数据
	 * @param object
	 * @return 返回主键id
	 */
	public Id save(T object);
	
	/**
	 *  保存数据
	 * <p></p>
	 * @param object
	 * @return 返回含有Id实体Bean
	 */
	public T add(T object);
	/**
	 *  保存数据
	 * <p></p>
	 * @param object
	 * @return 返回插入的行数
	 */
	public int insert(T object);
	
	
	/**
	 *  保存或者更新数据
	 * @param object
	 */
	public void saveOrUpdate(T object);
	/**
	 *  按id更新数据
	 * @param object
	 * @return
	 */
	public void update(T object);
	/**
	 * 根据字段名称的更新单列字段
	 *
	 * @param object 需要更新的实体参数和条件
	 * @param name   需要更新的属性字段
	 * @param conditionFields 更新条件字段列表   可为空
	 * @param whereString 更新条件 where sql 语句 例如 and name = chen  可为空，当conditionFields和whereString 为空时默认使用主键作为条件
	 * @return
	 */
	public void updateField(T object,String name,String[] conditionFields, String whereString);
	/**
	 * 根据主键更新一条记录(有选择的更新,为空的数据丢弃)
	 * @param object
	 */
	public void updateSelective(T object);

	/**
	 * 根据 whereql 更新一条记录(有选择的更新,为空的数据丢弃)
	 * @param object
	 * @param  whereSql id = 1 and name = 'chen'
	 */
	public void updateSelective(T object ,String whereSql);
	/**
	 * 根据 wheresql 更新一条记录(有选择的更新,为空的数据丢弃)
	 * @param object
	 * @param  properties 需要更新的属性字段 可为空
	 * @param  whereSql id = 1 and name = 'chen'
	 */
	public void updateSelective(T object ,String[] properties,String whereSql);

	/**
	 * 有选择的更新实体
	 * @param object 更新实体参数
	 * @param properties  需要更新的属性字段 可为空
	 * @param conditionFields 更新条件字段列表   可为空
	 * @param whereString 更新条件 where sql 语句 例如 and name = chen  可为空，为空时默认使用主键作为条件
	 * @return
	 */
	public int updateSelective(Object object,String[] properties,String[] conditionFields, String whereString);
    
	/**
	 * 按实体中的id删除数据
	 * @param object
	 */
	//public void delete(T object);
	public int delete(T object);
	/**
	 * 按实体中不为空属性参数删除数据
	 * @param object
	 */
	public void deleteByEntity(T object);

	/**
	 * 按id删除数据
	 *
	 * @param id
	 * @param tableName 支持分表和动态表名称 可为空，为空时使用默认
	 */
	public void delete(Id id, String tableName);
	
	public void delete(Id[] ids,String tableName);
	
	public void delete(Collection<?> ids,String tableName);
	
	/**
	 * 按id获取数据
	 * @param id
	 * @param tableName 支持分表和动态表名称 可为空，为空时使用默认
	 * @return
	 */
	public T get(Id id,String tableName);
	/**
	 * 统计全部数据的行数
	 * @return
	 */
	public long count();

	/**
	 * 统计数据的行数
	 * @param afterWhere 增加条件语句 如: and id in(1,2,3) 可为空
	 * @return
	 */
	public long count(String afterWhere);
	/**
	 * 按照实体中不为空的参数作为参数统计行数
	 * @param object 参数实体
	 * @param afterWhere 增加条件语句 如: and id in(1,2,3)
	 * @return
	 */
	public long count(T object,String afterWhere);
	
	/**
	 * 获取数据(数据量大时不建议使用)
	 * @return
	 */
	public List<T> getAll(String tableName);
	
	public List<T> getAll(Id[] ids,String tableName);
	
	public List<T> getAll(Collection<?> ids,String tableName);
	
	/**
	 * 按照实体中的参数查询实体列表
	 * @param object
	 * @return {@link List}
	 */
	public List<T> getForList(T object);
	
	/**
	 * getForList(index,"and status != -1"," order by create_time DESC"); <br>
	 *
	 * getForList(index," order by  create_time DESC"); <br>
	 * 按照实体中的参数查询实体列表
	 * @param object
	 * @param afterWhere group by/order by 子句
	 * @return {@link List}
	 */
	public List<T> getForList(T object,String afterWhere);
	
	/**
	 * 按照实体中的参数查询实体列表（特殊情况下不同得实现方式）
	 * @param object
	 * @return  {@link List}
	 */
	public List<T> queryForList(T object);


	/**
	 * queryForList(index,"and status != -1"," order by  create_time DESC"); <br>
	 *
	 * queryForList(index," order by  create_time DESC"); <br>
	 *
	 * 按照实体中的参数查询实体列表（特殊情况下不同得实现方式）
	 * @param object
	 * @param afterWhere afterWhere group by/order by 子句
	 * @return {@link List}
	 */
	public List<T> queryForList(T object,String afterWhere);

	/**
	 *  execute DDL
	 * @param sql
	 * @return
	 */
	public boolean execute(String sql);

	
	
	
	

}
