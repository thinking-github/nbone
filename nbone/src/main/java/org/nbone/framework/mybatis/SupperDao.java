package org.nbone.framework.mybatis;

import java.io.Serializable;

public interface SupperDao {
	
	void insertAuto(Object object);
	void updateAuto(Object object);
	void deleteAuto(Object object);
	void get(Class clazz,Serializable id);
	
	

}
