package org.nbone.demo.javabase.chapter1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019-04-23
 */
public class ListDemo {

    public static void main(String[] args) {
        listRemove();
        listForeachRemove();
    }

    /**
     * 循环删除元素问题
     */
    public static void listRemove() {
        List<Integer> list = new ArrayList<Integer>( Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        for (int i = 0; i < list.size(); i++) {
            if ((int) list.get(i) < 5) {
                list.remove(i);
            }

        }
        System.out.println(list);


        List<Integer> list1 = new ArrayList<Integer>( Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        for (int i = 0; i < list1.size(); i++) {
            if ((int) list1.get(i) < 5) {
                list1.remove(list1.get(i));
            }
        }
        System.out.println(list1);

    }

    /**
     * 循环删除元素问题
     * ConcurrentModificationException
     */
    public static void listForeachRemove() {
        List<Integer> list = new ArrayList<Integer>( Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        for (Integer value : list) {
            if ((int) value < 5) {
                list.remove(value);
            }

        }
        System.out.println(list);

    }
}
