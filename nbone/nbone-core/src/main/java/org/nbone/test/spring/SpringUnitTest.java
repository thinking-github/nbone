package org.nbone.test.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>Spring  Application Unit  Test</p>
 * @author thinking
 * @version 1.0 
 * @since  2015-12-12
 */

public class SpringUnitTest {

	public Log logger = LogFactory.getLog(SpringUnitTest.class);
	
	ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		
		//ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		ctx = new ClassPathXmlApplicationContext("test.xml");
		
	}
}
