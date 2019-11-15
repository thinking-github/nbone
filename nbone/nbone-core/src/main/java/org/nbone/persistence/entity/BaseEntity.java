package org.nbone.persistence.entity;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-11-14
 */
public class BaseEntity<T,Id extends Serializable> implements Serializable {

    private static final long serialVersionUID = -134067806668742646L;

    /**
     * 实体编号（唯一标识）
     */
    protected Id  id;

    protected String  remarks;	  // 备注

    @Column(name = "create_by")
    protected String  createBy;	  // 创建者
    @Column(name = "create_time")
    protected Date    createTime; // 创建日期

    @Column(name = "update_by")
    protected String  updateBy;	  // 更新者
    @Column(name = "update_time")
    protected Date    updateTime; // 更新日期

    protected Integer deleted; 	  // 删除标记（0：正常；1：删除）

    public BaseEntity(Id id) {
        this.id = id;
    }

    public BaseEntity() {

    }


    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
