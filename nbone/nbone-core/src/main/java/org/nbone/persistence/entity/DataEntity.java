package org.nbone.persistence.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-11-14
 */
public abstract class DataEntity<T> implements Serializable {

    private static final long serialVersionUID = 1549971235920921145L;

    @ApiModelProperty(value = "备注信息", readOnly = true)
    protected String remarks;     // 备注

    @ApiModelProperty(value = "创建者")
    @Column(name = "create_by")
    protected String createBy;     // 创建者

    @ApiModelProperty(value = "创建时间", readOnly = true)
    @Column(name = "create_date")
    protected Date createDate; // 创建日期

    @ApiModelProperty(value = "更新者")
    @Column(name = "update_by")
    protected String updateBy;     // 更新者

    @ApiModelProperty(value = "更新时间", readOnly = true)
    @Column(name = "update_date")
    protected Date updateDate; // 更新日期

    @ApiModelProperty(value = "删除标记", readOnly = true)
    protected String delFlag;     // 删除标记（0：正常；1：删除；2：审核）


}
