package org.nbone.mvc.domain;

import java.io.Serializable;

/**
 * @author thinking
 * @version 1.0
 * @since 2018-12-25
 */
public interface ParentDomain<T extends Serializable> {

    /**
     * 上级标识
     *
     * @return
     */
    public T getParentId();

    /**
     * 获取上级名称
     *
     * @return
     */
    public String getParentName();

    /**
     * 设置上级名称
     *
     * @param name
     */
    public void setParentName(String name);

}
