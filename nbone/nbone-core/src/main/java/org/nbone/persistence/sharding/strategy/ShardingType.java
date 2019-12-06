package org.nbone.persistence.sharding.strategy;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-12-02
 */
public enum ShardingType {

    /**
     * 区间策略，通过分表字段区间存储。
     */
    RANGE,

    /**
     * 默认策略，通过分表字段和总库表做Hash散列。
     */
    HASH;

}
