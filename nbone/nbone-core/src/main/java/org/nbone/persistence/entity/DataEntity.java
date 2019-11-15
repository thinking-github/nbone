package org.nbone.persistence.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author thinking
 * @version 1.0
 * @since 2019-11-14
 */
public abstract class DataEntity<T> implements Serializable {

    private static final long serialVersionUID = 1549971235920921145L;

    protected String remarks;	 // 备注
    protected String createBy;	 // 创建者
    protected Date   createDate; // 创建日期
    protected String updateBy;	 // 更新者
    protected Date   updateDate; // 更新日期
    protected String delFlag; 	 // 删除标记（0：正常；1：删除；2：审核）



}
