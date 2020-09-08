package org.nbone.persistence.entity;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-11-14
 */
public interface TypeAttribute<T> {

    String TYPE_KEY = "type";

    /**
     * 获取type参数
     *
     * @return
     */
    T getType();

    /**
     * 设置type 参数
     *
     * @param type
     */
    void setType(T type);

}
