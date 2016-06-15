package org.nbone.framework.mybatis.generator.plugins;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * @author thinking
 * @since 2016-06-15
 */
@Entity
@Table(name="ts_project")
public class TsProjectDTO implements Serializable {
    /**
     * 数据库字段 ts_project.id id
     */
    @Id
    @GeneratedValue(generator="id")
    @Column(name="id")
    private Long id;

    /**
     * 数据库字段 ts_project.type_id 项目类型ID
     */
    @Column(name="type_id")
    private Integer typeId;

    /**
     * 数据库字段 ts_project.name 服务项目名称
     */
    @Column(name="name")
    private String name;

    /**
     * 数据库字段 ts_project.code 服务项目编码
     */
    @Column(name="code")
    private String code;

    /**
     * 数据库字段 ts_project.sort 服务项目排序
     */
    @Column(name="sort")
    private Integer sort;

    /**
     * 数据库字段 ts_project.is_enable 是否启用
     */
    @Column(name="is_enable")
    private Boolean isEnable;

    /**
     * 数据库字段 ts_project.is_open 是否展开
     */
    @Column(name="is_open")
    private Boolean isOpen;

    /**
     * 数据库字段 ts_project.create_dt 创建时间
     */
    @Column(name="create_dt")
    private Date createDt;

    /**
     * 数据库字段 ts_project.create_by 创建者
     */
    @Column(name="create_by")
    private Long createBy;

    /**
     * 数据库字段 ts_project.modify_dt 修改时间
     */
    @Column(name="modify_dt")
    private Date modifyDt;

    /**
     * 数据库字段 ts_project.modify_by 修改者
     */
    @Column(name="modify_by")
    private Long modifyBy;

    /**
     * 数据库字段 ts_project.del_flag del_flag(0:正常,1:删除)
     */
    @Column(name="del_flag")
    private Boolean delFlag;

    /**
     * 数据库字段 ts_project.remark 备注
     */
    @Column(name="remark")
    private String remark;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getModifyDt() {
        return modifyDt;
    }

    public void setModifyDt(Date modifyDt) {
        this.modifyDt = modifyDt;
    }

    public Long getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(Long modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}