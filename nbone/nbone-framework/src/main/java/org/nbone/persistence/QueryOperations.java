package org.nbone.persistence;

import org.nbone.mvc.domain.GroupQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
/**
 * 查询基础接口
 * @author thinking
 * @version 1.0
 * @since 2014-08-08
 */
public interface QueryOperations {


	/**
	 * 根据 entity bean含有参数的属性组装查询条件 hibernate get method(参数默认全部使用 等号 =)
	 * @param object 查询实体参数
	 * @param sqlConfig 查询配置
	 *                  <li> fieldNames  按需返回字段java字段名称【返回含有数组中的字段的】,比返回整个实体数据提高效率
	 *                  <li> groupQuery  分组查询 ,
	 *                  <li> fieldLevel  字段级别查询,
	 *                  <li> dbFieldMode 是否启用数据库字段名称模式,
	 *                  <li> afterWhere  追加条件语句 或者 group by /order by 子句 参数可为null 如： and id in(1,2,3,4) （可为空）
	 * @return
	 */
	public <T> List<T> getForList(Object object, SqlConfig sqlConfig);

	/**
	 * 查询（根据 columnMap 条件）
	 *
	 * @param columnMap   columnMap 表字段 map 对象
	 * @param sqlConfig  设置字段为数据库字段对应模式和entityClass
	 * @param <T>
	 * @return
	 */
	public <T> List<T> getForList(Map<String, ?> columnMap,SqlConfig sqlConfig);

	/**
	 * 根据 entity bean含有参数的属性组装查询条件 hibernate get method(参数默认全部使用 等号 =)
	 *
	 * @param object    查询实体参数
	 * @param sqlConfig 查询配置
	 * @param <K>
	 * @param <V>
	 * @return return map 将返回的List 转换成Map默认使用主键作为map key
	 */
	public <K, V> Map<K, V> getMapKeyValue(Object object, SqlConfig sqlConfig);

	public <K, V> Map<K, V> getMapKeyValue(SqlConfig sqlConfig);

	/**
	 * 按照实体中不为空的参数查询实体列表(参数默认使用 number use = /String use like)(支持字段查询符号操作 =  > < >= <=  is null is not null)
	 * @param object 查询实体参数
	 * @param sqlConfig 查询配置
	 * @return
	 */
	public  <T> List<T> queryForList(Object object,SqlConfig sqlConfig);

	/**
	 * 按照实体中的参数查询实体列表（特殊情况下不同得实现方式）
	 * @param object 查询实体参数
	 * @return
	 */
	public  <T> List<T> findForList(Object object);

	/**
	 * 按照实体中的参数查询实体列表（特殊情况下不同得实现方式）
	 * @param object 查询实体参数
	 * @param sqlConfig 查询配置
	 * @param <T>
	 * @return
	 */
	public  <T> List<T> findForList(Object object,SqlConfig sqlConfig);


	/**
	 * 根据实体中的参数查询实体列表并分页(比返回整个实体数据提高效率) (参数默认全部使用 等号 =)【按照实体中的参数查询实体列表并分页】
	 *
	 * @param object  查询实体参数
	 * @param sqlConfig 按需返回字段java字段名称【返回含有数组中的字段的】,afterWhere group by/order by 子句 （可为空）
	 * @param pageNum 当前页码
	 * @param pageSize 页的大小
	 * @param
	 * @param <T>
	 * @return
	 */
	public  <T> Page<T> getForPage(Object object,SqlConfig sqlConfig,int pageNum, int pageSize);
	/**
	 * 根据实体中的参数查询实体列表并分页(比返回整个实体数据提高效率) (参数默认全部使用 等号 =)【按照实体中的参数查询实体列表并分页】
	 *
	 * @param paramMap  查询参数
	 * @param sqlConfig 按需返回字段java字段名称【返回含有数组中的字段的】,afterWhere group by/order by 子句 （可为空）
	 * @param pageNum 当前页码
	 * @param pageSize 页的大小
	 * @param
	 * @param <T>
	 * @return
	 */

