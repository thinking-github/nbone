package org.nbone.persistence.enums;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019/6/19
 */
public enum QueryType {

    EQ("="),
    NE("!="),
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<="),
    IN("IN"),
    NOT_IN("NOT IN"),

    BETWEEN("BETWEEN"),
    NOT_BETWEEN("NOT BETWEEN"),

    LIKE("LIKE", "%", "%"),
    LEFT_LIKE("LIKE", "%", ""),
    RIGHT_LIKE("LIKE", "", "%"),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),

    // 强制条件，不管值是不是空字符串都加载这个查询条件
    EQ_FORCE("=", true),
    NE_FORCE("!=", true),
    ;

    private final String operation;
    private  String valuePrefix;
    private  String valueSuffux;
    private  Boolean isForce;

    QueryType(String operation) {
        this.operation = operation;
    }

    QueryType(String operation, String valuePrefix, String valueSuffux) {
        this.operation = operation;
        this.valuePrefix = valuePrefix;
        this.valueSuffux = valueSuffux;
    }

    QueryType(String operation, String valuePrefix, String valueSuffux, Boolean isForce) {
        this.operation = operation;
        this.valuePrefix = valuePrefix;
        this.valueSuffux = valueSuffux;
        this.isForce = isForce;
    }

    QueryType(String operation, Boolean isForce) {
        this.operation = operation;
        this.isForce = isForce;
    }

    public String getOperation() {
        return operation;
    }

    public String getValuePrefix() {
        return valuePrefix;
    }

    public void setValuePrefix(String valuePrefix) {
        this.valuePrefix = valuePrefix;
    }

    public String getValueSuffux() {
        return valueSuffux;
    }

    public void setValueSuffux(String valueSuffux) {
        this.valueSuffux = valueSuffux;
    }

    public Boolean getForce() {
        return isForce;
    }

    public void setForce(Boolean force) {
        isForce = force;
    }

    static {
        QueryType[] QueryTypes = new QueryType[QueryType.values().length];
        boolean var10002 = true;
        for (QueryType value : QueryType.values()) {
            QueryTypes[value.ordinal()] = value;
        }
    }
}
