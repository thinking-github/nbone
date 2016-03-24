package org.nbone.web.context.prepare;


/**
 *
 * <b> reference struts </b> <br> 
 * Constants used by Struts. The constants can be used to get or set objects
 * out of the action context or other collections.
 * <p/>
 * Example:
 * <ul><code>ActionContext.getContext().put(HTTP_REQUEST, request);</code></ul>
 * <p/>
 * or
 * <p/>
 * <ul><code>
 * ActionContext context = ActionContext.getContext();<br>
 * HttpServletRequest request = (HttpServletRequest)context.get(HTTP_REQUEST);</code></ul>
 * 
 * @author thinking  2014-7-16
 * @see StrutsStatics
 */
public interface ServletStatics {
	

    /**
     * Constant for the HTTP request object.
     */
    public static final String HTTP_REQUEST = "dispatcher.HttpServletRequest";

    /**
     * Constant for the HTTP response object.
     */
    public static final String HTTP_RESPONSE = "dispatcher.HttpServletResponse";
    
    /**
     * 参数值信息
     */
    public static final String HTTP_PARAMETERS = "dispatcher.parameters";

    /**
     * Constant for an HTTP {@link javax.servlet.RequestDispatcher request dispatcher}.
     */
    public static final String SERVLET_DISPATCHER = "dispatcher.ServletDispatcher";

    /**
     * Constant for the {@link javax.servlet.ServletContext servlet context} object.
     */
    public static final String SERVLET_CONTEXT = "dispatcher.ServletContext";

    /**
     * Constant for the JSP {@link javax.servlet.jsp.PageContext page context}.
     */
    public static final String PAGE_CONTEXT = "dispatcher.PageContext";

    /** Constant for the PortletContext object */
    public static final String STRUTS_PORTLET_CONTEXT = "struts.portlet.context";

    /**
     * Set as an attribute in the request to let other parts of the framework know that the invocation is happening inside an
     * action tag
     */
    public static final String STRUTS_ACTION_TAG_INVOCATION= "struts.actiontag.invocation";

}
