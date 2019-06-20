package org.nbone.common.model;

import lombok.Data;

@Data
public class PageRequet {

    /**
     * 起始页 @see pageNum.（他们含义一样用于兼容）
     */
    private Integer pageNow;
    /**
     * 起始页 @see pageNow
     */
    private Integer pageNum;

    /**
     * 单页的大小
     */
    private Integer pageSize;

    private String orderBy;


    public Integer getPageNum(int def) {
        if(pageNum == null){
            return def;
        }
        return pageNum;
    }
    public Integer getPageNow(int def) {
        if(pageNow == null){
            return def;
        }
        return pageNow;
    }


    public Integer getPageSize(int def) {
        if(pageSize == null){
            return def;
        }
        return pageSize;
    }
}
