package org.nbone.mvc.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.nbone.framework.mybatis.SupperMapper;
import org.nbone.mvc.service.SuperService;
import org.nbone.util.reflect.GenericsUtils;
import org.springframework.beans.BeanUtils;

/**
 * 
 * @author thinking chenyicheng
 * @since 2016年3月25日下午3:39:45
 *
 * @param <T> DTOClass
 * @param <P> DomainClass 
 * @param <IdType> 
 */
public abstract  class BaseServiceDtoDomain<T,P,IdType extends Serializable> implements SuperService<T, IdType>{
	
	
	public static  String inputParameters ="The method of input parameters";
	public static  String outputParameters = "The method of output parameters";
	

	private SupperMapper<P,IdType> superMapper;
	
	private Class<T> dtoClass;
	private Class<P> targetClass;
	

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
	@SuppressWarnings("unchecked")
	protected void setSuperMapper(SupperMapper<P,IdType> superMapper) {
		this.superMapper = superMapper;
		this.dtoClass = (Class<T>) GenericsUtils.getSuperClassGenricType(this.getClass(), 0);
		this.targetClass = (Class<P>) GenericsUtils.getSuperClassGenricType(this.getClass(), 1);;
	}
	
	protected <E> E newInstance(Class<E> clazz){
		E instance = BeanUtils.instantiate(clazz);
		return instance;	
	}
	
    //--------------------------------------------------------------------
	@Override
	public IdType save(T object) {
		throw new RuntimeException("请在子类中实现.");
	}
	 
	@Override
	public T add(T object) {
		
		P bean = newInstance(targetClass);
		BeanUtils.copyProperties(object, bean);
		int  row =  superMapper.insert(bean);
		if(row > 0){
			BeanUtils.copyProperties(bean, object);
		}
		
		return object;
	}

	@Override
	public int insert(T object) {
		if(object == null){
			object = newInstance(dtoClass);
		}
		P bean = newInstance(targetClass);
		BeanUtils.copyProperties(object, bean);
		int  row =  superMapper.insert(bean);
		logger.debug("insert {} rows .thinking",row);
		return row;
	}

	@Override
	public void update(T object) {
		if(object == null){
			object = newInstance(dtoClass);
		}
		P bean = newInstance(targetClass);
		BeanUtils.copyProperties(object, bean);
	   int row =  superMapper.updateByPrimaryKeySelective(bean);
	   logger.debug("update {} rows .thinking",row);
	}

	@Override
	public int updateNotNull(T object) {
		if(object == null){
			object = newInstance(dtoClass);
		}
		P bean = newInstance(targetClass);
		BeanUtils.copyProperties(object, bean);
		return superMapper.updateByPrimaryKeySelective(bean);
	}

	@Override
	public void delete(IdType id) {
		int row  = superMapper.deleteByPrimaryKey(id);
		logger.debug("delete {} rows .thinking",row);
	}

	@Override
	public T get(IdType id) {
		
		P pojo = superMapper.selectByPrimaryKey(id);
		if(pojo == null){
			
			return null;
		}
		T dto = newInstance(dtoClass);
		BeanUtils.copyProperties(pojo, dto);
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
			T dto = newInstance(dtoClass);
			BeanUtils.copyProperties(bean, dto);
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
			R dto =  newInstance(targetClass);
			BeanUtils.copyProperties(bean, dto);
			resultDtos.add(dto);
		}
		return resultDtos;
	}

	@Override
	public void saveOrUpdate(T object) {
	}

	@Override
	public void delete(T object) {
	}

	@Override
	public List<T> getAll() {
		return null;
	}
	@Override
	public List<T> getForList(T object) {
		return null;
	}
	@Override
	public List<T> queryForList(T object) {
		if(object == null){
			object = newInstance(dtoClass);
		}
		P bean = newInstance(targetClass);
		BeanUtils.copyProperties(object, bean);
		List<P> beans  = superMapper.queryForList(bean);
		List<T> dtos  = listBean2ListDto(beans);
		
		return dtos;
	}

}
