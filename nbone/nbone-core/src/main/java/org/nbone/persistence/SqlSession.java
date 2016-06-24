package org.nbone.persistence;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 以实体Bean的方式实现单个对象的 增/删/改/查
 * @author thinking
 * @since 2015-12-12
 *
 */
public interface SqlSession  extends QueryOperations{
	
	Log logger = LogFactory.getLog(SqlSession.class);
	/**
	 * 单表操作增加一条记录
	 * @param object
	 * @return 返回插入的行数
	 */
	public int insert(Object object);
	
	/**
	 * 保存数据
	 * @param object
	 * @return 返回主键id
	 */
	public Serializable save(Object object);
	
	/**
	 *  单表操作增加一条记录
	 * <p></p>
	 * @param object
	 * @return 返回含有Id实体Bean
	 */
	public Object add(Object object);
	
	
	
	/**
	 * 以主键为条件修改一条记录
	 * @param object
	 * @return
	 */
	public int update(Object object);
	
	/**
	 * 根据主键更新一条记录(有选择的更新,为空的数据丢弃)
	 * @param object
	 * @return
	 */
	public int updateSelective(Object object);
	
	/**
	 *  保存或者更新数据
	 * @param object
	 */
	public void saveOrUpdate(Object object);
	
	
	
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
	 * 根据主键获取一条记录   <br>
	 * @param object
	 * @return
	 */
	public <T> T get(Object object);
	
	
	
	
}
