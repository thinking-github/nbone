package org.nbone.mvc.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.nbone.framework.spring.data.domain.PageImpl;
import org.nbone.lang.BaseObject;
import org.nbone.lang.MathOperation;
import org.nbone.persistence.BatchSqlSession;
import org.nbone.util.reflect.GenericsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

/**
 * 
 * @author thinking chenyicheng
 * @since 2016年3月25日下午3:39:45
 *
 * @param <T> DTOClass
 * @param <P> DomainClass 
 * @param <IdType> 
 */
@SuppressWarnings(value = "unchecked")
public abstract  class BaseServiceDtoDomain<T,P,IdType extends Serializable> extends BaseObject  implements SuperService<T, IdType>{
	
	private BaseServiceDomain<P,IdType> baseServiceDomain   = new BaseServiceDomain<P, IdType>();

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private BatchSqlSession namedJdbcDao;
	
	private Class<T> dtoClass;
	private Class<P> targetClass;
	
	
    /**
     * 泛型自动推断
     */
	@SuppressWarnings("unchecked")
	public BaseServiceDtoDomain() {
		this.dtoClass = (Class<T>) GenericsUtils.getSuperClassGenricType(this.getClass(), 0);
		this.targetClass = (Class<P>) GenericsUtils.getSuperClassGenricType(this.getClass(), 1);
		
		baseServiceDomain.setTargetClass(targetClass);
	}

	@Resource(name="namedJdbcDao")
	public void setNamedJdbcDao(BatchSqlSession namedJdbcDao) {
		this.namedJdbcDao = namedJdbcDao;
		this.baseServiceDomain.setNamedJdbcDao(this.namedJdbcDao);
		
	}
	
	
	protected void setSuperMapper(Class<T> dtoClass,Class<P> targetClass) {
		this.dtoClass = dtoClass;
		this.targetClass = targetClass;
	}
	/**
	 * 
	 * <p>Discription:供子类继承使用泛型自动推断</p>
	 * 注意: 使用此方法时子类继承此父类必须使用泛型，否则不能推断<p>
	 * Created on 2016年4月7日
	 * @param namespace
	 * @param id
	 * @author:ChenYiCheng
	 */
	

	protected void initMybatisOrm(String namespace,String id,boolean lazyBuild) {
		baseServiceDomain.initMybatisOrm(namespace, id,lazyBuild);
		
	}
	
	
	protected void initMybatisOrm(Class<?> namespace,String id,boolean lazyBuild) {
		this.initMybatisOrm(namespace.getName(), id,lazyBuild);
	}
	
	/*	@Autowired @PostConstruct
	@Override
	protected void initMybatisOrm() {
		super.initMybatisOrm(TsProjectBeanDao.class,"BaseResultMap");
	}*/
	protected  void initMybatisOrm(){
		
	}
    //--------------------------------------------------------------------
	@Override
	public IdType save(T object) {
		P bean = this.copyProperties(object, targetClass);
		
		return baseServiceDomain.save(bean);
		
	}
	 
	@Override
	public T add(T object) {
		P bean = this.copyProperties(object, targetClass);
		int  row = 0;
			bean = baseServiceDomain.add(bean);
		if(row > 0){
			BeanUtils.copyProperties(bean, object);
		}
		
		return object;
	}

	@Override
	public int insert(T object) {
		if(object == null){
			return 0;
		}
		P bean = this.copyProperties(object, targetClass);
		
		int  row = 0;
		row = baseServiceDomain.insert(bean);
		if(row > 0){
			BeanUtils.copyProperties(bean, object);
		}
		
		logger.debug("insert {} rows .thinking",row);
		return row;
	}

	@Override
	public void update(T object) {
		if(object == null){
			logger.warn("update object is null .thinking");
			return ;
		}
		P bean = this.copyProperties(object, targetClass);
		baseServiceDomain.update(bean);
	   
	}

	@Override
	public void updateSelective(T object) {
		if(object == null){
			return ;
		}
		P bean = this.copyProperties(object, targetClass);
		baseServiceDomain.updateSelective(bean);
	}

	@Override
	public void delete(IdType id) {
		   baseServiceDomain.delete(id);	
	}

	@Override
	public T get(IdType id) {
		P pojo;
		pojo =baseServiceDomain.get(id);
		if(pojo == null){
			return null;
		}
		T dto = this.copyProperties(pojo, dtoClass);
		return dto;
	}
	
	/**
	 * transToDTOList/transToDomainList
	 * <p>Discription:listBean to ListDto</p>
	 * Created on 2016年4月1日
	 * @param beans  原始数据列表
	 * @return
	 * @author:ChenYiCheng
	 */
	public List<T> listBean2ListDto(List<P> beans){
		
		return copyProperties(beans, dtoClass);
	}
	

	@Override
	public void saveOrUpdate(T object) {
	}

	@Override
	public int delete(T object) {
		P bean = this.copyProperties(object, targetClass);
		return  baseServiceDomain.delete(bean);
	}

