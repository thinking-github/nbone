package org.nbone.persistence.entity;

import java.io.Serializable;

/**
 * 实现动态表名时，实体类需要实现该接口
 *
 * @author thinking
 * @version 1.0
 * @since 2019-11-14
 */
public interface DynamicTableName {

    String TABLE_NAME_KEY = "dynamicTableName";

    /**
     * 获取动态表名 - 只要有返回值，不是null和''，就会用返回值作为表名
     *
     * @return
     */
    String getTableName();

    void setTableName(String tableName);


    /**
     * 计算分片表名称
     *
     * @param baseName
     * @param first
     * @return
     */
    static String shardingTableName(String baseName, Serializable first) {
        return baseName + "_" + first;
    }

    static String shardingTableName(String baseName, Serializable first, Serializable second) {
        return baseName + "_" + first + "_" + second;
    }

    static String shardingTableName(String baseName, Serializable first, Serializable second, Serializable third) {
        return baseName + "_" + first + "_" + second + "_" + third;
    }

}
