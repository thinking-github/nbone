package org.nbone.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.json.bind.annotation.JsonbTransient;

/**
 * @author thinking
 * @version 1.0
 * @since 2019/6/9
 */

@Data
public class PageRequet {

    /**
     * 起始页 @see pageNum.（他们含义一样用于兼容）
     */
    @JsonIgnore
    @JsonbTransient
    private Integer pageNow;
    /**
     * 起始页 @see pageNow
     */
    private Integer pageNum;

    /**
     * 单页的大小（兼容pageSize大小写）
     */
    private Integer pageSize;
    private transient Integer pagesize;

    /**
     * 排序语句 （兼容orderBy大小写）
     */
    private String orderBy;
    private transient String orderby;


    public Integer getPageNum(int def) {
        if (pageNum != null) {
            return pageNum;
        }
        if (pageNow != null) {
            return pageNow;
        }
        return def;
    }
    public Integer getPageNow(int def) {
        if (pageNow != null) {
            return pageNow;
        }
        if (pageNum != null) {
            return pageNum;
        }
        return def;
    }
    public Integer getPageNow() {
        if (pageNow != null) {
            return pageNow;
        }
        if (pageNum != null) {
            return pageNum;
        }
        return 1;
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
        if (pagesize != null) {
            return pagesize;
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
        if (pagesize != null) {
            return pagesize;
        }
        return null;
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
        if (orderby != null) {
            return orderby;
        }
        return null;
    }

}
