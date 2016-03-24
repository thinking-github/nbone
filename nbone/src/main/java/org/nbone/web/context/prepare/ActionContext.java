package org.nbone.web.context.prepare;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * <b>参照struts ActionContext</b><br><p>
 * 
 * 此上下文可以获取到当前使用的 request,response..
 * 
 * @author Thinking  2014-7-16
 * @see ServletActionContext
 * 
 */


public class ActionContext implements ServletStatics,Serializable {

	private static final long serialVersionUID = 2078011969783315065L;
	
	
	private static ThreadLocal<ActionContext> actionContext = new ThreadLocal<ActionContext>();
	private Map<String,Object> context;
	
	  //*************************简化使用request,response等*****************************
	   /**
	     * Returns the HTTP page context.
	     *
	     * @return the HTTP page context.
	     */
	    public static PageContext getPageContext() {
	        return (PageContext) ActionContext.getContext().get(PAGE_CONTEXT);
	    }

	    /**
	     * Sets the HTTP servlet request object.
	     *
	     * @param request the HTTP servlet request object.
	     */
	    public static void setRequest(HttpServletRequest request) {
	        ActionContext.getContext().put(HTTP_REQUEST, request);
	    }

	    /**
	     * Gets the HTTP servlet request object.
	     *
	     * @return the HTTP servlet request object.
	     */
	    public static HttpServletRequest getRequest() {
	        return (HttpServletRequest) ActionContext.getContext().get(HTTP_REQUEST);
	    }

	    /**
	     * Sets the HTTP servlet response object.
	     *
	     * @param response the HTTP servlet response object.
	     */
	    public static void setResponse(HttpServletResponse response) {
	        ActionContext.getContext().put(HTTP_RESPONSE, response);
	    }

	    /**
	     * Gets the HTTP servlet response object.
	     *
	     * @return the HTTP servlet response object.
	     */
	    public static HttpServletResponse getResponse() {
	        return (HttpServletResponse) ActionContext.getContext().get(HTTP_RESPONSE);
	    }

	    /**
	     * Gets the servlet context.
	     *
	     * @return the servlet context.
	     */
	    public static ServletContext getServletContext() {
	        return (ServletContext) ActionContext.getContext().get(SERVLET_CONTEXT);
	    }

	    /**
	     * Sets the current servlet context object
	     *
	     * @param servletContext The servlet context to use
	     */
	    public static void setServletContext(ServletContext servletContext) {
	        ActionContext.getContext().put(SERVLET_CONTEXT, servletContext);
	    }
	    
	    //--------------------------------------------------------------------
		public ActionContext(Map<String,Object> context) {
			this.context = context;
		}
	
		public static void setContext(ActionContext context) {
			actionContext.set(context);
		}
	
		public static ActionContext getContext() {
			return  actionContext.get();
		}
		
		public void setContextMap(Map<String,Object> contextMap) {
			getContext().context = contextMap;
		}

		public Map<String,Object> getContextMap() {
			return context;
		}
		
		public Object get(String key) {
			return context.get(key);
		}
	
		public void put(String key, Object value) {
			context.put(key, value);
		}
	


}
