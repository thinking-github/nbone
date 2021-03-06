package org.nbone.demo.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.nbone.framework.spring.dao.config.JdbcComponentConfig;
import org.nbone.framework.spring.web.filter.RequestIdFilter;
import org.nbone.framework.spring.web.method.annotation.*;
import org.nbone.framework.spring.web.mvc.AccessLogHandlerInterceptor;
import org.nbone.util.DateFPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Import(JdbcComponentConfig.class)
@MapperScan("com.chainnova.salix.tss.mapper")
public class BootApplication extends WebMvcConfigurerAdapter {

	@Autowired
	private JdbcComponentConfig jdbcConfig;

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);

	}

	//自定义持久化配置
	@PostConstruct
	public void init(){
		jdbcConfig.setShowSql(true);
		jdbcConfig.setMybatisMapperId("BaseResultMap");
	}


	//增加Spring MVC request mapping
	@Bean
	public ClassMethodNameHandlerMapping classMethodNameHandlerMapping(){
		return  new ClassMethodNameHandlerMapping();
	}

	//增强Spring MVC request  argument
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

		argumentResolvers.add(new JsonRequestParamMethodArgumentResolver());
		argumentResolvers.add(new JsonRequestBodyMethodProcessor());
		argumentResolvers.add(new FormModelMethodArgumentResolver());
		argumentResolvers.add(new NamespaceModelAttributeMethodProcessor('.'));

	}
    //增强Spring MVC response
	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
	    returnValueHandlers.add(new ItemRequestResponseBodyMethodProcessor());
	    returnValueHandlers.add(new RawResponseBodyMethodProcessor());
	}


	/**
	 * 记录请求的ID
	 * @return
	 */
	@Bean
	public Filter requestIdFilter(){
		return  new RequestIdFilter();
	}

	//http  accessLog
	@Bean
	public HandlerInterceptor accessLogHandlerInterceptor(){
		return  new AccessLogHandlerInterceptor();
	}


	//http cache
	@Bean
	public WebContentInterceptor webContentInterceptor(){
	 	WebContentInterceptor cacheInterceptor = 	new WebContentInterceptor();
		cacheInterceptor.addCacheMapping(CacheControl.maxAge(100, TimeUnit.MINUTES),"/cache/**");
		cacheInterceptor.addCacheMapping(CacheControl.maxAge(120, TimeUnit.MINUTES),"/cache1/**");
		return  cacheInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(accessLogHandlerInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(webContentInterceptor()).addPathPatterns("/static/**","/resource/**");
	}

	//自定义数据模型格式化
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateFPUtils.parseDateMultiplePattern(text));
			}
		});

	}




}
