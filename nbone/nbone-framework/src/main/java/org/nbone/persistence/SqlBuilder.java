package org.nbone.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.nbone.lang.MathOperation;
import org.nbone.mvc.domain.GroupQuery;
import org.nbone.persistence.annotation.FieldLevel;
import org.nbone.persistence.exception.BuilderSQLException;
import org.nbone.persistence.mapper.TableMapper;
import org.nbone.persistence.model.SqlModel;
import org.springframework.jdbc.core.RowMapper;

/**
 * 构建ORM映射超级接口
 * @author thinking
 * @since 2015-12-12
 * 
 */
public interface SqlBuilder {
	
	/**
	 * 由传入的对象的参数生成insert sql语句(不校验空值)
	 * @param object
	 * @return {@link SqlModel}
	 * @throws BuilderSQLException
	 */
	 public SqlModel<Object> insertSql(Object object) throws BuilderSQLException;
	/**
	 * 由传入的对象的参数生成insert sql语句(参数值不为空的加入)
	 * @param object
	 * @return {@link SqlModel}
	 * @throws BuilderSQLException
	 */
	 public SqlModel<Object> insertSelectiveSql(Object object) throws BuilderSQLException;



	 /**
	  * 由传入的对象生成update sql语句
	  *
	  * @param object  实体对象
	  * @param  propertys 更新的属性字段 可为空，为空时选择全部字段
	  * @param propertysCondition  属性条件 可为空， 为空时默认使用主键作为条件
	  * @return
	  * @throws BuilderSQLException
	  */
	 public SqlModel<Object> updateSql(Object object,String[] propertys,String[] propertysCondition) throws BuilderSQLException;

	/**
	 * 由传入的对象生成update sql语句(参数值不为空的加入)(启用安全属性设置,即为空的属性值不进行更新)
	 * @param object 实体对象
	 * @param propertysCondition  属性条件 可为空， 为空时默认使用主键作为条件
	 * @return
	 * @throws BuilderSQLException
	 */
	public SqlModel<Object> updateSelectiveSql(Object object,String[] propertysCondition) throws BuilderSQLException;

	/**
	 * 由传入的对象生成update sql语句
	 * @param  propertys 更新的属性字段 可为空
	 * @param object
	 * @param isSelective 是否只更新不为null的值
	 * @param propertysCondition  属性条件 可为空， 为空时默认使用主键作为条件
	 * @param whereSql    where 部分sql id=1 and name ='chen',  可为null
	 * @return
	 * @throws BuilderSQLException
	 */
	public SqlModel<Object> updateSql(String[] propertys,Object object,boolean isSelective,String[] propertysCondition,String whereSql) throws BuilderSQLException;
	 
	 /**
	  * 由传入的Map对象生成update sql语句
	  * @param entityClass 
	  * @param fieldsMap
	  * @param isDbFieldName map的key是否采用数据库的字段名称(默认采用数据库的字段名称效率高不用转化)
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<Map<String,?>> updateSql(Class<T> entityClass,Map<String,?> fieldsMap,boolean isDbFieldName) throws BuilderSQLException;
	 /**
	  * 由传入的对象生成update sql语句 <br>
	  *     (首选根据对propertys字段进行计算， 当propertys为空时，参数值不为空且为数字的加入进行数学计算)
	  * @param object
	  * @param  property 计算字段名称 可为空，为空时 参数值不为空且为数字的加入进行数学计算
	  * @param mathOperation 数学运算类型 {@link MathOperation}
	  * @return
	  * @throws BuilderSQLException
	  */
	 public SqlModel<Object> updateMathOperationSql(Object object,String property,MathOperation mathOperation) throws BuilderSQLException;



	 
	 /**
	  *  由传入的对象生成delete sql语句(包括不为null的属性生成sql)
	  * @param object
	  * @param onlypkParam true 只包括主键参数(根据对象主键生成),  <code>false</code> 包括不为null的属性参数
	  * @return
	  * @throws BuilderSQLException
	  */
	 public SqlModel<Object> deleteSqlByEntityParams(Object object,boolean onlypkParam) throws BuilderSQLException;
	 
	 /**
	  * 根据主键Id删除
	  * @param entityClass
	  * @param id
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<Map<String,?>> deleteSqlById(Class<T> entityClass,Serializable id) throws BuilderSQLException;
	 
	 /**
	  * 根据主键列表Id删除
	  * @param entityClass
	  * @param ids
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> deleteSqlByIds(Class<T> entityClass,Object[] ids) throws BuilderSQLException;
	
	 /**
	  * 根据主键Id查询
	  * @param id
	  * @param entityClass
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<Map<String,?>> selectSqlById(Class<T> entityClass,Serializable id) throws BuilderSQLException;
	 /**
	  * 根据实体中的主键Id查询
	  * @param object
	  * @return
	  * @throws BuilderSQLException
	  */
	 public SqlModel<Object> selectSqlById(Object object) throws BuilderSQLException;
	 /**
	  * 查询全表sql(小数量时使用)
	  * @param entityClass
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> selectAllSql(Class<T> entityClass) throws BuilderSQLException;
	 /**
	  * 统计单表的数量行数
	  * @param entityClass
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> countSql(Class<T> entityClass) throws BuilderSQLException;
	 
	 public  SqlModel<Object> countSql(Object object) throws BuilderSQLException;
	 
	 
	 /**
	  * 根据主键列表查询
	  * @param entityClass
	  * @param ids
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> selectSqlByIds(Class<T> entityClass,Collection<?> ids) throws BuilderSQLException;
	 /**
	  * 根据主键列表查询
	  * @param entityClass
	  * @param ids
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> selectSqlByIds(Class<T> entityClass,Object[] ids) throws BuilderSQLException;

	/**
	 * 根据实体中的参数查询
	 * <ol>
	 *  <li>  -1: number use = ;String use =  (全部使用等号)</li>
	 * 	<li> simpleModel: number use = ;String use Like</li>
	 * 	<li> middleModel: </li>
	 * 	<li>highModel: </li>
	 * </ol>
	 * @param object  参数对象
	 * @param group  构建分组查询
	 * @param fieldLevel 根据字段级别查询,参数可为null
	 * @param  model sqlConfig构建模型级别
	 * @param afterWhere group by /order  by 子句 参数可为null
	 * @return
	 * @throws BuilderSQLException
	 */
	 public <T> SqlModel<T> sqlConfigSelectSql(Object object,GroupQuery group,FieldLevel fieldLevel, int model, String... afterWhere) throws BuilderSQLException;

	 /**
	  * 根据实体中的参数查询
	  * @param object
	  * @param sqlConfig 特殊参数定义 
	  * @return
	  * @throws BuilderSQLException
	  */
	 public  SqlModel<Map<String,?>> objectModeSelectSql(Object object,SqlConfig sqlConfig) throws BuilderSQLException;
	
	 /**
	  * 根据实体中的参数查询
	  * @param object
	  * @param sqlConfig 特殊参数定义 
	  * @return
	  * @throws BuilderSQLException
	  */
	 public SqlModel<Object> selectSql(Object object,SqlConfig sqlConfig) throws BuilderSQLException;


	 public <T>RowMapper<T> getRowMapper(GroupQuery groupQuery);

	 
	 

}
