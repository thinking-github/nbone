package org.nbone.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author thinking
 * @version 1.0
 * @since 2020-08-06
 */
public class Stream8Test {

    public static void main(String args[]) {
        List names = new ArrayList();

        names.add("Google");
        names.add("Runoob");
        names.add("Taobao");
        names.add("Baidu");
        names.add("Sina");

        names.forEach(System.out::println);
    }

}
