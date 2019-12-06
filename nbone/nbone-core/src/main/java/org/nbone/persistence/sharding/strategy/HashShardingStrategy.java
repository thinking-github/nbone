package org.nbone.persistence.sharding.strategy;

import org.nbone.persistence.sharding.ShardingContext;
import org.nbone.persistence.sharding.ShardingInfo;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-12-02
 */
public class HashShardingStrategy implements ShardingStrategy {


    @Override
    public <V> ShardingInfo sharding(ShardingContext shardingContext, V shardingValue, int dbTotal, int tableTotal) {
        if (shardingValue == null) {
            throw new IllegalArgumentException("invalid shardingColumnValue:" + shardingValue);
        }
        if (dbTotal <= 0) {
            throw new IllegalArgumentException("invalid dbTotal:" + dbTotal);
        }
        if (tableTotal <= 0) {
            throw new IllegalArgumentException("invalid tableTotal:" + tableTotal);
        }
        if (tableTotal < dbTotal) {
            throw new IllegalArgumentException("tableTotal[" + tableTotal + "] can't less than dbTotal[" + dbTotal + "]");
        }
        long hash = 0;
        Class<?> shardingClass = shardingValue.getClass();
        if (Long.class.isAssignableFrom(shardingClass) || long.class.isAssignableFrom(shardingClass)) {
            hash = (Long) shardingValue;

        } else if (Integer.class.isAssignableFrom(shardingClass) || int.class.isAssignableFrom(shardingClass)) {
            hash = (Integer) shardingValue;
        } else {
            hash = shardingValue.toString().hashCode();
            if (hash < 0) {
                hash = Math.abs(hash);
            }
        }

        int tableNo = (int) (hash % tableTotal);
        int dbNo = tableNo / (tableTotal / dbTotal);
        //表名后缀宽度默认为4
        String tableKey = String.format("%04d", tableNo);
        String dbKey = String.format("%d", dbNo);
        return new ShardingInfo(dbKey, tableKey);
    }

}
