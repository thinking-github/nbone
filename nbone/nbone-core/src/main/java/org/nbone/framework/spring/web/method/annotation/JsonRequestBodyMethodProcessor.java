package org.nbone.framework.spring.web.method.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.nbone.constants.ContentType;
import org.nbone.framework.spring.web.bind.annotation.JsonRequestBody;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;
/**
 * 
 * @author thinking
 * @version 1.0 
 * @since spring 3.1
 * @see org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor
 * @see org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor
 */
public class JsonRequestBodyMethodProcessor  extends AbstractMessageConverterMethodProcessor{

	@SuppressWarnings("unchecked")
	protected final static List<HttpMessageConverter<?>> messageConverters = (List<HttpMessageConverter<?>>) JsonProcessor.msgConverters;
	
	protected JsonRequestBodyMethodProcessor() {
		super(messageConverters);
	}

	public JsonRequestBodyMethodProcessor(ContentNegotiationManager manager) {
		super(messageConverters, manager);
	}
	
    @PostConstruct
	public void init(){
    	logger.info("========================================================================");
		logger.info("nbone JsonRequestBodyMethodProcessor starting ....");
		logger.info("========================================================================");
	}
	
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return false;
	}
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(JsonRequestBody.class);
	}
	
	/**
	 * 转换至 application/json
	 * @param inputMessage
	 */
	private void setContentType(HttpInputMessage inputMessage){
		MediaType  mediaType = inputMessage.getHeaders().getContentType();
		
		logger.info("HttpServletRequest ContentType= " +mediaType.toString());
		inputMessage.getHeaders().setContentType(MediaType.APPLICATION_JSON);
	}
	

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		Object argument = readWithMessageConverters(webRequest, parameter, parameter.getGenericParameterType());

		String name = Conventions.getVariableNameForParameter(parameter);
		WebDataBinder binder = binderFactory.createBinder(webRequest, argument, name);

		if (argument != null) {
			validate(binder, parameter);
		}

		mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());

		return argument;
	}

	private void validate(WebDataBinder binder, MethodParameter parameter) throws Exception, MethodArgumentNotValidException {

		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation annot : annotations) {
			if (annot.annotationType().getSimpleName().startsWith("Valid")) {
				Object hints = AnnotationUtils.getValue(annot);
				binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
				BindingResult bindingResult = binder.getBindingResult();
				if (bindingResult.hasErrors()) {
					if (isBindExceptionRequired(binder, parameter)) {
						throw new MethodArgumentNotValidException(parameter, bindingResult);
					}
				}
				break;
			}
		}
	}

	/**
	 * Whether to raise a {@link MethodArgumentNotValidException} on validation errors.
	 * @param binder the data binder used to perform data binding
	 * @param parameter the method argument
	 * @return {@code true} if the next method argument is not of type {@link Errors}.
	 */
	private boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
		int i = parameter.getParameterIndex();
		Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));

		return !hasBindingResult;
	}

	@Override
	protected <T> Object readWithMessageConverters(NativeWebRequest webRequest,
			MethodParameter methodParam,  Type paramType) throws IOException, HttpMediaTypeNotSupportedException {

		final HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpInputMessage inputMessage = new ServletServerHttpRequest(servletRequest);
		
		JsonRequestBody annot = methodParam.getParameterAnnotation(JsonRequestBody.class);
		if (!annot.required()) {
			InputStream inputStream = inputMessage.getBody();
			if (inputStream == null) {
				return null;
			}
			else if (inputStream.markSupported()) {
				inputStream.mark(1);
				if (inputStream.read() == -1) {
					return null;
				}
				inputStream.reset();
			}
			else {
				final PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
				int b = pushbackInputStream.read();
				if (b == -1) {
					return null;
				}
				else {
					pushbackInputStream.unread(b);
				}
				inputMessage = new ServletServerHttpRequest(servletRequest) {
					@Override
					public InputStream getBody() throws IOException {
						// Form POST should not get here
						return pushbackInputStream;
					}
				};
			}
		}
		//XXX:thinking
		setContentType(inputMessage);
		
		return super.readWithMessageConverters(inputMessage, methodParam, paramType);
	}

	
	

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
	}

	
	

}
