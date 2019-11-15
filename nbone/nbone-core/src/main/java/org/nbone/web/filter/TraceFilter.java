package org.nbone.web.filter;

import org.nbone.web.util.RequestUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-11-13
 */
@WebFilter(urlPatterns = "/*")
public class TraceFilter implements Filter {

    private boolean enabled = true;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (enabled && (RequestUtils.isDebug(httpRequest) || RequestUtils.isTrace(httpRequest))) {
            httpResponse.addHeader("X-ServerName", request.getServerName());
            httpResponse.addHeader("X-Server-LocalAddr", request.getLocalAddr());
            httpResponse.addHeader("X-Server-LocalName", request.getLocalName());
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
