package org.nbone.persistence.sharding;

import org.nbone.persistence.sharding.strategy.ShardingStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-12-02
 */
public class ShardingEntityInfo implements Serializable {

    private static final long serialVersionUID = 6975055638838323118L;

    /**
     * 逻辑表名。
     */
    private String logicName;

    /**
     * 分表的属性名称列表,按照数组中的顺序向后连接表名,只作为简单拼接 如: user_coupon_${appId}
     * <p>注意不是数据库字段。
     */
    private String[] joinName;

    /**
     * 分表的属性名称。需做分片运算字段  如: user_coupon_${appId}_#{10%8}
     * <p>注意不是数据库字段。
     */
    private String shardingName;

    /**
     * 分库总数。
     */
    private int dbTotal = 1;

    /**
     * 分表总数。
     */
    private int tableTotal = 1;
    /**
     * 分表策略。
     */
    private ShardingStrategy shardingStrategy;

    /**
     * 物理表的分隔符。
     * <p>一般比如：逻辑表为user,实际物理表为user_0001
     */
    private String delimiter = "_";

    public ShardingEntityInfo(String logicName, String[] joinName, String shardingName) {
        this.logicName = logicName;
        this.joinName = joinName;
        this.shardingName = shardingName;
    }

    public ShardingEntityInfo(String logicName, String[] joinName, String shardingName, String delimiter) {
        this.logicName = logicName;
        this.joinName = joinName;
        this.shardingName = shardingName;
        this.delimiter = delimiter;
    }

    public String getTableName(Object object) {
        String tableName = logicName;
        if (!ObjectUtils.isEmpty(joinName)) {
            for (String propertyName : joinName) {
                PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(object.getClass(), propertyName);
                Object fieldValue = ReflectionUtils.invokeMethod(descriptor.getReadMethod(), object);
                Assert.notNull(fieldValue, "class " + object.getClass() + " property " + propertyName + " must not be null.");
                tableName = tableName + this.delimiter + fieldValue;
            }
        }
        if (StringUtils.hasLength(shardingName)) {
            PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(object.getClass(), shardingName);
            Object fieldValue = ReflectionUtils.invokeMethod(descriptor.getReadMethod(), object);
            Assert.notNull(fieldValue, "class " + object.getClass() + " property " + shardingName + " must not be null.");
            ShardingInfo shardingInfo = shardingStrategy.sharding(null, fieldValue, dbTotal, tableTotal);
            tableName = tableName + this.delimiter + shardingInfo.getTableKey();
        }
        return tableName;
    }

    public String getLogicName() {
        return logicName;
    }

    public void setLogicName(String logicName) {
        this.logicName = logicName;
    }

    public String[] getJoinName() {
        return joinName;
    }

    public void setJoinName(String[] joinName) {
        this.joinName = joinName;
    }

    public int getDbTotal() {
        return dbTotal;
    }

    public void setDbTotal(int dbTotal) {
        this.dbTotal = dbTotal;
    }

    public int getTableTotal() {
        return tableTotal;
    }

    public void setTableTotal(int tableTotal) {
        this.tableTotal = tableTotal;
    }

    public ShardingStrategy getShardingStrategy() {
        return shardingStrategy;
    }

    public void setShardingStrategy(ShardingStrategy shardingStrategy) {
        this.shardingStrategy = shardingStrategy;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
