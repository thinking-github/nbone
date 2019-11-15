package org.nbone.persistence.annotation;

import org.nbone.persistence.enums.QueryType;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-28
 */
public class QueryOperation {

    private String name;

    private Class<?> fieldType;

    private QueryType queryType;

    public QueryOperation(String name, Class<?> fieldType, QueryType queryType) {
        this.name = name;
        this.fieldType = fieldType;
        this.queryType = queryType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }
}
