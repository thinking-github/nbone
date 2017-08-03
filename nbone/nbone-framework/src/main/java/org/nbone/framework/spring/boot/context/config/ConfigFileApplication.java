package org.nbone.framework.spring.boot.context.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

/**
 * 在传统spring中使用Spring Boot 配置文件 application.properties,application-{profile}.properties
 * 
 * @author thinking
 * @since 2017-1-9
 *
 */
public class ConfigFileApplication extends ConfigFileApplicationListener implements EnvironmentAware, ResourceLoaderAware, ApplicationContextAware {

	protected Logger logger = LoggerFactory.getLogger(ConfigFileApplication.class);

	private Environment environment;

	private ResourceLoader resourceLoader;
    
	/*
	 * (non-Javadoc)
	 * @see org.springframework.boot.context.config.ConfigFileApplicationListener#addPropertySources(org.springframework.core.env.ConfigurableEnvironment, org.springframework.core.io.ResourceLoader)
	 */
	@Override
	protected void addPropertySources(ConfigurableEnvironment environment, ResourceLoader resourceLoader) {
		super.addPropertySources(environment, resourceLoader);
	}

	@Override
	public void setEnvironment(Environment environment) {
		String[] activeProfiles = environment.getActiveProfiles();
		logger.info("spring application config environment active profiles initialize" + Arrays.asList(activeProfiles) + " .thinking");

		this.environment = environment;
		addPropertySources((ConfigurableEnvironment) environment, resourceLoader);

	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Environment environment = applicationContext.getEnvironment();
		String[] activeProfiles = environment.getActiveProfiles();
		logger.info("spring application config environment active profiles : " + Arrays.asList(activeProfiles));

	}

	

}
