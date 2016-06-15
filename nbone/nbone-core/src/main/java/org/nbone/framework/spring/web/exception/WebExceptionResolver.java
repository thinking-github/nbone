package org.nbone.framework.spring.web.exception;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.nbone.exception.ExceptionUtils;
import org.nbone.framework.spring.web.method.annotation.JsonProcessor;
import org.nbone.mx.datacontrols.support.ResultWrapper;
import org.nbone.web.support.WebResultWrapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.google.common.base.Throwables;
/**
 *  @author uap
 *  @author thinking
 *  @since 2013-08-08
 *  @see org.springframework.web.servlet.handler.SimpleMappingExceptionResolver
 *  @see ExceptionHandlerExceptionResolver
 */
public class WebExceptionResolver extends ExceptionHandlerExceptionResolver implements JsonProcessor{
	
	  private final Map<Class<?>, ExceptionHandlerMethodResolver> exceptionHandlerMethodResolvers = new ConcurrentHashMap();
	  private static final String EXCEPTION_KEY = "CUSTOM_EXCEPTION";
	  private Map<String, String> exMap = new HashMap<String, String>();
	  private Map<String, String> defaultMap = new HashMap<String, String>();
	  private static final String DEFAULT_KEY = "default";
	  private static final String DEFAULT_STATCK_KEY = "showstack";
	  private Map<String, String> stackMap = new HashMap<String, String>();
	  
	  
	  @SuppressWarnings("unchecked")
	  protected final static List<HttpMessageConverter<?>> messageConverters = (List<HttpMessageConverter<?>>) msgConverters;
	  

	  /**
	   * 
	   * 默认的构造器使用默认的 Json 转换器
	   * 
	   */
	  public WebExceptionResolver() {
		  this.setMessageConverters(messageConverters);
		  
	  }	

	protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception)
	  {
	    Class<?> handlerType = handlerMethod.getBeanType();
	    Method method = getExceptionHandlerMethodResolver(handlerType).resolveMethod(exception);

	    if (method != null) {
	      return new ServletInvocableHandlerMethod(handlerMethod.getBean(), method);
	    }
	    try
	    {
	      method = getClass().getMethod("getFailedResult", new Class[] { Exception.class });
	    }
	    catch (Exception e) {
	    }
	    return new ServletInvocableHandlerMethod(this, method);
	  }

	  @ResponseBody
	  public ResultWrapper getFailedResult(Exception exception)
	  {
	    this.logger.error(ExceptionUtils.getFullStackTrace(exception));
	    
	    this.logger.error("RootCause.thinking",Throwables.getRootCause(exception));

	    String exMessage = exception.getMessage();
	    if (exMessage == null)
	      return ResultWrapper.failedResultWraped("操作异常！");
	    if (exMessage.indexOf("validationType") > 0) {
	      exMessage = exMessage.substring(0, exMessage.lastIndexOf("@"));
	      return new ResultWrapper(false, "", exMessage);
	    }

	    boolean flag = false;
	    if (this.exMap.isEmpty()) {
	      return ResultWrapper.failedResultWraped(exception.getMessage());
	    }

	    if (!this.stackMap.isEmpty()) {
	      flag = Boolean.valueOf((String)this.stackMap.get("showstack")).booleanValue();
	    }
	    String errorPage = findMatchingValue(this.exMap, exception);
	    if ((errorPage == null) || ("".equals(errorPage))) {
	      errorPage = (String)this.defaultMap.get("default");
	      if (null == errorPage) {
	        synchronized (this.defaultMap) {
	          errorPage = (String)this.defaultMap.get("default");
	          if (null == errorPage) {
	            errorPage = (String)this.exMap.get("default");
	            this.defaultMap.put("default", errorPage);
	          }
	        }
	      }

	    }

	    if (flag)
	    {
	      return WebResultWrapper.failedResultWraped(ExceptionUtils.getFullStackTrace(exception), errorPage);
	    }

	    return WebResultWrapper.failedResultWraped(exception.getMessage(), errorPage);
	  }

	  private ExceptionHandlerMethodResolver getExceptionHandlerMethodResolver(Class<?> handlerType)
	  {
	    ExceptionHandlerMethodResolver resolver = (ExceptionHandlerMethodResolver)this.exceptionHandlerMethodResolvers.get(handlerType);

	    if (resolver == null) {
	      resolver = new ExceptionHandlerMethodResolver(handlerType);
	      this.exceptionHandlerMethodResolvers.put(handlerType, resolver);
	    }
	    return resolver;
	  }

	  private void initExInfo()
	  {
		String CUSTOM_EXCEPTION="default:$serverPath/test/exception.jsp;com.sgcc.uap.test.TestEx,com.sgcc.uap.test.TestEx2:$serverPath/test/exception1.jsp;com.sgcc.uap.test.TestEx3:$serverPath/test/exception2.jsp;showstack:true";

	    //String exStr = PlatformConfigUtil.getString("CUSTOM_EXCEPTION");
	    String exStr = CUSTOM_EXCEPTION;

	    if ((exStr != null) && (!"".equals(exStr)))
	    {
	      for (String c : exStr.split(";"))
	      {
	        String ks = c.split(":")[0];

	        String v = c.substring(c.indexOf(":") + 1);

	        for (String e : ks.split(","))
	        {
	          if (e.equals("showstack"))
	            this.stackMap.put(e, v);
	          else
	            this.exMap.put(e, v);
	        }
	      }
	    }
	  }

	@SuppressWarnings("rawtypes")
	protected String findMatchingValue(Map<String, String> exceptionMappings, Exception ex)
	  {
	    String value = null;
	    int deepest = 2147483647;
	    for (Map.Entry entry : exceptionMappings.entrySet())
	    {
	      String exceptionMapping = (String)entry.getKey();

	      int depth = getDepth(exceptionMapping, ex);
	      if ((depth >= 0) && (depth < deepest)) {
	        deepest = depth;
	        value = (String)entry.getValue();
	      }
	    }
	    return value;
	  }

	  protected int getDepth(String exceptionMapping, Exception ex)
	  {
	    return getDepth(exceptionMapping, ex.getClass(), 0);
	  }

	  private int getDepth(String exceptionMapping, Class<?> exceptionClass, int depth)
	  {
	    if (exceptionClass.getName().contains(exceptionMapping))
	    {
	      return depth;
	    }

	    if (exceptionClass.equals(Throwable.class)) {
	      return -1;
	    }
	    return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
	  }
	
	
	
	

}
