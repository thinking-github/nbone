package org.nbone.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.lang.MathOperation;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 以实体Bean的方式实现单个对象的 增/删/改/查
 * @author thinking
 * @since 2014-12-12
 *
 */
public interface SqlSession extends QueryOperations{
	
	Log logger = LogFactory.getLog(SqlSession.class);
	/**
	 * 单表操作增加一条记录
	 * @param object
	 * @return 返回插入的行数
	 */
	public int insert(Object object);
	/**
	 * 单表操作插入一条数据(数据来源于map)当map 主键值为空时自动生成主键值
	 * @param entityClass
	 * @param fieldMap
	 * @return
	 */
	public int insert(Class<?> entityClass, Map<String, Object> fieldMap);

	
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
	 * 以主键为条件修改一条记录 (数据来源于map)
	 * @param entityClass
	 * @param fieldMap
	 * @return
	 */
    public int update(Class<?> entityClass, Map<String, Object> fieldMap);
   
	
	/**
	 * 根据主键更新一条记录(有选择的更新,为空的数据丢弃)
	 * @param object 更新实体参数
	 * @return
	 */
	public int updateSelective(Object object);
	
	 /**
     * 有选择的更新实体
     * @param object 更新参数
     * @param whereEntity 更新条件
     * @return
     */
    public int updateSelective(Object object, Object whereEntity);
    
    /**
     * 有选择的更新实体
     * @param object 更新参数
     * @param whereMap 更新条件
     * @return
     */
    public int updateSelective(Object object, Map<String, Object> whereMap);


	/**
	 * 有选择的更新实体
	 * @param object 更新实体参数
	 * @param  properties 需要更新的属性字段 可为空
	 * @param whereString 更新条件 where sql 语句 例如 and name = chen  可为空，为空时默认使用主键作为条件
	 * @return
	 */
	public int updateSelective(Object object,String[] properties, String whereString);
	/**
	 * 有选择的更新实体
	 * @param object 更新实体参数
	 * @param  properties  需要更新的属性字段 可为空
	 * @param  conditionFields 更新条件字段列表   可为空
	 * @param whereString 更新条件 where sql 语句 例如 and name = chen  可为空，为空时默认使用主键作为条件
	 * @return
	 */
	public int updateSelective(Object object,String[] properties,String[] conditionFields, String whereString);
	
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
	public int deleteByEntity(Object object);

	/**
	 * 根据 SqlConfig 配置删除数据
	 *
	 * @param sqlConfig 参数必填
	 * @return
	 */
	public int delete(SqlConfig sqlConfig);
	
	/**
	 * 单表操作删除一条记录
	 * @param clazz
	 * @param id
	 * @param  tableName 支持分表， 可为空,为空时使用默认表名称
	 * @return
	 */
	public int delete(Class<?> clazz, Serializable id,String tableName);

	/**
	 * 根据主键列表ids删除
	 * @param clazz
	 * @param ids
	 * @param tableName 分表支持，可为空,为空时使用默认表名称
	 * @return
	 */
	public <T> int delete(Class<T> clazz, Object[] ids,String tableName);
	/**
	 * 根据主键获取一条记录   <br>
	 * 参照 hibernate get method
	 * @param clazz  实体类型
	 * @param id     主键id
	 * @param tableName 分表支持，可为空,为空时使用默认表名称
	 * @param <T>
	 * @return
	 */
	public <T> T get(Class<T> clazz, Serializable id, String tableName);
	/**
	 * 根据主键获取一条记录   <br>
	 * @param object
	 * @return
	 */
	public <T> T get(Object object);

	/**
	 * 根据对象实体参数查询条件获取一条记录   <br>
	 *
	 * <li> 1.当返回<=0条数据时返回null
	 * <li> 2.当返回=1条数据时直接返回
	 * <li> 3.当返回>1数据时只取第一条
	 *
	 * @param object
	 * @param sqlConfig  可为空
	 * @return
	 */
	public <T> T getOne(Object object,SqlConfig sqlConfig);

	/**
	 * 获取全部数据(数据量大时不建议使用)
	 * @param clazz 映射实体类型
	 * @param  tableName 支持分表、动态表,可为空，为空时使用默认值
	 * @return
	 */
	public <T> List<T> getAll(Class<T> clazz, String tableName);

	/**
	 * Returns all instances of the type with the given IDs.
	 * @param clazz 映射实体类型
	 * @param ids   查询主键列表
	 * @param  tableName 支持分表、动态表,可为空，为空时使用默认值
	 * @return
	 */
	public <T> List<T> getAll(Class<T> clazz, Collection<?> ids, String tableName);
	/**
	 * Returns all instances of the type with the given IDs.
	 * @param clazz 映射实体类型
	 * @param ids   查询主键列表
	 * @param  tableName 支持分表、动态表,可为空，为空时使用默认值
	 * @return
	 */
	public <T> List<T> getAll(Class<T> clazz,Object[] ids,String tableName);

	/**
	 * 统计单表的数据总行数
	 * @param clazz
	 * @param afterWhere 增加条件语句 如: and id in(1,2,3) 可为空
	 * @return
	 */
	public long count(Class<?> clazz,String afterWhere);
	/**
	 * 根据实体的参数统计单表的行数
	 * @param object
	 * @param sqlConfig 查询配置 可为空
	 *                  <li> fieldNames  按需返回字段java字段名称【返回含有数组中的字段的】,比返回整个实体数据提高效率
	 *                  <li> groupQuery  分组查询 ,
	 *                  <li> fieldLevel  字段级别查询,
	 *                  <li> dbFieldMode 是否启用数据库字段名称模式,
	 *                  <li> afterWhere  追加条件语句 或者 group by /order by 子句 参数可为null 如： and id in(1,2,3,4) （可为空）
	 * @return
	 */
	public long count(Object object,SqlConfig sqlConfig);
	/**
	 * 对数据的字段进行数学运算 (加减乘除...)
	 * @param object 根据实体不为null参数计算且是数字类型
	 * @param  property 计算字段名称 可为空，为空时 参数值不为空且为数字的加入进行数学计算
	 * @param mathOperation
	 * @param conditionFields  条件字段 可为空,默认使用主键
	 * @param whereString 更新条件 where sql 语句 例如 and name = chen  可为空，为空时默认使用主键作为条件
	 */
	public int updateMathOperation(Object object,String property,MathOperation mathOperation,String[] conditionFields,String whereString);

	public int updateMathOperation(Object object,String property,MathOperation mathOperation,String[] conditionFields);

	
	
}
