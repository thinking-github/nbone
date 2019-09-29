package org.nbone.web.util;

import org.nbone.common.model.PageRequest;
import org.nbone.mvc.domain.RequestQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-09-05
 */
public class RequestQueryUtils {

    public final static String[] PAGE_NUM_NAMES  = {"pageNum", "pageNo", "pageNow", "page", "currentPage", "pageNumber"};
    public final static String[] PAGE_SIZE_NAMES = {"pageSize", "pagesize", "count", "size", "length"};
    public final static String[] ORDER_BY_NAMES  = {"orderBy", "orderby", "sort"};
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

    public static RequestQuery getRequestQuery(ServletRequest request) {

        Integer pageNum = getPageNum(request);
        Integer pageSize = getPageSize(request);
        Integer limit = getLimit(request);
        String orderBy = getOrderBy(request);
        RequestQuery requestQuery = new RequestQuery(pageNum, pageSize, limit, orderBy);

        return requestQuery;
    }

    /**
     * pageNum ->pageNo -> pageNow ->page -> currentPage -> pageNumber
     *
     * @param request
     * @return
     */
    public static Integer getPageNum(ServletRequest request) {
        for (String pageNumKey : PAGE_NUM_NAMES) {
            String pageNum = request.getParameter(pageNumKey);
            if(StringUtils.hasLength(pageNum)){
                // format
                return Integer.valueOf(pageNum);
            }
        }
        return 1;
    }

    /**
     * pageSize -> pagesize -> count -> size -> length
     *
     * @param request
     * @return
     */
    public static Integer getPageSize(ServletRequest request) {
        for (String pageSizeName : PAGE_SIZE_NAMES) {
            String pageSize = request.getParameter(pageSizeName);
            if(StringUtils.hasLength(pageSize)){
                return Integer.valueOf(pageSize);
            }
        }
        return 20;
    }

    public static Integer getLimit() {
        return getLimit(getRequest());
    }

    public static Integer getLimit(ServletRequest request) {
        String limit = request.getParameter(LIMIT);
        if (StringUtils.hasLength(limit)) {
            return Integer.valueOf(limit);
        }
        return null;
    }

    public static String getOrderBy() {
        return getOrderBy(getRequest());
    }

    public static String getOrderBy(ServletRequest request) {
        for (String orderByName : ORDER_BY_NAMES) {
            String orderBy = request.getParameter(orderByName);
            if (StringUtils.hasLength(orderBy)) {
                return orderBy;
            }
        }
        return null;
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = servletRequest.getRequest();

        return request;
    }


}
