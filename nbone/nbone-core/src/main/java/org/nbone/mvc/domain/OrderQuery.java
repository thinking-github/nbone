package org.nbone.mvc.domain;

import org.nbone.constants.CaseName;
import org.nbone.core.util.NameFormat;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.domain.Sort.Order;

/**
 * SortUtils
 *
 * @author thinking
 * @version 1.0
 * @since 2019-01-08
 */
@SuppressWarnings("unused")
public interface OrderQuery {

    /**
     * order by id ASC
     */
    Order ID_ASC = new Order(Direction.ASC, "id");
    /**
     * order by id DESC
     */
    Order ID_DESC = new Order(Direction.DESC, "id");

    /**
     * order by createTime ASC
     */
    Order CREATE_TIME_ASC = new Order(Direction.ASC, "createTime");
    /**
     * order by createTime DESC
     */
    Order CREATE_TIME_DESC = new Order(Direction.DESC, "createTime");

    /**
     * order by createTime ASC
     */
    Order UPDATE_TIME_ASC = new Order(Direction.ASC, "updateTime");
    /**
     * order by createTime DESC
     */
    Order UPDATE_TIME_DESC = new Order(Direction.DESC, "updateTime");


    public static String orderBy(Sort sort, CaseName caseName) {
        return orderBy(sort, caseName, null);
    }


    public static String orderBy(Sort sort, CaseName caseName, String prefix) {
        if (sort == null) {
            return null;
        }

        prefix = StringUtils.isEmpty(prefix) ? "" : prefix + ".";

        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Sort.Order order : sort) {
            if (index > 0) {
                builder.append(", ");
            }
            String columnName = NameFormat.caseFormat(order.getProperty(), caseName);
            builder.append(prefix).append(columnName).append(" ").append(order.getDirection().name());
            index++;
        }
        return builder.toString();
    }

}
