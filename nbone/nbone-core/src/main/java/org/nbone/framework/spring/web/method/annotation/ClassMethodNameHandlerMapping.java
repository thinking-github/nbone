package org.nbone.framework.spring.web.method.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.nbone.framework.spring.web.bind.annotation.ClassMethodNameRequestMapping;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 此类配合Spring MVC RequestMappingHandlerMapping工作, 属于RequestMappingHandlerMapping的辅助
 * <p> 当{@link RequestMapping @RequestMapping} annotations 作用在类上时  且  value is null 默认使用类的短名称
 * <p> 当{@link RequestMapping @RequestMapping} annotations 作用在方法上时 且 value is null  throw Exception
 * 
 * <ul>
 * <li>1.{@code HomeController} -> {@code /home}</li>
 * <li>2.<pre class="code">public Object actionName(HttpServletRequest request, HttpServletResponse response); -> {@code /actionName}</pre>
 * </ul>
 * 
 *  <pre class="code">合并后 -->/home/actionName</pre>
 *  
 * <p>
 * <b>
 * note:
 * <li> 当方法重载时需要使用@RequestMapping value=action加以区分重载的问题
 * <li> 当方法没有使用@RequestMapping且想映射时,方法修饰方式必须是public(防止映射资源过多)
 * 
 * </b>
 * 
 * <p>
 * 
 * @author  thinking
 * @version 1.0 
 * @since spring 3.1
 * @since 2015-12-12
 * @see RequestMapping
 * @see org.nbone.framework.spring.web.bind.annotation.ClassMethodNameRequestMapping
 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
 * @see org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping
 * 
 * 
 */
public class ClassMethodNameHandlerMapping extends RequestMappingHandlerMapping implements InitializingBean {
	
	private static final String CONTROLLER_SUFFIX = "Controller";
	/**
	 * 是否区分大小写
	 */
	private boolean caseSensitive = false;
	
	/**
	 * 根据ClassName生成 url
	 * @param beanClass
	 * @return
	 */
	protected String[] generatePathMappings(Class<?> beanClass) {
		String className = ClassUtils.getShortName(beanClass);
		String path = (className.endsWith(CONTROLLER_SUFFIX) ?
				className.substring(0, className.lastIndexOf(CONTROLLER_SUFFIX)) : className);
		StringBuilder pathMapping =  new StringBuilder("/");
		if (path.length() > 0) {
			if (this.caseSensitive) {
				pathMapping.append(path.substring(0, 1).toLowerCase()).append(path.substring(1));
			}
			else {
				pathMapping.append(path.toLowerCase());
			}
		}
		return new String[] {pathMapping.toString()};
	}

	

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		//跳过父类的方法
		if(!isSelfDeclaredMethod(method, handlerType)){
			return null ;
		}
		
		RequestMappingInfo info = null;
		ClassMethodNameRequestMapping clsMethodName = AnnotationUtils.findAnnotation(handlerType, ClassMethodNameRequestMapping.class);
		//包了一层注解
		// ClassMethodNameRequestMapping
		if(clsMethodName != null){
			
			RequestMapping methodAnnotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
			if(methodAnnotation == null){
				//only load public method
				if(Modifier.isPublic(method.getModifiers())){
					
					RequestCondition<?> methodCondition = getCustomMethodCondition(method);
					info = createRequestMappingInfo(methodAnnotation, methodCondition,method);
					
					RequestMapping typeAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
					RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
					info = createRequestMappingInfo(typeAnnotation, typeCondition,handlerType).combine(info);
					
				}
			}else{
				//Spring MVC RequestMappingHandlerMapping会加载,此类不加载含有@RequestMapping注解的方法
				String[] value = methodAnnotation.value();
				if(value == null ||(value != null && value.length == 0)){
					logger.error(method + "@RequestMapping value must has value.thinking");
					throw new IllegalArgumentException(method + "@RequestMapping value must has value.thinking");
							 
				}
				return null;
			}
			
		}
		return info;
	}

	protected RequestMappingInfo createRequestMappingInfo(RequestMapping annotation,
			RequestCondition<?> customCondition,Object handler) {
		//XXX: not used  RequestMapping
		if(annotation == null){
			return createRequestMappingInfo(customCondition, handler);
		}
		String[] value = annotation.value();
		String[] patterns = resolveEmbeddedValuesInPatterns(value);
		
		//XXX:thining 
		//XXX:增加 RequestMapping value is null 时 默认使用方法名称(包括驼峰式和小写式)
		if(patterns == null ||(patterns != null && patterns.length == 0)){
			
			patterns = getPathMaping(handler);
		}
		
		return new RequestMappingInfo(
				new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(),
						this.useSuffixPatternMatch(), this.useTrailingSlashMatch(), this.getFileExtensions()),
				new RequestMethodsRequestCondition(annotation.method()),
				new ParamsRequestCondition(annotation.params()),
				new HeadersRequestCondition(annotation.headers()),
				new ConsumesRequestCondition(annotation.consumes(), annotation.headers()),
				new ProducesRequestCondition(annotation.produces(), annotation.headers(), this.getContentNegotiationManager()),
				customCondition);
	}
	
	/**
	 * 不使用{@link @RequestMapping}注解时默认类名和方法名称
	 * @param customCondition
	 * @param handler
	 * @return
	 * @author:ChenYiCheng
	 */
	protected RequestMappingInfo createRequestMappingInfo(RequestCondition<?> customCondition,Object handler) {
		String[] patterns = getPathMaping(handler);;
		
		return new RequestMappingInfo(
				new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(),
						this.useSuffixPatternMatch(), this.useTrailingSlashMatch(), this.getFileExtensions()),
				new RequestMethodsRequestCondition(),
				new ParamsRequestCondition(),
				new HeadersRequestCondition(),
				new ConsumesRequestCondition(null,null),
				new ProducesRequestCondition(null, null, this.getContentNegotiationManager()),
				customCondition);
	}
	
	@Override
	public void afterPropertiesSet(){
		

		logger.info("============================================================");
		logger.info("<<<ClassNameMethodNameHandlerMapping initializing>>> .thinking");
		logger.info("============================================================");
		
		int order  = super.getOrder();
		if(order > 5){
			super.setOrder(1);
		}
		super.afterPropertiesSet();
		
	}
	
	/**
	 * @param handler param Class or Method Object
	 * @return
	 */
	protected String[] getPathMaping(Object handler){
		
		String[] pathMapping = {};
		if (handler instanceof Method) {
			Method method  = (Method) handler;
			if(Modifier.isPublic(method.getModifiers())){
				
				String methodName = method.getName();
				pathMapping = new String[]{methodName.toLowerCase(),methodName};
			}
			
		} else if(handler instanceof Class) {
			
			pathMapping = generatePathMappings((Class<?>) handler);
		}
		return pathMapping;
	}
	
	protected  boolean isSelfDeclaredMethod(Method method, Class<?> handlerType){
		
		Method[] meMethods = handlerType.getDeclaredMethods();
		for (Method meMethod : meMethods) {
			if(method.equals(meMethod)){
				return true;
			}
			
		}
		return false;
	}
	

	
	

}
