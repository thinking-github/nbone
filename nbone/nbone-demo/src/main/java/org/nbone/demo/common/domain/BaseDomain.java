package org.nbone.demo.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019/6/27
 */
@Data
public class BaseDomain implements Serializable {

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private Date updateBy;


}
