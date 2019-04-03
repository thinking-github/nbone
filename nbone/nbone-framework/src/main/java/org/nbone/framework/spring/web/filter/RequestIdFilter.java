package org.nbone.framework.spring.web.filter;

import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 调用请求追踪 requestId
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2019/3/21
 */
public class RequestIdFilter extends OncePerRequestFilter implements Ordered {

    public final static String REQUEST_ID = "web.requestId";

    private IdGenerator idGenerator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (idGenerator == null) {
            idGenerator = new UUIDGenerator();
        }
        //优先使用上层系统传过来的requestId, 如果没有的本系统生成一个requestId
        String requestId = request.getHeader("requestId");
        if (requestId == null || requestId.length() == 0) {
            requestId = request.getParameter("requestId");
        }
        if (requestId == null || requestId.length() == 0) {
            requestId = idGenerator.generateId();
        }

        //唯一的request id，用于问题定位
        request.setAttribute(REQUEST_ID, requestId);
        filterChain.doFilter(request, response);
    }


    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    public interface IdGenerator {

        String generateId();

    }

    public static class UUIDGenerator implements IdGenerator {

        @Override
        public String generateId() {
            return UUID.randomUUID().toString();
        }
    }
}
