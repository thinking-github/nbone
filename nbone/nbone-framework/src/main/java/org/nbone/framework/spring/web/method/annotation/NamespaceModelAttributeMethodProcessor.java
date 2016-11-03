package org.nbone.framework.spring.web.method.annotation;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.nbone.framework.spring.web.bind.annotation.Namespace;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.ModelAndViewContainer;
/**
 * ts:itemId 模式使用(注意含有ts：itemId时不要使用itemId spring 默认使用itemId)
 * Namespace Model Parameter Resolver
 * @author thinking 
 * @version 1.0
 * @since 15-12-12
 * @since spring 3.1
 */
public class NamespaceModelAttributeMethodProcessor  extends BaseMethodArgumentResolver{

	
	private final boolean annotationNotRequired;
	
	public NamespaceModelAttributeMethodProcessor() {
		this.annotationNotRequired = false;
	}
	public NamespaceModelAttributeMethodProcessor(boolean annotationNotRequired) {
		this.annotationNotRequired = annotationNotRequired;
	}
	
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		
			return parameter.hasParameterAnnotation(Namespace.class);
	}
	
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
		
		String name = ModelFactory.getNameForParameter(parameter);
		Object attribute = (mavContainer.containsAttribute(name)) ?
				mavContainer.getModel().get(name) : createAttribute(name, parameter, binderFactory, request);

		WebDataBinder binder = binderFactory.createBinder(request, attribute, name);
		if (binder.getTarget() != null) {
			//XXX: thinking 增加一个参数MethodParameter方便使用注解
			bindRequestParameters(binder, request,parameter);
			validateIfApplicable(binder, parameter);
			if (binder.getBindingResult().hasErrors()) {
				if (isBindExceptionRequired(binder, parameter)) {
					throw new BindException(binder.getBindingResult());
				}
			}
		}

		// Add resolved attribute and BindingResult at the end of the model

		Map<String, Object> bindingResultModel = binder.getBindingResult().getModel();
		mavContainer.removeAttributes(bindingResultModel);
		mavContainer.addAllAttributes(bindingResultModel);

		return binder.getTarget();
		
	}
	
	
	@Override
	protected final Object createAttribute(String attributeName,
										   MethodParameter parameter,
										   WebDataBinderFactory binderFactory,
										   NativeWebRequest request) throws Exception {

		String value = getRequestValueForAttribute(attributeName, request);
		if (value != null) {
			Object attribute = createAttributeFromRequestValue(value, attributeName, parameter, binderFactory, request);
			if (attribute != null) {
				return attribute;
			}
		}

		return super.createAttribute(attributeName, parameter, binderFactory, request);
	}

	protected String getRequestValueForAttribute(String attributeName, NativeWebRequest request) {
		Map<String, String> variables = getUriTemplateVariables(request);
		if (StringUtils.hasText(variables.get(attributeName))) {
			return variables.get(attributeName);
		}
		else if (StringUtils.hasText(request.getParameter(attributeName))) {
			return request.getParameter(attributeName);
		}
		else {
			return null;
		}
	}
	
	protected Object createAttributeFromRequestValue(String sourceValue,
													 String attributeName,
													 MethodParameter parameter,
													 WebDataBinderFactory binderFactory,
													 NativeWebRequest request) throws Exception {
			DataBinder binder = binderFactory.createBinder(request, null, attributeName);
			ConversionService conversionService = binder.getConversionService();
			if (conversionService != null) {
				TypeDescriptor source = TypeDescriptor.valueOf(String.class);
				TypeDescriptor target = new TypeDescriptor(parameter);
				if (conversionService.canConvert(source, target)) {
					return binder.convertIfNecessary(sourceValue, parameter.getParameterType(), parameter);
				}
			}
				return null;
			}   

	
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request,MethodParameter parameter) {
		Namespace annot = parameter.getParameterAnnotation(Namespace.class);
		String attrName= (annot != null) ? annot.value() : null;
		if(StringUtils.hasText(attrName)){
			binder.setFieldDefaultPrefix(attrName+":");
		}
		ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
		ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
		servletBinder.bind(servletRequest);
	}

	
	

}
