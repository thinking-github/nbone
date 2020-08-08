package org.nbone.common.model;

import lombok.Data;


/**
 * @author thinking
 * @version 1.0
 * @since 2019/6/9
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
        return null;
    }


}
