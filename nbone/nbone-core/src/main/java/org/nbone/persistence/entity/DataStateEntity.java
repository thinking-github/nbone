package org.nbone.persistence.entity;

import java.io.Serializable;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-11-15
 */
public abstract class DataStateEntity<T, Id extends Serializable> extends BaseEntity<T, Id>
        implements Serializable {

    private static final long serialVersionUID = -4416407610435188430L;
    /**
     * 实体状态
     */
    protected Integer status;

    public DataStateEntity(Id id, Integer status) {
        super(id);
        this.status = status;
    }

    public DataStateEntity(Integer status) {
        this.status = status;
    }

    public DataStateEntity() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