	@Override
	public void deleteByEntityParams(T object) {
		P bean = this.copyProperties(object, targetClass);
		baseServiceDomain.deleteByEntityParams(bean);
	}

	@Override
	public void delete(IdType[] ids) {
		baseServiceDomain.delete(ids);
	}
	@Override
	public void delete(Collection<?> ids) {
		baseServiceDomain.delete(ids);
	}
	@Override
	public List<T> getAll() {
		List<P> beans =  baseServiceDomain.getAll();
		
		return listBean2ListDto(beans);
	}
	@Override
	public List<T> getAll(IdType[] ids) {
		List<P> beans = baseServiceDomain.getAll(ids);
		return listBean2ListDto(beans);
	}
	

	@Override
	public List<T> getAll(Collection<?> ids) {
		List<P> beans = baseServiceDomain.getAll(ids);
		return listBean2ListDto(beans);
	}

	@Override
	public long count() {
		return baseServiceDomain.count();
	}

	
	@Override
	public long count(T object, String afterWhere) {
		P bean = this.copyProperties(object, targetClass);
		
		return baseServiceDomain.count(bean,afterWhere);
	}

	@Override
	public List<T> getForList(T object) {
		return getForList(object,null);
	}
	
	@Override
	public List<T> getForList(T object, String afterWhere) {
		P bean = this.copyProperties(object, targetClass);
		List<P> beans = baseServiceDomain.getForList(bean,afterWhere);
		
		return listBean2ListDto(beans);
	}

	

	@Override
	public List<T> queryForList(T object) {
		return queryForList(object,null);
	}
	@Override
	public List<T> queryForList(T object, String afterWhere) {
		if(object == null){
			return new ArrayList<T>(0);
		}
		
		P bean = this.copyProperties(object, targetClass);
		List<P> beans;
		beans = baseServiceDomain.queryForList(bean,afterWhere);
		List<T> dtos  = listBean2ListDto(beans);
		
		return dtos;
	}
	
	@Override
	public void batchInsert(T[] objects,boolean jdbcBatch) {
		List<P> beans = this.copyProperties(objects, targetClass);
		baseServiceDomain.batchInsert(beans,jdbcBatch);
	}
	@Override
	public void batchInsert(Collection<T> objects,boolean jdbcBatch) {
		List<P> beans = this.copyProperties(objects, targetClass);
		baseServiceDomain.batchInsert(beans,jdbcBatch);
	}

	@Override
	public void batchUpdate(T[] objects,String...propertys) {
		List<P> beans = this.copyProperties(objects, targetClass);
		baseServiceDomain.batchUpdate(beans);
	}

	@Override
	public void batchUpdate(Collection<T> objects,String...propertys) {
		List<P> beans = this.copyProperties(objects, targetClass);
		baseServiceDomain.batchUpdate(beans);
	}

	@Override
	public void batchDelete(Class<T> clazz, Serializable[] ids) {
		baseServiceDomain.batchDelete(targetClass, ids);
	}

	@Override
	public void batchDelete(Class<T> clazz, List<Serializable> ids) {
		baseServiceDomain.batchDelete(targetClass, ids);
		
	}

	@Override
	public Page<T> getForPage(Object object,String[] fieldNames, int pageNum, int pageSize,String... afterWhere) {
		P bean = this.copyProperties(object, targetClass);
		
		Page<P> page = baseServiceDomain.getForPage(bean,fieldNames, pageNum, pageSize,afterWhere);
		List<P> beans = page.getContent();
		List<T> dtos  = listBean2ListDto(beans);
		
		Page<T> result = new PageImpl<T>(dtos, null, page.getTotalElements());
		return result;
	}

	@Override
	public Page<T> queryForPage(Object object, int pageNum, int pageSize,String... afterWhere) {
		P bean = this.copyProperties(object, targetClass);
		
		Page<P> page = baseServiceDomain.queryForPage(bean, pageNum, pageSize,afterWhere);
		List<P> beans = page.getContent();
		List<T> dtos  = listBean2ListDto(beans);
		
		Page<T> result = new PageImpl<T>(dtos, null, page.getTotalElements());
		return result;
	}

	@Override
	public Page<T> findForPage(Object object, int pageNum, int pageSize,String... afterWhere) {
		P bean = this.copyProperties(object, targetClass);
		
		Page<P> page = baseServiceDomain.findForPage(bean, pageNum, pageSize,afterWhere);
		List<P> beans = page.getContent();
		List<T> dtos  = listBean2ListDto(beans);
		
		Page<T> result = new PageImpl<T>(dtos, null, page.getTotalElements());
		return result;
	}

	@Override
	public <E> List<E> getForList(Object object, String fieldName,Class<E> requiredType,String... afterWhere) {
		P bean = this.copyProperties(object, targetClass);
		return baseServiceDomain.getForList(bean, fieldName,requiredType,afterWhere);
	}

	@Override
	public int updateMathOperation(Object object,String property, MathOperation mathOperation) {
		P bean = this.copyProperties(object, targetClass);
		return baseServiceDomain.updateMathOperation(bean,property, mathOperation);
	}

	

}
