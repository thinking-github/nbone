package org.nbone.mvc.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nbone.common.model.DataGrid;
import org.nbone.framework.spring.dao.BaseJdbcDao;
import org.nbone.framework.spring.dao.config.JdbcComponentConfig;
import org.nbone.lang.BaseObject;
import org.nbone.lang.MathOperation;
import org.nbone.mvc.domain.GroupQuery;
import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.BatchSqlSession;
import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.SqlSession;
import org.nbone.util.reflect.GenericsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * 
 * @author thinking
 * @since 2016年3月25日下午3:39:45
 *
 * @param <P>
 * @param <Id>
 */
public  class BaseServiceDomain<P,Id extends Serializable> extends BaseObject implements BaseService<P, Id>{

	protected final  Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name="baseJdbcDao")
	private BaseJdbcDao baseJdbcDao;

	@Resource
	private BatchSqlSession namedJdbcDao;

	@Resource
	private JdbcComponentConfig jdbcComponentConfig;
	
	private Class<P> targetClass;
	/**
	 * 	
	 * 映射mybatis  namespace
	 */
	private String namespace;
	/**
	 * 	
	 * 映射mybatis 实体  resultMap id
	 */
	private String id;

	/**
	 * 数据库表名称
	 */
	private  String tableName;
	
	private  boolean builded =  false;
	
	private static long count = 0;

	private boolean mybatis = true;

	
	public BaseServiceDomain() {

		this.targetClass = (Class<P>) GenericsUtils.getSuperClassGenricType(this.getClass(), 0);
		count ++;
	}
	public BaseServiceDomain(boolean mybatis) {
		this();
		this.mybatis = mybatis;
	}
    /**
     * 初始化mybatis 映射实体 
     * @param namespace  映射文件的命名空间
     * @param id    resultMap id
     */
	protected void initMybatisOrm(String namespace,String id,boolean lazyBuild) {
		this.setNamespace(namespace);
		this.setId(id);
		if(!lazyBuild){
			builded();
		}
	}
	
	protected void initMybatisOrm(Class<?> namespace,String id,boolean lazyBuild) {
		this.initMybatisOrm(namespace.getName(), id,lazyBuild);
	}

	protected void initMybatisOrm(Class<?> namespace,String id) {
		this.initMybatisOrm(namespace.getName(), id,true);
	}

	protected void initMybatisOrm(Class<?> namespace,String id,String tableName) {
		this.setTableName(tableName);
		this.initMybatisOrm(namespace.getName(), id,true);

	}

	protected void initMybatisOrm(Class<?> namespace) {

		String id = jdbcComponentConfig.getMybatisMapperId();
		this.initMybatisOrm(namespace.getName(), id,true);
	}
	
	/*
	@Autowired
	@PostConstruct
	@Override
	protected void initMybatisOrm() {
		super.initMybatisOrm(TsProjectBeanDao.class,"BaseResultMap");
	}*/
	protected  void initMybatisOrm(){
		
	}


	protected synchronized void builded() {
		if (!builded) {
			try {
				if (mybatis) {
					BaseSqlBuilder.buildTableMapper(targetClass, namespace, id);
				} else {
					BaseSqlBuilder.buildEntityMapper(targetClass);
				}
				builded = true;
			} catch (Exception e) {
				builded = false;
				logger.error(e.getMessage(), e);
			}

		}
	}
	
	protected  void checkBuilded(){
		if(!builded){
			builded();
		}
	}
	
	
    public SqlSession getNamedJdbcDao() {
		return namedJdbcDao;
	}

	public void setNamedJdbcDao(BatchSqlSession namedJdbcDao) {
		this.namedJdbcDao = namedJdbcDao;
	}

	public Class<P> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<P> targetClass) {
		this.targetClass = targetClass;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public static long getCount() {
		return count;
	}

	public static void setCount(long count) {
		BaseServiceDomain.count = count;
	}

	//--------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@Override
	public Id save(P object) {
		checkBuilded();
		Serializable id  = namedJdbcDao.save(object);
		return (Id) id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public P add(P object) {
		checkBuilded();
		object = (P) namedJdbcDao.add(object);
		return object;
	}

	@Override
	public int insert(P object) {
		checkBuilded();
		int row = namedJdbcDao.insert(object);
		return row;
	}


	@Override
	public void update(P object) {
		checkBuilded();
		namedJdbcDao.updateSelective(object);
	}

	@Override
	public void updateField(P object, String name, String[] conditionFields, String whereString) {
		updateSelective(object,new String[]{name},conditionFields,whereString);
	}

	@Override
	public void updateSelective(P object) {
		checkBuilded();
		namedJdbcDao.updateSelective(object);
	}
	@Override
	public void updateSelective(P object, String whereSql) {
		checkBuilded();
		namedJdbcDao.updateSelective(object,null,whereSql);
	}
	@Override
	public void updateSelective(P object,String[] properties, String whereSql) {
		checkBuilded();
		namedJdbcDao.updateSelective(object,properties,whereSql);
	}

	@Override
	public int updateSelective(Object object, String[] properties, String[] conditionFields, String whereString) {
		checkBuilded();
		return namedJdbcDao.updateSelective(object,properties,conditionFields,whereString);
	}

	@Override
	public void delete(Id id,String tableName) {
		checkBuilded();
		namedJdbcDao.delete(targetClass, id,tableName);
	}

	@Override
	public P get(Id id,String tableName) {
		checkBuilded();
		 P bean = namedJdbcDao.get(targetClass, id,tableName);
		return bean;
	}

	@Override
	public void saveOrUpdate(P object) {
		
	}

	@Override
	public int delete(P object) {
		checkBuilded();
		return  namedJdbcDao.delete(object);
	}


	@Override
	public void deleteByEntity(P object) {
		checkBuilded();
		namedJdbcDao.deleteByEntity(object);
	}
	@Override
	public List<P> getAll(String tableName) {
		checkBuilded();
		List<P> beans = namedJdbcDao.getAll(targetClass,tableName);
		return beans;
	}


	@Override
	public List<P> getForList(P object) {
		return getForList(object,(SqlConfig) null);
	}
	@Override
	public List<P> getForList(P object, String afterWhere) {
		SqlConfig sqlConfig = null;
		if(afterWhere != null){
			sqlConfig = new SqlConfig(-1).afterWhere(afterWhere);
		}
		return getForList(object,sqlConfig);
	}
	@Override
	public List<P> queryForList(P object) {
		return queryForList(object,  (SqlConfig)null);
	}
	@Override
	public List<P> queryForList(P object, String afterWhere) {
		SqlConfig sqlConfig = null;
		if(afterWhere != null){
			sqlConfig = new SqlConfig(SqlConfig.PrimaryMode).afterWhere(afterWhere);
		}
		return queryForList(object,sqlConfig);
	}

	//--- SqlConfig sqlConfig support
	@Override
	public List<P> getForList(P object, SqlConfig sqlConfig) {
		checkBuilded();
		List<P> beans =  namedJdbcDao.getForList(object,sqlConfig);
		return beans;
	}

	@Override
	public List<P> queryForList(P object, SqlConfig sqlConfig) {
		checkBuilded();
		List<P> beans =  namedJdbcDao.queryForList(object,sqlConfig);
		return beans;
	}

	@Override
	public List<P> getForLimit(P object, SqlConfig sqlConfig, int limit) {
		checkBuilded();
		return namedJdbcDao.getForLimit(object,sqlConfig, limit);
	}
	@Override
	public List<P> queryForLimit(P object,SqlConfig sqlConfig,int limit) {
		checkBuilded();
		return namedJdbcDao.queryForLimit(object,sqlConfig, limit);
	}

	@Override
	public Page<P> getForPage(P object, SqlConfig sqlConfig, int pageNum, int pageSize) {
		checkBuilded();
		return namedJdbcDao.getForPage(object,sqlConfig,pageNum,pageSize);
	}

	@Override
	public Page<P> queryForPage(P object, SqlConfig sqlConfig, int pageNum, int pageSize) {
		checkBuilded();
		return namedJdbcDao.queryForPage(object,sqlConfig,pageNum,pageSize);
	}

	@Override
	public long count(P object, SqlConfig sqlConfig) {
		checkBuilded();
		return namedJdbcDao.count(object,sqlConfig);
	}

	@Override
	public boolean execute(String sql) {
		JdbcTemplate jdbcTemplate =  baseJdbcDao.getJdbcTemplate();
		if(jdbcTemplate == null ){
			jdbcTemplate = baseJdbcDao.getSuperJdbcTemplate();
		}
		try {
			jdbcTemplate.execute(sql);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(),e);
			throw  e;
		}
		return true;
	}


	@Override
	public void delete(Id[] ids,String tableName) {
		checkBuilded();
		namedJdbcDao.delete(targetClass, ids,tableName);
	}
	
	@Override
	public void delete(Collection<?> ids,String tableName) {
		checkBuilded();
		namedJdbcDao.delete(targetClass, ids.toArray(),tableName);
	}


	@Override
	public List<P> getAll(Id[] ids,String tableName) {
		checkBuilded();
		List<P> beans = namedJdbcDao.getAll(targetClass, ids,tableName);
		return beans;
	}
	@Override
	public List<P> getAll(Collection<?> ids,String tableName) {
		checkBuilded();
		List<P> beans = namedJdbcDao.getAll(targetClass, ids,tableName);
		return beans;
	}
	
	@Override
	public long count() {
		return count(null);
	}

	@Override
	public long count(String afterWhere) {
		checkBuilded();
		return namedJdbcDao.count(targetClass,afterWhere);
	}

	@Override
	public long count(P object,String afterWhere) {
		SqlConfig sqlConfig = null;
		if(afterWhere != null){
			sqlConfig = new SqlConfig(-1).afterWhere(afterWhere);
		}
		return count(object,sqlConfig);
	}

	/*
	 * 批量处理增/删/改
	 */
	@Override
	public void batchInsert(P[] objects,boolean jdbcBatch) {
		this.batchInsert(objects,null,jdbcBatch);
	}
	@Override
	public void batchInsert(Collection<P> objects,boolean jdbcBatch) {
		this.batchInsert(objects,null,jdbcBatch);
	}

	@Override
	public void batchInsert(Object[] objects, String[] insertProperties, boolean jdbcBatch) {
		checkBuilded();
		namedJdbcDao.batchInsert(objects,insertProperties,jdbcBatch);
	}

	@Override
	public void batchInsert(Collection<?> objects, String[] insertProperties, boolean jdbcBatch) {
		checkBuilded();
		namedJdbcDao.batchInsert(objects,insertProperties,jdbcBatch);
	}

	@Override
	public void batchUpdate(P[] objects,String...properties) {
		checkBuilded();
		namedJdbcDao.batchUpdate(objects,properties);
	}
	@Override
	public void batchUpdate(Collection<P> objects,String...properties) {
		checkBuilded();
		namedJdbcDao.batchUpdate(objects,properties);
	}
	
	@Override
	public void batchDelete(Class<P> clazz, Serializable[] ids) {
		checkBuilded();
		namedJdbcDao.batchDelete(clazz, ids);
	}
	@Override
	public void batchDelete(Class<P> clazz, List<Serializable> ids) {
		checkBuilded();
		namedJdbcDao.batchDelete(clazz, ids);
	}

	/*
	 * --------------------分页限制结果集--------------------
	 */
	@Override
	public Page<P> getForPage(P object, String[] fieldNames,int pageNum, int pageSize,String... afterWhere) {
		SqlConfig sqlConfig =  new SqlConfig(-1).fieldNames(fieldNames).afterWhere(afterWhere);
		return getForPage(object,sqlConfig,pageNum,pageSize);
	}
	@Override
	public Page<P> queryForPage(P object, int pageNum, int pageSize,String... afterWhere) {
		checkBuilded();
		SqlConfig sqlConfig =  new SqlConfig(SqlConfig.PrimaryMode).afterWhere(afterWhere);
		return namedJdbcDao.queryForPage(object,sqlConfig, pageNum, pageSize);
	}
	@Override
	public Page<P> findForPage(Object object, int pageNum, int pageSize,String... afterWhere) {
		checkBuilded();
		return namedJdbcDao.findForPage(object, pageNum, pageSize,afterWhere);
	}

	@Override
	public List<P> getForLimit(Object object, Map<String, String> operationMap,GroupQuery group,int limit, String... afterWhere) {
		checkBuilded();
		return namedJdbcDao.getForLimit(object,operationMap, group,limit,afterWhere);
	}

	/*
     * ----------------------按需返回字段列----------------------
     */
	@Override
	public <E> List<E> getForList(Object object, String fieldName,Class<E> requiredType,String... afterWhere) {
		checkBuilded();
		return namedJdbcDao.getForList(object, fieldName,requiredType);
	}
	@Override
	public int updateMathOperation(Object object,String property, MathOperation mathOperation) {
		checkBuilded();
		return namedJdbcDao.updateMathOperation(object,property, mathOperation);
	}

	/**
	 * 工具方法
	 */
	public <T> DataGrid<T>  to(Page<T> page) {
		if(page == null){
			return null;
		}
		DataGrid<T> dataGrid = new DataGrid<T>(page.getTotalElements(),page.getTotalPages(),page.getContent());

		return dataGrid;
	}





}
