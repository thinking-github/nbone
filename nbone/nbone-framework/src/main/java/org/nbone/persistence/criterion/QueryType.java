package org.nbone.persistence.criterion;

import org.hibernate.Hibernate;
import org.hibernate.type.Type;

public interface QueryType extends IQueryTransfer
{

	
	public static final Type SHORT = Hibernate.SHORT;
	public static final Type INTEGER = Hibernate.INTEGER;
	public static final Type LONG = Hibernate.LONG;
	public static final Type FLOAT = Hibernate.FLOAT;
	public static final Type DOUBLE = Hibernate.DOUBLE;
	
	public static final Type BYTE = Hibernate.BYTE;
	public static final Type CHAR = Hibernate.CHARACTER;
	public static final Type BOOLEAN = Hibernate.BOOLEAN;
	
	
	public static final Type CHARACTER = Hibernate.CHARACTER;
	public static final Type STRING = Hibernate.STRING;
	
	public static final Type DATE = Hibernate.DATE;

}

