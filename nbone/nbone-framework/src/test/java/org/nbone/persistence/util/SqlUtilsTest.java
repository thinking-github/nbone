package org.nbone.persistence.util;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author thinking
 * @version 1.0
 * @since 2019-12-25
 */
public class SqlUtilsTest {

    public static void main(String[] args) {
        String ss = "kk,oo";
        String ss4int = "1,2,3";
        System.out.println(SqlUtils.stringSplit2In("user.id", ss, String.class));
        System.out.println(SqlUtils.stringSplit2In("user.id", ss4int, Integer.class));

        System.out.println(SqlUtils.stringSplit2Notin("user.id", ss4int, String.class));
        System.out.println(SqlUtils.stringSplit2Notin("user.id", ss4int, Integer.class));

        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder sql = SqlUtils.list2NamedIn("id", true, new Object[]{1, 2, 3}, map);

        System.out.println(sql);

        System.out.println(int.class);
        System.out.println(Integer.class);

    }


}
