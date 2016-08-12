package org.nbone.persistence;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.lang.MathOperation;
/**
 * 以实体Bean的方式实现单个对象的 增/删/改/查
 * @author thinking
 * @since 2014-12-12
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
	 * 单表操作删除记录, 根据实体不为null参数删除
	 * @param object
	 * @return
	 */
	public int deleteByEntityParams(Object object);
	
	/**
	 * 单表操作删除一条记录
	 * @param clazz
	 * @param id
	 * @return
	 */
	public int delete(Class<?> clazz, Serializable id);
	/**
	 * 根据主键列表ids删除
	 * @param clazz
	 * @param ids
	 * @return
	 */
	public <T> int delete(Class<T> clazz, Object[] ids);
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
	/**
	 * 统计单表的数据总行数
	 * @param clazz
	 * @return
	 */
	public long count(Class<?> clazz);
	/**
	 * 根据实体的参数统计单表的行数
	 * @param object
	 * @return
	 */
	public long count(Object object);
	/**
	 * 对数据的字段进行数学运算 (加减乘除...)
	 * @param object 根据实体不为null参数计算且是数字类型
	 * @param mathOperation
	 */
	public int updateMathOperation(Object object,MathOperation mathOperation);
	
	
	
	
	
}
