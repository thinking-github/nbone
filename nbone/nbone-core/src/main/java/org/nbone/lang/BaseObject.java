package org.nbone.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nbone.util.lang.ToStringUtils;
import org.springframework.beans.BeanUtils;
/**
 * 
 * @author thinking
 * @version 1.0 
 */
public abstract class BaseObject {
	
	
	public static  String inputParameters ="The method of input parameters";
	public static  String outputParameters = "The method of output parameters";
	
	
	protected <E> E newInstance(Class<E> clazz){
		E instance = BeanUtils.instantiate(clazz);
		return instance;	
	}
	
	protected <E> E copyProperties(Object source, Class<E> targetClass){
		E target = newInstance(targetClass);
		BeanUtils.copyProperties(source, target);
		return target;
	}

	protected <E>  List<E> copyProperties(Object[] sources, Class<E> targetClass){
		List<E> targetList = new  ArrayList<E>(sources.length);
		for (Object source : sources) {
			E targetObject = this.copyProperties(source, targetClass);
			targetList.add(targetObject);
		}
		
		return targetList;
	}
	
	protected <E> List<E> copyProperties(Collection<?> sources, Class<E> targetClass){
		List<E> targetList = new  ArrayList<E>(sources.size());
		for (Object source : sources) {
			E targetObject = this.copyProperties(source, targetClass);
			targetList.add(targetObject);
		}
		return targetList;
	}
	/**
	 * 
	 * <p>Discription:[listBean to ListDto]</p>
	 * Created on 2016年4月12日
	 * @param beans        原始数据列表
	 * @param targetClass  转换的目标Class
	 * @return
	 */
	public <S,T> List<T> listBeanConverter(List<S> beans,Class<T> targetClass){
		List<T> results = new ArrayList<T>();
		for (S bean : beans) {
			T dto = this.copyProperties(bean, targetClass);
			results.add(dto);
		}
		return results;
	}
	
	
	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}
	
	
	
	

}
