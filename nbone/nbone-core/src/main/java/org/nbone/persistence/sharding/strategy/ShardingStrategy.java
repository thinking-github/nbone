package org.nbone.persistence.sharding.strategy;

import org.nbone.persistence.sharding.ShardingContext;
import org.nbone.persistence.sharding.ShardingInfo;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-12-02
 */
public interface ShardingStrategy {


    <V> ShardingInfo sharding(ShardingContext shardingContext, V shardingValue, int dbTotal, int tableTotal);
}
