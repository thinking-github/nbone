package org.nbone.framework.hibernate.util;

import org.nbone.persistence.SqlPropertyDescriptor;
import org.nbone.persistence.criterion.QueryOperator;

/**
 * 
 * @author thinking
 * @version 1.0 
 */
public class HqlPropertyDescriptor extends SqlPropertyDescriptor implements QueryOperator{

	public HqlPropertyDescriptor(String fieldName, Object beginValue, Object endValue) {
		super(fieldName, beginValue, endValue);
	}

	public HqlPropertyDescriptor(String fieldName, String operType) {
		super(fieldName, operType);
	}

	public HqlPropertyDescriptor(String fieldName) {
		super(fieldName);
	}
    
	


}