	public  <T> Page<T> getForPage(Map<String,?> paramMap,SqlConfig sqlConfig,int pageNum, int pageSize);
	/**
	 * 按照实体中的参数查询实体列表并分页（特殊情况下不同得实现方式 参数默认number use = /String use like）
	 * @param object  查询实体参数
	 * @param sqlConfig 按需返回字段java字段名称【返回含有数组中的字段的】,afterWhere group by/order by 子句 （可为空）
	 * @param pageNum 当前页码
	 * @param pageSize 页的大小
	 * @return
	 */
	public  <T> Page<T> queryForPage(Object object,SqlConfig sqlConfig,int pageNum, int pageSize);

	/**
	 * 按照实体中的参数查询实体列表并分页（可以自定义操作符号类型 = > <  between  in ）
	 * @param object  查询实体参数
	 * @param pageNum 当前页码
	 * @param pageSize 页的大小
	 * @param sqlConfig 查询配置 可以自定义操作符号类型 = > <  between  in
	 * @param <T>
	 * @return
	 */
	public  <T> Page<T> queryForPage(Object object,int pageNum, int pageSize,SqlConfig sqlConfig);

	public  <T> Page<T> findForPage(Object object,int pageNum, int pageSize,String... afterWhere);
	/**
	 * 按照实体中的参数查询实体列表并分页
	 * @param object 查询实体参数
	 * @param pageNum 当前页码
	 * @param pageSize 页的大小
	 * @param sqlConfig  group by/order by 子句
	 * @return
	 * {@link #findForList(Object)}
	 */
	public  <T> Page<T> findForPage(Object object,int pageNum, int pageSize,SqlConfig sqlConfig);

	/**
	 * 按照实体中的参数查询实体列表并限制返回数量  (参数全部使用 等号 =)
	 * @param object 查询实体参数
	 * @param sqlConfig 按需返回字段java字段名称【返回含有数组中的字段的】,分组查询 ,afterWhere group by/order by 子句 （可为空）
	 * @param limit 限制返回的大小
	 * @return
	 */
	public  <T> List<T> getForLimit(Object object,SqlConfig sqlConfig,int limit);

	/**
	 * 按照实体中的参数查询实体列表并限制返回数量  (参数全部使用 等号 = > < )
	 *
	 * @param object    查询实体参数
	 * @param sqlConfig 按需返回字段java字段名称【返回含有数组中的字段的】,分组查询 ,afterWhere group by/order by 子句 （可为空）
	 * @param offset    偏移量位置 默认为 0
	 * @param limit     限制返回的大小
	 * @return
	 */
	public <T> List<T> getForLimit(Object object, SqlConfig sqlConfig, long offset, int limit);

	/**
	 * 按照实体中的参数查询实体列表并限制返回数量  (参数全部使用 等号 =)
	 * @param object 查询实体参数
	 * @param operationMap 设置字段查询操作符号 = > < 等等 （可为空）
	 * @param group 分组查询
	 * @param limit 限制返回的大小
	 * @param afterWhere  group by/order by 子句
	 * @return
	 */
	public  <T> List<T> getForLimit(Object object,Map<String,String> operationMap,GroupQuery group,int limit,String... afterWhere);

	/**
	 * 按照实体中的参数查询实体列表并限制返回数量（特殊情况下不同得实现方式 number use = /String use like）
	 * @param object 查询实体参数
	 * @param sqlConfig 按需返回字段java字段名称【返回含有数组中的字段的】,分组查询 ,afterWhere group by/order by 子句 （可为空）
	 * @param limit 限制返回的大小
	 * @return
	 */
	public  <T> List<T> queryForLimit(Object object,SqlConfig sqlConfig,int limit);


	/**
	 * 根据实体参数查询返回单个字段的列表(比返回整个实体数据提高效率)
	 *
	 * @param object       查询实体参数
	 * @param fieldName    要返回的单个字段名称 默认采用java property mapping
	 * @param requiredType 单个字段的目标类型
	 * @param afterWhere   追加条件语句 或者 group by /order by 子句 参数可为null 如： and id in(1,2,3,4)
	 * @return
	 */
	public <T> List<T> getForList(Object object, String fieldName, Class<T> requiredType, String... afterWhere);

    /**
     * 根据实体参数和配置查询返回单个字段的列表(比返回整个实体数据提高效率)
     *
     * @param object       查询实体参数 可空
     * @param sqlConfig    查询配置
     * @param fieldName    要返回的单个字段名称 默认采用java property mapping
     * @param requiredType 单个字段的目标类型
     * @return
     */
    public <T> List<T> getForList(Object object, SqlConfig sqlConfig, String fieldName, Class<T> requiredType);



}
