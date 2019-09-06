package org.nbone.web.util;

import org.nbone.common.model.PageRequest;
import org.nbone.mvc.domain.RequestQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-09-05
 */
public class RequestQueryUtils {

    public final static String PAGE_NUM = "pageNum";
    public final static String PAGE_SIZE = "pageSize";
    public final static String ORDER_BY = "orderBy";
    public final static String LIMIT = "limit";


    public static void getPageRequest(PageRequest pageRequest) {
        HttpServletRequest request = getRequest();
        pageRequest.setPageNum(getPageNum(request));
        pageRequest.setPageSize(getPageSize(request));
        pageRequest.setLimit(getLimit(request));
        pageRequest.setOrderBy(getOrderBy(request));
    }

    public static RequestQuery getRequestQuery() {
        return getRequestQuery(getRequest());
    }

    public static RequestQuery getRequestQuery(HttpServletRequest request) {

        Integer pageNum = getPageNum(request);
        Integer pageSize = getPageSize(request);
        Integer limit = getLimit(request);
        String orderBy = getOrderBy(request);
        RequestQuery requestQuery = new RequestQuery(pageNum, pageSize, limit, orderBy);

        return requestQuery;
    }

    public static Integer getPageNum(HttpServletRequest request) {
        String pageNum = request.getParameter(PAGE_NUM);
        if (StringUtils.isEmpty(pageNum)) {
            pageNum = request.getParameter("pageNow");
        }
        if (StringUtils.isEmpty(pageNum)) {
            pageNum = request.getParameter("currentPage");
        }
        if (StringUtils.isEmpty(pageNum)) {
            pageNum = request.getParameter("page");
        }

        // format
        if (StringUtils.hasLength(pageNum)) {
            return Integer.valueOf(pageNum);
        }
        return 1;
    }

    public static Integer getPageSize(HttpServletRequest request) {
        String pageSize = request.getParameter(PAGE_SIZE);
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = request.getParameter("pagesize");
        }
        if (StringUtils.hasLength(pageSize)) {
            return Integer.valueOf(pageSize);
        }
        return 20;
    }

    public static Integer getLimit() {
        return getLimit(getRequest());
    }

    public static Integer getLimit(HttpServletRequest request) {
        String limit = request.getParameter(LIMIT);
        if (StringUtils.hasLength(limit)) {
            return Integer.valueOf(limit);
        }
        return null;
    }

    public static String getOrderBy() {
        return getOrderBy(getRequest());
    }

    public static String getOrderBy(HttpServletRequest request) {
        String orderBy = request.getParameter(ORDER_BY);
        if (StringUtils.isEmpty(orderBy)) {
            orderBy = request.getParameter("orderby");
        }
        if (StringUtils.hasLength(orderBy)) {
            return orderBy;
        }
        return null;
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = servletRequest.getRequest();

        return request;
    }


}
