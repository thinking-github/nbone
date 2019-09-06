package org.nbone.mvc.domain;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-09-05
 */
public class RequestQuery {
    /**
     * 起始页
     */
    private Integer pageNum;

    /**
     * 单页的大小
     */
    private Integer pageSize;
    /**
     * 查询结果集限制
     */
    private Integer limit;
    /**
     * 排序语句
     */
    private String orderBy;

    public RequestQuery() {

    }

    public RequestQuery(Integer pageNum, Integer pageSize, Integer limit, String orderBy) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.limit = limit;
        this.orderBy = orderBy;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
