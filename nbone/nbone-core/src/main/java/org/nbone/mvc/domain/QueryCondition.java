package org.nbone.mvc.domain;

import java.io.Serializable;

/**
 * 自定义查询操作类型  id > 20 , name = 'thinking'
 *
 * @author thinking
 * @version 1.0
 * @since 2019-09-04
 */
@SuppressWarnings("unused")
public class QueryCondition implements Serializable {

    private static final long serialVersionUID = 7028900637927153064L;
    /**
     * 字段名称
     */
    private String field;
    /**
     * type
     */
    private String type;
    /**
     * 操作符号规则  = > < ...
     */
    private String rule = "=";
    /**
     * 属性值
     */
    private Object value;


    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (field == null || "".equals(field)) {
            return "";
        }
        sb.append(this.field).append(" ").append(this.rule).append(" ").append(this.type).append(" ").append(this.value);
        return sb.toString();
    }
}
