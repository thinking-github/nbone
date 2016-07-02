package org.nbone.framework.spring.web.method.annotation;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nbone.framework.spring.web.bind.annotation.ItemResponseBody;
import org.nbone.framework.spring.web.bind.annotation.ItemsRequestBody;
import org.nbone.mx.datacontrols.support.ResultWrapper;
import org.nbone.util.lang.StringArrayUtils;
import org.nbone.web.support.QueryResultObject;
import org.nbone.web.support.WrapedItems;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

/**
 * Java Object Result  Wrapper Out (包装成ResultWrapper,默认转化为Json)
 * @author uap
 * @author thinking
 * @version 1.0 
 * @since 2013-08-12
 * @see ResultWrapper
 * @since spring 3.1
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" ,"unused"})
public class ItemRequestResponseBodyMethodProcessor extends AbstractMessageConverterMethodProcessor {
	
  private Map<String, String[]> ignoreFieldsCache = new HashMap();
  
  protected final static List<HttpMessageConverter<?>> messageConverters = (List<HttpMessageConverter<?>>) JsonProcessor.msgConverters;
  
 /**
  * 
  * 默认的构造器使用默认的 Json 转换器
  * 
  */
  protected ItemRequestResponseBodyMethodProcessor(){
	  
	  super(messageConverters);
  }
  /**
   * 
   * 自定义Json 转换器
   * 
   */
  protected ItemRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters)
  {
    super(messageConverters);
  }
   
  /*Response  data */
  public boolean supportsReturnType(MethodParameter returnType) {
    return returnType.getMethodAnnotation(ItemResponseBody.class) != null;
  }

  public void handleReturnValue(Object returnValue, MethodParameter returnType,
		  ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
    mavContainer.setRequestHandled(true);
    ResultWrapper wrapedResult = null;

    if (returnValue == null) {
      wrapedResult = ResultWrapper.successResultWraped();
    }else {
      WrapedItems wrapedItems = new WrapedItems();

      if (returnValue.getClass().getName().equals(QueryResultObject.class.getName()))
      {
        QueryResultObject queryResult = (QueryResultObject)returnValue;
        int count = queryResult.getItemCount();
        wrapedItems.setItemCount(count);

        if (queryResult.getItems() != null) {
          wrapedItems.addItems((List<Object>) queryResult.getItems());
        }
        if (queryResult.getDicItems() != null) {
          wrapedItems.setDicts(queryResult.getDicItems());
        }
        
        wrapedResult = ResultWrapper.successResultWraped(wrapedItems);
      }else{
    	  
    	  wrapedResult = ResultWrapper.successResultWraped(returnValue);
      }
    }
    writeWithMessageConverters(wrapedResult, returnType, webRequest);
  }

  
  
  /*Request  parameter */
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterAnnotation(ItemsRequestBody.class) != null;
  }

  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, 
		  					   NativeWebRequest webRequest, WebDataBinderFactory binderFactory)throws Exception{
    Object arg = readWithMessageConverters(webRequest, parameter, parameter.getParameterType());

    return arg;
  }

  private String[] getIgnoreFields(Class dataClass, String[] showFields)
  {
    if (null != showFields) {
      Arrays.sort(showFields);
    }
    String key = dataClass.getName() + Arrays.toString(showFields);
    String[] ignoreFields = null;

    if (this.ignoreFieldsCache.containsKey(key)) {
      ignoreFields = (String[])this.ignoreFieldsCache.get(key);
    }
    else {
      String[] fields = getAllFields(dataClass);
      if ((null != showFields) && (showFields.length > 0)) {
        ignoreFields = StringArrayUtils.subtract(fields, showFields);
      }
      this.ignoreFieldsCache.put(key, ignoreFields);
    }
    return ignoreFields;
  }

 
  private boolean isCollection(Object returnValue)
  {
    return Collection.class.isAssignableFrom(returnValue.getClass());
  }

  private Class<?> getCollectionGenericType(MethodParameter returnType)
  {
    Type type = returnType.getGenericParameterType();
    ParameterizedType aType = (ParameterizedType)type;
    Type parameterArgType = aType.getActualTypeArguments()[0];
    return (Class)parameterArgType;
  }

private String[] getAllFields(Class clazz)
  {
    Field[] fields = clazz.getDeclaredFields();
    List fieldList = new ArrayList();
    for (Field field : fields) {
      String fieldName = field.getName();
      if (!"serialVersionUID".equals(fieldName)) {
        fieldList.add(field.getName());
      }
    }
    return (String[])fieldList.toArray(new String[fieldList.size()]);
  }
}