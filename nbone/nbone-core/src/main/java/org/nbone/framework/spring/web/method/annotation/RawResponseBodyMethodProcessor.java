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
public class RawResponseBodyMethodProcessor extends AbstractMessageConverterMethodProcessor implements JsonProcessor
{
	protected final static List<HttpMessageConverter<?>> messageConverters = (List<HttpMessageConverter<?>>) msgConverters;
	
  /**
   * 
   * 默认的构造器使用默认的 Json 转换器
   * 
   */

  protected RawResponseBodyMethodProcessor()
  {
	 super(messageConverters);
  }
  
 
  protected RawResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters)
  {
    super(messageConverters);
  }

  public boolean supportsReturnType(MethodParameter returnType) {
    return returnType.getMethodAnnotation(RawResponseBody.class) != null;
  }

  public void handleReturnValue(Object returnValue, MethodParameter returnType,
		  						ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception
  {
    mavContainer.setRequestHandled(true);

    writeWithMessageConverters(returnValue, returnType, webRequest);
  }
  
  /*---------------------------------------*/
  public boolean supportsParameter(MethodParameter parameter)
  {
    return false;
  }

  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
    throws Exception
  {
    return null;
  }
}