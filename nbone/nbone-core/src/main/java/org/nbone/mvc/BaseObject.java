package org.nbone.mvc;

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
	
	

}
