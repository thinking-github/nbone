package org.nbone.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.nbone.lang.MathOperation;
import org.nbone.persistence.annotation.FieldLevel;
import org.nbone.persistence.exception.BuilderSQLException;
import org.nbone.persistence.model.SqlModel;

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
	  * @param object
	  * @param safeAttr 
	  * @return
	  * @throws BuilderSQLException
	  */
	 public SqlModel<Object> updateSql(Object object) throws BuilderSQLException;
	 
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
	  * 由传入的对象生成update sql语句(参数值不为空的加入)(启用安全属性设置,即为空的属性值不进行更新)
	  * @param object
	  * @return
	  * @throws BuilderSQLException
	  */
	 public SqlModel<Object> updateSelectiveSql(Object object) throws BuilderSQLException;
	 
     
	 /**
	  * 由传入的对象生成update sql语句(参数值不为空且为数字的加入进行数学计算)
	  * @param object
	  * @param mathOperation 数学运算类型 {@link MathOperation}
	  * @return
	  * @throws BuilderSQLException
	  */
	 public SqlModel<Object> updateMathOperationSql(Object object,MathOperation mathOperation) throws BuilderSQLException;
	 
	 
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
	  * @param id
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
	  * 根据实体中的参数查询(全部使用等号)
	  * @param object
	  * @param fieldLevel 根据字段级别查询,参数可为null
	  * @return
	  * @throws Exception
	  */
	 public <T> SqlModel<T> selectSql(Object object,FieldLevel fieldLevel) throws BuilderSQLException;
	 
	 /**
	  * 根据实体中的参数查询
	  * <p> number use = ;String use Like
	  * @param object
	  * @param fieldLevel 根据字段级别查询,参数可为null
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> simpleSelectSql(Object object,FieldLevel fieldLevel) throws BuilderSQLException;
	 
	 /**
	  * 根据实体中的参数查询
	  * @param object
	  * @param fieldLevel 根据字段级别查询,参数可为null
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> middleModeSelectSql(Object object,FieldLevel fieldLevel) throws BuilderSQLException;
	 /**
	  * 根据实体中的参数查询
	  * @param object
	  * @param fieldLevel 根据字段级别查询,参数可为null
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> highModeSelectSql(Object object,FieldLevel fieldLevel) throws BuilderSQLException;
	 /**
	  * 根据实体中的参数查询
	  * @param object
	  * @param sqlConfig 特殊参数定义 
	  * @return
	  * @throws BuilderSQLException
	  */
	 public  SqlModel<Map<String,Object>> objectModeSelectSql(Object object,SqlConfig sqlConfig) throws BuilderSQLException;
	
	 /**
	  * 根据实体中的参数查询
	  * @param object
	  * @param sqlConfig 特殊参数定义 
	  * @return
	  * @throws BuilderSQLException
	  */
	 public SqlModel<Object> selectSql(Object object,SqlConfig sqlConfig) throws BuilderSQLException;
	 
	 
	 

}
