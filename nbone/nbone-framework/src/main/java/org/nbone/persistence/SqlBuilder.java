package org.nbone.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.nbone.lang.MathOperation;
import org.nbone.mvc.domain.GroupQuery;
import org.nbone.persistence.exception.BuilderSQLException;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.model.SqlModel;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.jdbc.core.RowMapper;

import javax.servlet.ServletRequest;

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
	SqlModel<Object> insertSql(Object object) throws BuilderSQLException;
	/**
	 * 由传入的对象的参数生成insert sql语句(参数值不为空的加入)
	 * @param object
	 * @return {@link SqlModel}
	 * @throws BuilderSQLException
	 */
	SqlModel<Object> insertSelectiveSql(Object object) throws BuilderSQLException;

	/**
	 * 获取主键信息
	 *
	 * @param entityClass
	 * @return
	 */
	List<FieldMapper> getPrimaryKeys(Class<?> entityClass);

	/***
	 * 获取主键信息
	 * @param entityClass
	 * @return
	 */
	FieldMapper getPrimaryKey(Class<?> entityClass);
	 /**
	  * 由传入的对象生成update sql语句
	  *
	  * @param object  实体对象
	  * @param  properties 更新的属性字段 可为空，为空时选择全部字段
	  * @param conditionFields  属性条件 可为空， 为空时默认使用主键作为条件
	  * @return
	  * @throws BuilderSQLException
	  */
	 SqlModel<Object> updateSql(Object object,String[] properties,String[] conditionFields) throws BuilderSQLException;

	/**
	 * 由传入的对象生成update sql语句(参数值不为空的加入)(启用安全属性设置,即为空的属性值不进行更新)
	 * @param object 实体对象
	 * @param conditionFields  属性条件 可为空， 为空时默认使用主键作为条件
	 * @return
	 * @throws BuilderSQLException
	 */
	SqlModel<Object> updateSelectiveSql(Object object,String[] conditionFields) throws BuilderSQLException;

	/**
	 * 由传入的对象生成update sql语句
	 * @param  object   更新实体数据
	 * @param  properties 需要更新的属性字段 可为空
	 * @param isSelective 是否只更新不为null的值
	 * @param conditionFields  属性条件 可为空， 为空时默认使用主键作为条件
	 * @param whereSql    where 部分sql id=1 and name ='chen',  可为null
	 * @return
	 * @throws BuilderSQLException
	 */
	SqlModel<Object> updateSql(Object object,String[] properties,boolean isSelective,String[] conditionFields,String whereSql) throws BuilderSQLException;
	 
	 /**
	  * 由传入的Map对象生成update sql语句
	  * @param entityClass 
	  * @param fieldsMap
	  * @param isDbFieldName map的key是否采用数据库的字段名称(默认采用数据库的字段名称效率高不用转化)
	  * @return
	  * @throws BuilderSQLException
	  */
	 <T> SqlModel<Map<String,?>> updateSql(Class<T> entityClass,Map<String,?> fieldsMap,boolean isDbFieldName) throws BuilderSQLException;
	 /**
	  * 由传入的对象生成update sql语句 <br>
	  *     (首选根据对propertys字段进行计算， 当propertys为空时，参数值不为空且为数字的加入进行数学计算)
	  * @param object
	  * @param  property 计算字段名称 可为空，为空时 参数值不为空且为数字的加入进行数学计算
	  * @param mathOperation 数学运算类型 {@link MathOperation}
	  * @return
	  * @throws BuilderSQLException
	  */
	 SqlModel<Object> updateMathOperationSql(Object object,String property,MathOperation mathOperation) throws BuilderSQLException;


	/**
	 *  由传入的对象生成delete sql语句(包括不为null的属性生成sql)
	 * @param object
	 * @param primaryKey true 只包括主键参数(根据对象主键生成),  <code>false</code> 包括不为null的属性参数
	 * @param tableName 支持分表查询，可为空
	 * @return
	  * @throws BuilderSQLException
	  */
	SqlModel<Object> deleteSqlByEntity(Object object, boolean primaryKey, String tableName)
			throws BuilderSQLException;
	 
	 /**
	  * 根据主键Id删除
	  * @param entityClass
	  * @param id
	  * @return
	  * @throws BuilderSQLException
	  */
	 <T> SqlModel<Map<String,?>> deleteSqlById(Class<T> entityClass,Serializable id,String tableName)
			 throws BuilderSQLException;
	 
	 /**
	  * 根据主键列表Id删除
	  * @param entityClass
	  * @param ids
	  * @return
	  * @throws BuilderSQLException
	  */
	 <T> SqlModel<T> deleteSqlByIds(Class<T> entityClass,Object[] ids,String tableName) throws BuilderSQLException;
	
	 /**
	  * 根据主键Id查询
	  * @param id
	  * @param entityClass
	  * @return
	  * @throws BuilderSQLException
	  */
	 <T> SqlModel<Map<String,?>> selectSqlById(Class<T> entityClass,Serializable id,String tableName)
			 throws BuilderSQLException;
	 /**
	  * 根据实体中的主键Id查询
	  * @param object
	  * @return
	  * @throws BuilderSQLException
	  */
	 SqlModel<Object> selectSqlById(Object object,String tableName) throws BuilderSQLException;
	 /**
	  * 查询全表sql(小数量时使用)
	  * @param entityClass
	  * @return
	  * @throws BuilderSQLException
	  */
	 <T> SqlModel<T> selectAllSql(Class<T> entityClass,String tableName) throws BuilderSQLException;
	 /**
	  * 统计单表的数量行数
	  * @param entityClass
	  * @param afterWhere 增加条件语句 如: and id in(1,2,3)
	  * @return
	  * @throws BuilderSQLException
	  */
	 <T> SqlModel<T> countSql(Class<T> entityClass,String afterWhere) throws BuilderSQLException;

	/**
	 *
	 * 根据实体的参数统计单表的行数
	 * @param object 参数实体
	 * @param sqlConfig 增加条件语句 如: and id in(1,2,3)
	 *
	 * @throws BuilderSQLException
	 */
	SqlModel<Object> countSql(Object object, SqlConfig sqlConfig) throws BuilderSQLException;
	 
	 
	 /**
	  * 根据主键列表查询
	  * @param entityClass
	  * @param ids
	  * @return
	  * @throws BuilderSQLException
	  */
	 <T> SqlModel<T> selectSqlByIds(Class<T> entityClass,Collection<?> ids,String tableName)
			 throws BuilderSQLException;
	 /**
	  * 根据主键列表查询
	  * @param entityClass
	  * @param ids
	  * @return
	  * @throws BuilderSQLException
	  */
	 <T> SqlModel<T> selectSqlByIds(Class<T> entityClass,Object[] ids,String tableName) throws BuilderSQLException;


	 /**
	  * 根据实体中的参数查询
	  * @param object
	  * @param sqlConfig 特殊参数定义 
	  * @return
	  * @throws BuilderSQLException
	  */
	 SqlModel<Map<String,?>> objectModeSelectSql(Object object,SqlConfig sqlConfig) throws BuilderSQLException;

	/**
	 * 根据实体中的参数查询
	 * <ol>
	 * <li>  -1: number use = ;String use =  (全部使用等号)</li>
	 * <li> simpleModel: number use = ;String use Like</li>
	 * <li> middleModel: </li>
	 * <li>highModel: </li>
	 * </ol>
	 *
	 * @param object    参数对象
	 * @param sqlConfig 特殊参数定义
	 * @return
	 * @throws BuilderSQLException
	 */
	SqlModel<Object> selectSql(Object object, SqlConfig sqlConfig) throws BuilderSQLException;

	SqlModel<Map<String, ?>> selectSql(Map<String,?> columnMap, SqlConfig sqlConfig) throws BuilderSQLException;

	/**
	 * 根据Servlet ServletRequest parameter 构建sql进行查询
	 *
	 * @param request   ServletRequest
	 * @param sqlConfig
	 * @return
	 */
	SqlModel<Map<String,Object>> requestQuery(ServletRequest request, SqlConfig sqlConfig);


	<T>RowMapper<T> getRowMapper(GroupQuery groupQuery);

	 
	 

}
