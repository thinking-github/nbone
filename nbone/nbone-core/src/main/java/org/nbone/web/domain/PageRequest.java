package org.nbone.web.domain;

import lombok.Data;
import org.nbone.web.util.RequestQueryUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author thinking
 * @version 1.0
 * @since 2020-07-01
 */
@Data
public class PageRequest {

    /**
     * 起始页 @see pageNow/pageNow
     */
    private Integer pageNum;

    /**
     * 单页的大小（兼容pageSize大小写）
     */
    private Integer pageSize;

    private Integer limit;

    /**
     * 排序语句 （兼容orderBy大小写）
     */
    private String orderBy;


    public Integer getPageNum(int def) {
        if (pageNum != null) {
            return pageNum;
        }

        return def;
    }


    public Integer getPageNum() {
        if (pageNum != null) {
            return pageNum;
        }
        this.pageNum = RequestQueryUtils.getPageNum(RequestQueryUtils.getRequest(), PAGE_NUM_NAMES);
        return pageNum;
    }

    /**
     * 获取单页的大小（兼容大小写）
     *
     * @param def 默认值
     * @return
     */
    public Integer getPageSize(int def) {
        if (pageSize != null) {
            return pageSize;
        }
        return def;
    }

    /**
     * 获取单页的大小（兼容大小写）
     *
     * @return
     */
    public Integer getPageSize() {
        if (pageSize != null) {
            return pageSize;
        }
        this.pageSize = RequestQueryUtils.getPageSize(RequestQueryUtils.getRequest(), PAGE_SIZE_NAMES);
        return pageSize;
    }

    /**
     * 获取orderBy 语句 （兼容大小写）
     *
     * @return
     */
    public String getOrderBy() {
        if (orderBy != null) {
            return orderBy;
        }
        this.orderBy = RequestQueryUtils.getOrderBy(RequestQueryUtils.getRequest(), ORDER_BY_NAMES);
        return orderBy;
    }

    public void compatible() {
        compatible(RequestQueryUtils.getRequest());
    }

    /**
     * 兼容模式， 兼容大小写
     *
     * @param request
     */
    public void compatible(HttpServletRequest request) {
        if (this.pageNum == null) {
            this.pageNum = RequestQueryUtils.getPageNum(request, PAGE_NUM_NAMES);
        }
        if (this.pageSize == null) {
            this.pageSize = RequestQueryUtils.getPageSize(request, PAGE_SIZE_NAMES);
        }
        if (this.orderBy == null) {
            this.orderBy = RequestQueryUtils.getOrderBy(request, ORDER_BY_NAMES);
        }
    }

    public final static String[] PAGE_NUM_NAMES = {"pageNo", "pageNow", "page", "currentPage", "pageNumber"};
    public final static String[] PAGE_SIZE_NAMES = {"pagesize", "count", "size", "length"};
    public final static String[] ORDER_BY_NAMES = {"orderby", "sort"};

}

