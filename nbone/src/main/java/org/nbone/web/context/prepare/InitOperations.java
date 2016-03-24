package org.nbone.web.context.prepare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.FilterConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * 初始化操作
 * @author Thinking  2014-7-16
 * @see ThinkPrepareFilter
 * @see Dispatcher
 */

public class InitOperations {
	
	private final Log loger = LogFactory.getLog(getClass()); 
	
    /**
     * Creates and initializes the dispatcher
     */
    public Dispatcher initDispatcher(FilterConfig filterConfig ) {
        Dispatcher dispatcher = createDispatcher(filterConfig);
        dispatcher.init();
        return dispatcher;
    }
    
    /**
     * Create a {@link Dispatcher}
     */
    private Dispatcher createDispatcher(FilterConfig filterConfig ) {
        Map<String, String> params = new HashMap<String, String>();
        
        for (Enumeration<String> e = filterConfig.getInitParameterNames(); e.hasMoreElements(); ) {
            String name = (String) e.nextElement();
            String value = filterConfig.getInitParameter(name);
            params.put(name, value);
        }
        
        return new Dispatcher(filterConfig.getServletContext(), params);
    }
    
    public void cleanup() {
        ActionContext.setContext(null);
    }
    
	/**
	 * 构建排除过滤的的匹配
	 * @param dispatcher
	 * @return
	 */
    public List<Pattern> buildExcludedPatternsList(Dispatcher dispatcher ) {
    	String excludePattern = dispatcher.getFilterInitParameter(ThinkConstants.FILTER_EXCLUDE_PATTERN);
    	
    	if(excludePattern != null){
    		return buildExcludedPatternsList(excludePattern);
    	}
    	//TODO:构建排除过滤的的匹配,配置文件方式还未添加
    	//另一种情况使用配置后续添加
    	
    	//buildExcludedPatternsList(ThinkConstants.ACTION_EXCLUDE_PATTERN);
        return null;
    }
    
    private List<Pattern> buildExcludedPatternsList( String patterns ) {
        if (null != patterns && patterns.trim().length() != 0) {
            List<Pattern> list = new ArrayList<Pattern>();
            String[] tokens = patterns.split(",");
            for ( String token : tokens ) {
                list.add(Pattern.compile(token.trim()));
            }
            return Collections.unmodifiableList(list);
        } else {
            return null;
        }
    }
	
	

}
