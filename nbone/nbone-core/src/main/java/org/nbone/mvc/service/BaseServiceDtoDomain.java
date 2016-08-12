package org.nbone.mvc.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.nbone.framework.mybatis.SupperMapper;
import org.nbone.framework.spring.data.domain.PageImpl;
import org.nbone.lang.BaseObject;
import org.nbone.lang.MathOperation;
import org.nbone.persistence.BatchSqlSession;
import org.nbone.persistence.SqlSession;
import org.nbone.util.reflect.GenericsUtils;
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
public abstract  class BaseServiceDtoDomain<T,P,IdType extends Serializable> extends BaseObject  implements SuperService<T, IdType>{
	
    @Deprecated
	private SupperMapper<P,IdType> superMapper;
	
	private BaseServiceDomain<P,IdType> baseServiceDomain   = new BaseServiceDomain<P, IdType>();
	
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
	
	
	protected void setSuperMapper(SupperMapper<P,IdType> superMapper,Class<T> dtoClass,Class<P> targetClass) {
		this.superMapper = superMapper;
		this.dtoClass = dtoClass;
		this.targetClass = targetClass;
	}
	/**
	 * 
	 * <p>Discription:供子类继承使用泛型自动推断</p>
	 * 注意: 使用此方法时子类继承此父类必须使用泛型，否则不能推断<p>
	 * Created on 2016年4月7日
	 * @param superMapper
	 * @author:ChenYiCheng
	 */
	
	protected void setSuperMapper(SupperMapper<P,IdType> superMapper) {
		this.superMapper = superMapper;
	}
	

	protected void initMybatisOrm(String namespace,String id) {
		this.superMapper = null;
		baseServiceDomain.initMybatisOrm(namespace, id);
		
	}
	
	
	protected void initMybatisOrm(Class<?> namespace,String id) {
		this.initMybatisOrm(namespace.getName(), id);
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
		if(superMapper != null){
			row =  superMapper.insert(bean);
		}else{
			bean = baseServiceDomain.add(bean);
		}
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
		if(superMapper != null){
			row =  superMapper.insert(bean);
		}else{
			row = baseServiceDomain.insert(bean);
		}
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
		if(superMapper != null){
			superMapper.updateByPrimaryKeySelective(bean);
		}else{
			baseServiceDomain.update(bean);
		}
	   
	}

	@Override
	public void updateSelective(T object) {
		if(object == null){
			return ;
		}
		P bean = this.copyProperties(object, targetClass);
		if(superMapper != null){
		  superMapper.updateByPrimaryKeySelective(bean);
		}else{
		 baseServiceDomain.updateSelective(bean);
		}
	}

	@Override
	public void delete(IdType id) {
		if(superMapper != null){
		    superMapper.deleteByPrimaryKey(id);
		}else{
		   baseServiceDomain.delete(id);	
		}
	}

	@Override
	public T get(IdType id) {
		P pojo;
		if(superMapper != null){
			 pojo = superMapper.selectByPrimaryKey(id);
		}else{
			 pojo =baseServiceDomain.get(id);
		}
		if(pojo == null){
			return null;
		}
		T dto = this.copyProperties(pojo, dtoClass);
		return dto;
	}
	
	/**
	 * 
	 * <p>Discription:listBean to ListDto</p>
	 * Created on 2016年4月1日
	 * @param list  原始数据列表
	 * @return
	 * @author:ChenYiCheng
	 */
	public List<T> listBean2ListDto(List<P> beans){
		
		return listBeanConverter(beans, dtoClass);
	}
	

	@Override
	public void saveOrUpdate(T object) {
	}

	@Override
	public void delete(T object) {
		P bean = this.copyProperties(object, targetClass);
		baseServiceDomain.delete(bean);
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
	public long count(T object) {
		P bean = this.copyProperties(object, targetClass);
		
		return baseServiceDomain.count(bean);
	}

	@Override
	public List<T> getForList(T object) {
		P bean = this.copyProperties(object, targetClass);
		List<P> beans = baseServiceDomain.getForList(bean);
		
		return listBean2ListDto(beans);
	}
	@Override
	public List<T> queryForList(T object) {
		if(object == null){
			return new ArrayList<T>(0);
		}
		
		P bean = this.copyProperties(object, targetClass);
		List<P> beans;
		if(superMapper != null){
			beans  = superMapper.queryForList(bean);
		}else{
			beans = baseServiceDomain.queryForList(bean);
		}
		List<T> dtos  = listBean2ListDto(beans);
		
		return dtos;
	}
	
	
	@Override
	public void batchInsert(T[] objects) {
		List<P> beans = this.copyProperties(objects, targetClass);
		baseServiceDomain.batchInsert(beans);
	}
	@Override
	public void batchInsert(Collection<T> objects) {
		List<P> beans = this.copyProperties(objects, targetClass);
		baseServiceDomain.batchInsert(beans);
	}

	@Override
	public void batchUpdate(T[] objects) {
		List<P> beans = this.copyProperties(objects, targetClass);
		baseServiceDomain.batchUpdate(beans);
	}

	@Override
	public void batchUpdate(Collection<T> objects) {
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
	public Page<T> getForPage(Object object, int pageNum, int pageSize) {
		P bean = this.copyProperties(object, targetClass);
		
		Page<P> page = baseServiceDomain.getForPage(bean, pageNum, pageSize);
		List<P> beans = page.getContent();
		List<T> dtos  = listBean2ListDto(beans);
		
		Page<T> result = new PageImpl<T>(dtos, null, page.getTotalElements());
		return result;
	}

	@Override
	public Page<T> queryForPage(Object object, int pageNum, int pageSize) {
		P bean = this.copyProperties(object, targetClass);
		
		Page<P> page = baseServiceDomain.queryForPage(bean, pageNum, pageSize);
		List<P> beans = page.getContent();
		List<T> dtos  = listBean2ListDto(beans);
		
		Page<T> result = new PageImpl<T>(dtos, null, page.getTotalElements());
		return result;
	}

	@Override
	public Page<T> findForPage(Object object, int pageNum, int pageSize) {
		P bean = this.copyProperties(object, targetClass);
		
		Page<P> page = baseServiceDomain.findForPage(bean, pageNum, pageSize);
		List<P> beans = page.getContent();
		List<T> dtos  = listBean2ListDto(beans);
		
		Page<T> result = new PageImpl<T>(dtos, null, page.getTotalElements());
		return result;
	}

	@Override
	public <E> List<E> getForList(Object object, String fieldName) {
		P bean = this.copyProperties(object, targetClass);
		return baseServiceDomain.getForList(bean, fieldName);
	}

	@Override
	public List<T> getForListWithFieldNames(Object object, String[] fieldNames) {
		P bean = this.copyProperties(object, targetClass);
		List<P> beans = baseServiceDomain.getForListWithFieldNames(bean, fieldNames);
		List<T> dtos  = listBean2ListDto(beans);
		return dtos;
	}

	@Override
	public int updateMathOperation(Object object, MathOperation mathOperation) {
		P bean = this.copyProperties(object, targetClass);
		return baseServiceDomain.updateMathOperation(bean, mathOperation);
	}

	

}
