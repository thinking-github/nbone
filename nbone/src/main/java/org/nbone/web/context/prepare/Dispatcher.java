package org.nbone.web.context.prepare;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <b>参考struts Dispatcher</b> <br> 
 *   application 全局处理调度器
 *   
 * @author Thinking  2014-7-16
 */
public class Dispatcher {
	
	 private final Log loger = LogFactory.getLog(getClass()); 
	 private ServletContext servletContext;
	 private Map<String, String> initParams;
	 
    /**
      * Provide a thread local instance.
      */
    private static ThreadLocal<Dispatcher> instance = new ThreadLocal<Dispatcher>();
    
  /**
    * Provide the dispatcher instance for the current thread.
    *
    * @return The dispatcher instance
    */
   public static Dispatcher getInstance() {
       return instance.get();
   }

   /**
    * Store the dispatcher instance for this thread.
    *
    * @param instance The instance
    */
   public static void setInstance(Dispatcher dispatcher) {
          instance.set(dispatcher);
   }

	
   /**
     * Create the Dispatcher instance for a given ServletContext and set of initialization parameters.
     *
     * @param servletContext Our servlet context
     * @param initParams The set of initialization parameters
     */
    public Dispatcher(ServletContext servletContext, Map<String, String> initParams) {
        this.servletContext = servletContext;
        this.initParams = initParams;
    }
    
    /**
     * 用于以后扩展<br>
     * Load configurations, including both XML and zero-configuration strategies,
     * and update optional settings, including whether to reload configurations and resource files.
     */
    public void init() {

    }
    
    /**
     * 获取当前filter 初始化的参数 Map
     * @return
     */
    public Map<String, String> getInitParamsMap() {
		return initParams;
	}
    /**
     * 获取当前filter 初始化的参数 的值
     * @param name
     * @return
     */
    public String getFilterInitParameter(String name) {
    	String value = null;
    	if(initParams.containsKey(name)){
    		value = initParams.get(name);	
    	}
		return value;
	}

	/**
     * Create a context map containing all the wrapped request objects
     *
     * @param request The servlet request
     * @param response The servlet response
     * @param context The servlet context
     * @return A map of context objects
     * @see #createContextMap(Map, HttpServletRequest, HttpServletResponse, ServletContext)
     */
    public Map<String,Object> createContextMap(HttpServletRequest request, HttpServletResponse response,ServletContext context) {

        // parameters map wrapping the http parameters.  ActionMapping parameters are now handled and applied separately
        @SuppressWarnings({ "rawtypes", "unchecked" })
		Map params = new HashMap(request.getParameterMap());

        Map<String,Object> extraContext = createContextMap(params,request, response, context);
       
        return extraContext;
    }
    

    /**
     * Merge all application and servlet attributes into a single <tt>HashMap</tt> to represent the entire
     * <tt>Action</tt> context.
     *
     * @param parameterMap   a Map of all request parameters.
     * @param request        the HttpServletRequest object.
     * @param response       the HttpServletResponse object.
     * @param servletContext the ServletContextmapping object.
     * @return a HashMap representing the <tt>Action</tt> context.
     */
    public HashMap<String,Object> createContextMap(Map parameterMap,HttpServletRequest request,HttpServletResponse response,ServletContext servletContext) {
        
    	HashMap<String,Object> extraContext = new HashMap<String,Object>();
    	
        extraContext.put(ServletStatics.HTTP_PARAMETERS,parameterMap);
        extraContext.put(ServletStatics.HTTP_REQUEST, request);
        extraContext.put(ServletStatics.HTTP_RESPONSE, response);
        extraContext.put(ServletStatics.SERVLET_CONTEXT, servletContext);

        return extraContext;
    }
    
    /**
     * 预处理
     * Prepare a request, including setting the encoding and locale.
     *
     * @param request The request
     * @param response The response
     */
    public void prepare(HttpServletRequest request, HttpServletResponse response) {
        String encoding = null;
        Locale locale = null;
        // check for Ajax request to use UTF-8 encoding strictly http://www.w3.org/TR/XMLHttpRequest/#the-send-method
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            encoding = "UTF-8";
        }
        if (encoding != null) {
        	applyRequestEncoding(request, encoding);
        	applyResponseEncoding(response, encoding);
        }

        if (locale != null) {
            response.setLocale(locale);
        }

    }
    
    /**
     * 设置request 字符集
     * @param request
     * @param encoding
     */
    private void applyRequestEncoding(HttpServletRequest request, String encoding) {
        try {
            if (!encoding.equals(request.getCharacterEncoding())) {
                // if the encoding is already correctly set and the parameters have been already read
                // do not try to set encoding because it is useless and will cause an error
                request.setCharacterEncoding(encoding);
            }
        } catch (Exception e) {
            loger.error("Error setting character encoding to '" + encoding + "' - ignoring.", e);
        }
    }
    /**
     * 设置response 字符集
     * @param response
     * @param encoding
     */
    private void applyResponseEncoding(HttpServletResponse response, String encoding) {
        try {
            if (!encoding.equals(response.getCharacterEncoding())) {
                // if the encoding is already correctly set and the parameters have been already read
                // do not try to set encoding because it is useless and will cause an error
            	response.setCharacterEncoding(encoding);
            }
        } catch (Exception e) {
            loger.error("Error setting character encoding to '" + encoding + "' - ignoring.", e);
        }
    }

    
    /**
     * Releases all instances bound to this dispatcher instance.
     */
    public void cleanup() {

        // clean up Dispatcher itself for this thread
        instance.set(null);
        //cleanup action context
        ActionContext.setContext(null);
    }
	
	
	

}
