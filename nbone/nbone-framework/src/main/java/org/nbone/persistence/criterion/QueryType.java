package org.nbone.persistence.criterion;

import org.hibernate.Hibernate;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

public interface QueryType extends IQueryTransfer {


	/**
	 * org.hibernate.Hibernate;
	 * org.hibernate.type.StandardBasicTypes
	 */
	public static final Type SHORT = StandardBasicTypes.SHORT;
	public static final Type INTEGER = StandardBasicTypes.INTEGER;
	public static final Type LONG = StandardBasicTypes.LONG;
	public static final Type FLOAT = StandardBasicTypes.FLOAT;
	public static final Type DOUBLE = StandardBasicTypes.DOUBLE;
	
	public static final Type BYTE = StandardBasicTypes.BYTE;
	public static final Type CHAR = StandardBasicTypes.CHARACTER;
	public static final Type BOOLEAN = StandardBasicTypes.BOOLEAN;
	
	
	public static final Type CHARACTER = StandardBasicTypes.CHARACTER;
	public static final Type STRING = StandardBasicTypes.STRING;
	
	public static final Type DATE = StandardBasicTypes.DATE;

}

