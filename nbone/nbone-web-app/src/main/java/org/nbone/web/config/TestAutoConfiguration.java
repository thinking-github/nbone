package org.nbone.web.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value={ApplicationProperties.class})
public class TestAutoConfiguration implements InitializingBean{
	
	@Autowired
	private ApplicationProperties properties;
	
	/*@Autowired
	private MyProperties myProperties;*/

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("--------------");
		
	}
	
	
	
	

}
