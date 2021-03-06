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
	
	
	public static  String INPUT_PARAMETERS ="The method of input parameters";
	public static  String OUTPUT_PARAMETERS = "The method of output parameters";
	
	
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


	public boolean  state(int number) {
		if(number > 0){
			return true;
		}
		return false;
	}

	public boolean state(long number) {
		if(number > 0){
			return true;
		}
		return false;
	}

	public boolean state(Number number) {
		if(number == null){
			return  false;
		}
		if(number.intValue() > 0){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}
	

	

}
