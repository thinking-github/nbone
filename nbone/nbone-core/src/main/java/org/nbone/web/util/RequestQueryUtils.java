package org.nbone.web.util;

import org.nbone.common.model.PageRequest;
import org.nbone.constants.CaseName;
import org.nbone.mvc.domain.OrderQuery;
import org.nbone.mvc.domain.RequestQuery;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-09-05
 */
public class RequestQueryUtils {

    private static final String DEFAULT_PARAMETER = "sort";
    private static final String DEFAULT_PROPERTY_DELIMITER = ",";

    public final static String[] PAGE_NUM_NAMES = {"pageNum", "pageNo", "pageNow", "page", "currentPage", "pageNumber"};
    public final static String[] PAGE_SIZE_NAMES = {"pageSize", "pagesize", "count", "size", "length"};
    public final static String[] ORDER_BY_NAMES = {"orderBy", "orderby", "sort"};
    public final static String LIMIT = "limit";


    public static void fillPageRequest(PageRequest pageRequest) {
        HttpServletRequest request = getRequest();
        pageRequest.setPageNum(getPageNum(request));
        pageRequest.setPageSize(getPageSize(request));
        pageRequest.setLimit(getLimit(request));
        pageRequest.setOrderBy(getOrderBy(request));
    }

    public static PageRequest getPageRequest() {
        PageRequest pageRequest = new PageRequest();
        fillPageRequest(pageRequest);
        return pageRequest;
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

        return getPageNum(request, PAGE_NUM_NAMES);
    }

    /**
     * @param request
     * @return
     */
    public static Integer getPageNum(ServletRequest request, String[] pageNumNames) {
        for (String pageNumKey : pageNumNames) {
            String pageNum = request.getParameter(pageNumKey);
            if (StringUtils.hasLength(pageNum)) {
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
        return getPageSize(request, PAGE_SIZE_NAMES);
    }

    /**
     * @param request
     * @return
     */
    public static Integer getPageSize(ServletRequest request, String[] pageSizeNames) {
        for (String pageSizeName : pageSizeNames) {
            String pageSize = request.getParameter(pageSizeName);
            if (StringUtils.hasLength(pageSize)) {
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
        return getOrderBy(request, ORDER_BY_NAMES);
    }

    public static String getOrderBy(ServletRequest request, String[] orderNames) {
        for (String orderByName : orderNames) {
            String orderBy = request.getParameter(orderByName);
            if (StringUtils.hasLength(orderBy)) {
                return orderBy;
            }
        }
        Object orderBy = request.getAttribute("orderBy");
        if (orderBy != null && orderBy instanceof String) {
            return (String) orderBy;
        }
        return null;
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = servletRequest.getRequest();

        return request;
    }


    // spring data Sort

    public static String getSort(CaseName caseName, String prefix) {
        Sort sort = getSort();
        return OrderQuery.orderBy(sort, caseName, prefix);
    }

    public static String getSort(ServletRequest request, CaseName caseName, String prefix) {
        Sort sort = getSort(request);
        return OrderQuery.orderBy(sort, caseName, prefix);
    }


    public static Sort getSort() {
        return getSort(getRequest());
    }

    /**
     * page，第几页，从0开始，默认为第0页  <br>
     * size，每一页的大小，默认为20 <br>
     * sort，排序相关的信息，以property,property(,ASC|DESC)的方式组织，例如sort=firstname&sort=lastname,desc表示在按firstname正序排列基础上按lastname倒序排列
     *
     * @param request
     * @return
     */
    public static Sort getSort(ServletRequest request) {
        String[] directionParameter = request.getParameterValues(DEFAULT_PARAMETER);

        // No parameter
        if (directionParameter == null) {
            return null;
        }

        // Single empty parameter, e.g "sort="
        if (directionParameter.length == 1 && !StringUtils.hasText(directionParameter[0])) {
            return null;
        }

        return parseParameterIntoSort(directionParameter, DEFAULT_PROPERTY_DELIMITER);
    }


    /**
     * Parses the given sort expressions into a {@link Sort} instance. The implementation expects the sources to be a
     * concatenation of Strings using the given delimiter. If the last element can be parsed into a {@link Sort.Direction} it's
     * considered a {@link Sort.Direction} and a simple property otherwise.
     *
     * @param source    will never be {@literal null}.
     * @param delimiter the delimiter to be used to split up the source elements, will never be {@literal null}.
     * @return
     */
    static Sort parseParameterIntoSort(String[] source, String delimiter) {

        List<Sort.Order> allOrders = new ArrayList<Sort.Order>();

        for (String part : source) {

            if (part == null) {
                continue;
            }

            String[] elements = part.split(delimiter);
            Sort.Direction direction = elements.length == 0 ? null : Sort.Direction.fromStringOrNull(elements[elements.length - 1]);

            for (int i = 0; i < elements.length; i++) {

                if (i == elements.length - 1 && direction != null) {
                    continue;
                }

                String property = elements[i];

                if (!StringUtils.hasText(property)) {
                    continue;
                }

                allOrders.add(new Sort.Order(direction, property));
            }
        }

        return allOrders.isEmpty() ? null : new Sort(allOrders);
    }


}
