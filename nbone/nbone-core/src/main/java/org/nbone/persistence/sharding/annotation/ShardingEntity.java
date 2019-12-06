package org.nbone.persistence.sharding.annotation;

import org.nbone.persistence.sharding.strategy.HashShardingStrategy;
import org.nbone.persistence.sharding.strategy.ShardingStrategy;
import org.nbone.persistence.sharding.strategy.ShardingType;

import java.lang.annotation.*;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-12-02
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardingEntity {

    /**
     * 逻辑表名。
     */
    String logicName();

    /**
     * 分表的属性名称列表,按照数组中的顺序向后连接表名,只作为简单拼接 如: user_coupon_${appId}
     * <p>注意不是数据库字段。
     */
    String[] joinName() default {};

    /**
     * 分表的属性名称。需做分片运算字段  如: user_coupon_${appId}_#{10%8}
     * <p>注意不是数据库字段。
     */
    String shardingName() default "";

    /**
     * 分表类型。
     */
    ShardingType shardingType() default ShardingType.HASH;

    /**
     * 分表策略。
     */
    Class<? extends ShardingStrategy> shardingStrategy() default HashShardingStrategy.class;

    /**
     * 物理表的分隔符。
     * <p>一般比如：逻辑表为user,实际物理表为user_0001
     */
    String delimiter() default "_";


}
