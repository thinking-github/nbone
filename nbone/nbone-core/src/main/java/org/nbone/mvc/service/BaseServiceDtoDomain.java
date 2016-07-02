package org.nbone.mvc.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.nbone.framework.mybatis.SupperMapper;
import org.nbone.mvc.BaseObject;
import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.SqlSession;
import org.nbone.util.reflect.GenericsUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;

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
	

	private SupperMapper<P,IdType> superMapper;
	
	private BaseServiceDomain<P,IdType> baseServiceDomain;
	
	
	private SqlSession namedJdbcDao;
	
	private Class<T> dtoClass;
	private Class<P> targetClass;
	
	
    /**
     * 泛型自动推断
     */
	@SuppressWarnings("unchecked")
	public BaseServiceDtoDomain() {
		baseServiceDomain  = new BaseServiceDomain<P, IdType>();
		this.dtoClass = (Class<T>) GenericsUtils.getSuperClassGenricType(this.getClass(), 0);
		this.targetClass = (Class<P>) GenericsUtils.getSuperClassGenricType(this.getClass(), 1);
		
		baseServiceDomain.setTargetClass(targetClass);
	}

	@Resource(name="namedJdbcDao")
	public void setNamedJdbcDao(SqlSession namedJdbcDao) {
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
		baseServiceDomain.setNamespace(namespace);
		baseServiceDomain.setId(id);
		
	}
	
	
	protected void initMybatisOrm(Class<?> namespace,String id) {
		this.initMybatisOrm(namespace.getName(), id);
	}
	
	/*	@Autowired
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
		//throw new RuntimeException("请在子类中实现.");
		
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
	public int updateNotNull(T object) {
		if(object == null){
			return 0;
		}
		P bean = this.copyProperties(object, targetClass);
		int row ;
		if(superMapper != null){
			row =  superMapper.updateByPrimaryKeySelective(bean);
		}else{
			row = baseServiceDomain.updateNotNull(bean);
		}
		return row;
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
			 long count = baseServiceDomain.getCount();
			 System.out.println(count);
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
		List<T> resultDtos = new ArrayList<T>();
		for (P bean : beans) {
			T dto = this.copyProperties(bean, dtoClass);
			resultDtos.add(dto);
		}
		return resultDtos;
	}
	
	/**
	 * 
	 * <p>Discription:[listBean to ListDto]</p>
	 * Created on 2016年4月12日
	 * @param beans        原始数据列表
	 * @param targetClass  转换的目标Class
	 * @return
	 * @author:ChenYiCheng
	 */
	public <R> List<R> listBean2ListDto(List<? extends Object> beans,Class<R> targetClass){
		List<R> resultDtos = new ArrayList<R>();
		for (Object bean : beans) {
			R dto = this.copyProperties(bean, targetClass);
			resultDtos.add(dto);
		}
		return resultDtos;
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
	public List<T> getAll() {
		List<P> beans =  baseServiceDomain.getAll();
		
		return listBean2ListDto(beans);
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
	public void delete(IdType[] ids) {
		baseServiceDomain.delete(ids);
	}

	@Override
	public List<T> getAll(IdType[] ids) {
		List<P> beans = baseServiceDomain.getAll(ids);
		return listBean2ListDto(beans);
	}

}
