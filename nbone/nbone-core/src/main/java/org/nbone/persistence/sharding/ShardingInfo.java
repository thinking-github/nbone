package org.nbone.persistence.sharding;

/**
 *
 * @author thinking
 * @version 1.0
 * @since 2019-12-02
 */
public class ShardingInfo {

    //表示数据库实例的key。
    private String dbKey;

    //表示表的key。
    private String tableKey;


    public ShardingInfo(String dbKey, String tableKey) {
        this.dbKey = dbKey;
        this.tableKey = tableKey;
    }


    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    public String getTableKey() {
        return tableKey;
    }

    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }
}
