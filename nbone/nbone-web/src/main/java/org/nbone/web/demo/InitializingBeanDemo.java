package org.nbone.web.demo;

import org.springframework.beans.factory.InitializingBean;

public class InitializingBeanDemo  implements  InitializingBean{

	@Override
	public void afterPropertiesSet() throws Exception {
		
		System.out.println("===============================================");
		System.out.println("====================InitializingBeanDemo===========================");
		
	}

}
