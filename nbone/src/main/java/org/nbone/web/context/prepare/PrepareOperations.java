package org.nbone.web.context.prepare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.web.util.RequestUtils;

/**
 * <b>参考struts PrepareOperations</b> <br>
 *  application 预处理操作
 *  
 * @author Thinking  2014-7-16
 */

public class PrepareOperations {
	 private final Log loger = LogFactory.getLog(getClass()); 
	private ServletContext servletContext;
	private Dispatcher dispatcher;
	
	public static final String CLEANUP_RECURSION_COUNTER = "__cleanup_recursion_counter";
	  
    public PrepareOperations(ServletContext servletContext, Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.servletContext = servletContext;
    }
	
	/**
	 * 创建 ActionContext 上下文 <br>
	 * 
     * Creates the action context and initializes the thread local
     */
    public ActionContext createActionContext(HttpServletRequest request, HttpServletResponse response) {
        ActionContext ctx;
        Integer counter = 1;
        Integer oldCounter = (Integer) request.getAttribute(CLEANUP_RECURSION_COUNTER);
        if (oldCounter != null) {
            counter = oldCounter + 1;
        }
        
        ActionContext oldContext = ActionContext.getContext();
        if (oldContext != null) {
            // detected existing context, so we are probably in a forward
            ctx = new ActionContext(new HashMap<String, Object>(oldContext.getContextMap()));
        } else {
            Map<String,Object> context = dispatcher.createContextMap(request, response,servletContext);
            ctx = new ActionContext(context);
        }
        request.setAttribute(CLEANUP_RECURSION_COUNTER, counter);
        ActionContext.setContext(ctx);
        return ctx;
    
    }
    
    /**
     * 设置字符集和 本地化 <br>
     * Sets the request encoding and locale on the response
     */
    public void setEncodingAndLocale(HttpServletRequest request, HttpServletResponse response) {
        dispatcher.prepare(request, response);
    }
    
    /**
     * <b>分配调度处理器至 ThreadLocal</b> <br>
     * 
     * Assigns the dispatcher to the dispatcher thread local
     */
    public void assignDispatcherToThread() {
        Dispatcher.setInstance(dispatcher);
    }
    
    /**
     * 清理  ThreadLoad 中的 request <br>
     * Cleans up a request of thread locals
     */
    public void cleanupRequest(HttpServletRequest request) {
        Integer counterVal = (Integer) request.getAttribute(CLEANUP_RECURSION_COUNTER);
        if (counterVal != null) {
            counterVal -= 1;
            request.setAttribute(CLEANUP_RECURSION_COUNTER, counterVal);
            if (counterVal > 0 ) {
                if (loger.isDebugEnabled()) {
                	loger.debug("skipping cleanup counter="+counterVal);
                }
                return;
            }
        }
        // always clean up the thread request, even if an action hasn't been executed
        try {
            //dispatcher.cleanUpRequest(request);
        } finally {
            ActionContext.setContext(null);
            Dispatcher.setInstance(null);
        }
    }
    
    /**
     * 清理 dispatcher 实例
     * Cleans up the dispatcher instance
     * @throws Exception 
     */
    public void cleanupDispatcher() {
        if (dispatcher == null) {
            throw new NullPointerException("Something is seriously wrong, Dispatcher is not initialized(null).");
        } else {
            try {
                dispatcher.cleanup();
            } finally {
                ActionContext.setContext(null);
            }
        }
    }
    
    
    /**
     * Check whether the request matches a list of exclude patterns.
     *
     * @param request  The request to check patterns against
     * @param excludedPatterns list of patterns for exclusion
     *
     * @return <tt>true</tt> if the request URI matches one of the given patterns
     */
    public boolean isUrlExcluded( HttpServletRequest request, List<Pattern> excludedPatterns ) {
        if (excludedPatterns != null) {
            String uri = RequestUtils.getUri(request);
            for ( Pattern pattern : excludedPatterns ) {
                if (pattern.matcher(uri).matches()) {
                    return true;
                }
            }
        }
        return false;
    }
	
	
	

}
