package org.nbone.persistence;

import java.util.HashSet;
import java.util.Set;

import org.nbone.mvc.domain.QueryCondition;
import org.nbone.persistence.criterion.QueryOperator;

/**
 * @author thinking
 * @version 1.0
 */
public class SqlOperation implements QueryOperator {

    public static Set<String> OPERATION_TYPE_SET = new HashSet<String>();
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 实体属性类型
     */
    private Class<?> propertyType;
    /**
     * 如果是where子句 请设置为true 否则为false
     */
    private boolean hasWhere = true;
    /**
     * String Type Default is  like/number Type Default  is =
     */
    private String operationType;

    private boolean between = false;

    private String beginRule = gt_eq;
    private String endRule = lt_eq;

    /**
     * specialValue 特殊值<li> in  object[] / List / String List 1,2,3,4,5
     * <li> between   object[]
     */
    private Object value;


    public SqlOperation(String fieldName) {
        this.fieldName = fieldName;
    }

    public SqlOperation(String fieldName, String operationType) {
        this.fieldName = fieldName;
        this.operationType = operationType;
    }

    public SqlOperation(String fieldName, Object beginValue, Object endValue) {
        this.fieldName = fieldName;
        this.value = new Object[]{beginValue, endValue};
        this.between = true;
    }

    public SqlOperation(String fieldName, Object[] values, boolean between) {
        this.fieldName = fieldName;
        this.value = values;
        this.between = between;
        if (!between) {
            this.operationType = in;
        }
    }

    public SqlOperation(String fieldName, Object[] values) {
        this.fieldName = fieldName;
        this.value = values;
        this.operationType = in;
    }

    public SqlOperation(String fieldName, String operationType, Object value) {
        this.fieldName = fieldName;
        this.operationType = operationType;
        this.value = value;
    }

    public SqlOperation(QueryCondition condition) {
        this(condition.getField(),condition.getRule(),condition.getValue());
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Class<?> propertyType) {
        this.propertyType = propertyType;
    }

    public boolean isHasWhere() {
        return hasWhere;
    }

    public void setHasWhere(boolean hasWhere) {
        this.hasWhere = hasWhere;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    /**
     * 是否属于 in 查询
     *
     * @return
     */
    public boolean isIn() {
        return SqlOperation.in.equalsIgnoreCase(operationType) || SqlOperation.not_in.equalsIgnoreCase(operationType);
    }

    public boolean isBetween() {
        return between;
    }

    public void setBetween(boolean between) {
        this.between = between;
    }

    public Object getBeginValue() {
        if (value != null && value.getClass().isArray()) {
            Object[] values = (Object[]) value;
            if (values.length != 2) {
                throw new IllegalArgumentException("sqlOperation.value array length != 2 , length=" + values.length);
            }
            return values[0];
        }
        return null;
    }

    public Object getEndValue() {
        if (value != null && value.getClass().isArray()) {
            Object[] values = (Object[]) value;
            if (values.length != 2) {
                throw new IllegalArgumentException("sqlOperation.value array length != 2 , length=" + values.length);
            }
            return values[1];
        }
        return null;
    }

    public String getBeginRule() {
        return beginRule;
    }

    public void setBeginRule(String beginRule) {
        this.beginRule = beginRule;
    }

    public String getEndRule() {
        return endRule;
    }

    public void setEndRule(String endRule) {
        this.endRule = endRule;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    //------------------------------------------------------------------
    @Override
    public String transfer(String s) {
        return null;
    }

    static {
        OPERATION_TYPE_SET.add(eq);
        OPERATION_TYPE_SET.add(not_eq);
        OPERATION_TYPE_SET.add(lt);
        OPERATION_TYPE_SET.add(gt);
        OPERATION_TYPE_SET.add(lt_eq);
        OPERATION_TYPE_SET.add(gt_eq);
    }

}
