package org.nbone.persistence;

import java.util.*;

import org.nbone.persistence.criterion.QueryOperator;

/**
 * @author thinking
 * @version 1.0
 */
public class SqlOperations {

    private Map<String, SqlOperation> sqlOperation = new HashMap<String, SqlOperation>();

    private List<SqlOperationRange> sqlOperationRanges;


    public SqlOperations() {
    }

    public SqlOperations(String fieldName, String operationType) {
        this.addOperation(fieldName, operationType);
    }

    /**
     * @param fieldName
     * @param operationType
     * @return
     */
    public SqlOperations addOperation(String fieldName, String operationType) {
        return addOperation(fieldName,operationType,null);
    }

    public SqlOperations addOperation(String fieldName, String operationType,Object value) {
        SqlOperation sqlOperation = new SqlOperation(fieldName, operationType,value);
        this.sqlOperation.put(fieldName, sqlOperation);
        return this;
    }

    public SqlOperations addOperationBetween(String fieldName, Object beginValue, Object endValue) {
        SqlOperation sqlOperation = new SqlOperation(fieldName, beginValue, endValue);
        this.sqlOperation.put(fieldName, sqlOperation);
        return this;
    }

    public SqlOperations addOperationRange(String leftName, String rightName, Object value) {
        SqlOperationRange sqlOperation = new SqlOperationRange(leftName, rightName, value);
        if (sqlOperationRanges == null) {
            sqlOperationRanges = new ArrayList<SqlOperationRange>();
        }
        sqlOperationRanges.add(sqlOperation);
        return this;
    }

    public SqlOperations addOperationBetween(String fieldName, Object[] values) {
        SqlOperation sqlOperation = new SqlOperation(fieldName, values, true);
        this.sqlOperation.put(fieldName, sqlOperation);
        return this;
    }

    public SqlOperations addOperationIn(String fieldName, boolean isIn, Object values) {
        SqlOperation sqlOperation = new SqlOperation(fieldName);
        sqlOperation.setValue(values);
        if (isIn) {
            sqlOperation.setOperationType(QueryOperator.in);
        } else {
            sqlOperation.setOperationType(QueryOperator.not_in);
        }
        this.sqlOperation.put(fieldName, sqlOperation);
        return this;
    }


    public SqlOperations addOperation(SqlOperation sqlOperation) {
        if (sqlOperation == null) {
            return this;
        }
        String key = sqlOperation.getFieldName();
        this.sqlOperation.put(key, sqlOperation);
        return this;
    }


    public void addAll(SqlOperation[] sqlOperations) {
        if (sqlOperations == null) {
            return;
        }
        for (int i = 0; i < sqlOperations.length; i++) {
            addOperation(sqlOperations[i]);
        }
    }


    public void addAll(Map<String, SqlOperation> sqlOperationMap) {
        if (sqlOperationMap == null) {
            return;
        }
        this.sqlOperation.putAll(sqlOperationMap);
    }

    public void addMapString(Map<String, String> sqlOperationMap) {
        if (sqlOperationMap == null) {
            return;
        }
        for (Map.Entry<String, String> entry : sqlOperationMap.entrySet()) {
            addOperation(entry.getKey(), entry.getValue());
        }
    }

    public void remove(SqlOperation sqlOperation) {
        if (sqlOperation == null) {
            return;
        }
        String key = sqlOperation.getFieldName();
        this.sqlOperation.remove(key);
    }

    public Map<String, SqlOperation> getSqlOperationAsMap() {
        return sqlOperation;
    }
    public Collection<SqlOperation> getSqlOperations() {
        return sqlOperation.values();
    }

    public List<SqlOperationRange> getSqlOperationRanges() {
        return sqlOperationRanges;
    }

    public SqlOperation getSqlOperation(String fieldName) {
        if (sqlOperation.isEmpty()) {
            return null;
        }
        return sqlOperation.get(fieldName);
    }


}
