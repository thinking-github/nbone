package org.nbone.persistence.entity;

/**
 * 实现动态表名时，实体类需要实现该接口
 *
 * @author thinking
 * @version 1.0
 * @since 2019-11-14
 */
public interface DynamicTableName {


    /**
     * 获取动态表名 - 只要有返回值，不是null和''，就会用返回值作为表名
     *
     * @return
     */
    String getTableName();

}
