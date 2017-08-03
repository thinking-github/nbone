package org.nbone.persistence;

import org.nbone.util.PropertyUtil;

public class Test {

	public static void main(String[] args) {
		
		TsProjectDTO test1 =  new TsProjectDTO();
		test1.setId(1L);
		
		TsProjectDTO test2 =  new TsProjectDTO();
		test2.setId(2L);
		
		
		Object value = PropertyUtil.getProperty(test1,"id");
		Object value1 = PropertyUtil.getProperty(test2,"id");
		 
		 System.out.println(value);
		 System.out.println(value1);

	}

}
