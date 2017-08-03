package org.nbone.web.context.prepare;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 预处理filter 此过滤器将 request,response等参数缓存下来,用于application其他位置
 *  
 * @author thinking  
 * @since 2014-7-16
 * @see struts2
 * 
 */

public class ThinkPrepareFilter implements Filter  {
	
	protected static final String REQUEST_EXCLUDED_FROM_ACTION_MAPPING = ThinkPrepareFilter.class.getName() + ".REQUEST_EXCLUDED_FROM_ACTION_MAPPING";
	protected PrepareOperations prepare;
	protected List<Pattern> excludedPatterns = null;
	protected Log logger = LogFactory.getLog(getClass());
			

	public void init(FilterConfig filterConfig) throws ServletException {
		  InitOperations init = new InitOperations();
		  Dispatcher dispatcher = null;
		  try {
			     //logger.info(new StringBuilder(" [start initialize. thinking]"));
			    //初始化全局调度器
	            dispatcher = init.initDispatcher(filterConfig);
	            //预处理操作
	            prepare = new PrepareOperations(filterConfig.getServletContext(), dispatcher);
	            //构建排除的过滤的URL
	            this.excludedPatterns = init.buildExcludedPatternsList(dispatcher);

	            postInit(dispatcher, filterConfig);
	            //logger.info(new StringBuilder(" [end initialize. thinking]"));
	        } finally {
	            init.cleanup();
	        }
		
	}
   /**
	 * 自定义回调函数用于扩展
     * Callback for post initialization
     */
    protected void postInit(Dispatcher dispatcher, FilterConfig filterConfig) {
    }

	public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        try {
            if (excludedPatterns != null && prepare.isUrlExcluded(request, excludedPatterns)) {
                request.setAttribute(REQUEST_EXCLUDED_FROM_ACTION_MAPPING, new Object());
            } else {
                prepare.setEncodingAndLocale(request, response);
                prepare.createActionContext(request, response);
                prepare.assignDispatcherToThread();
            }
            chain.doFilter(request, response);
        } finally {
            prepare.cleanupRequest(request);
        }
    }

	public void destroy() {
		 prepare.cleanupDispatcher();
		
	}
	
   
}
