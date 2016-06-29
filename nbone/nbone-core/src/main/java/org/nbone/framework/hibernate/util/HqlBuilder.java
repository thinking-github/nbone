package org.nbone.framework.hibernate.util;

import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.enums.JdbcFrameWork;
/**
 * 
 * @author thinking
 * @version 1.0 
 */
public class HqlBuilder  extends BaseSqlBuilder{

	public HqlBuilder() {
		super(JdbcFrameWork.HIBERNATE);
	}
	
	
	
	

}
