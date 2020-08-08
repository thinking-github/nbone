package org.nbone.demo.common.domain;

import lombok.Data;
import org.nbone.persistence.annotation.MappedBy;
import org.nbone.persistence.enums.QueryType;

import java.util.Date;
import java.util.List;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-01-09
 */
@Data
public class UserQuery {

    @MappedBy(name = "id", queryType = QueryType.IN)
    private List<String> ids;
    private String name;
    private int age;
    private Integer deleted;

    @MappedBy(name = "createTime", queryType = QueryType.BETWEEN)
    private Date[] createTimes;

}
