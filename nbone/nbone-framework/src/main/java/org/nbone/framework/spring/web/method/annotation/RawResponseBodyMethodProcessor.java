package org.nbone.framework.spring.web.method.annotation;

import java.util.List;

import org.nbone.framework.spring.web.bind.annotation.RawResponseBody;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;
/**
 * Java Object Raw Out (默认转化为Json)
 * @author uap
 * @author thinking
 * @version 1.0 
 * @since 2013-08-12
 * @since spring 3.1
 */
@SuppressWarnings("unchecked")
public class RawResponseBodyMethodProcessor extends AbstractMessageConverterMethodProcessor {
	protected final static List<HttpMessageConverter<?>> messageConverters = (List<HttpMessageConverter<?>>) JsonProcessor.msgConverters;
	
  /**
   * 
   * 默认的构造器使用默认的 Json 转换器
   * 
   */

  public RawResponseBodyMethodProcessor() {
	 super(messageConverters);
  }


  public RawResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters) {
    super(messageConverters);
  }

  @Override
  public boolean supportsReturnType(MethodParameter returnType) {
    return returnType.getMethodAnnotation(RawResponseBody.class) != null;
  }

  @Override
  public void handleReturnValue(Object returnValue, MethodParameter returnType,
		  						ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
    mavContainer.setRequestHandled(true);

    writeWithMessageConverters(returnValue, returnType, webRequest);
  }
  
  /*---------------------------------------*/
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return false;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
    throws Exception {
    return null;
  }
}