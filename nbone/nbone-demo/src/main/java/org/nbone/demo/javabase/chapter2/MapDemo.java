package org.nbone.demo.javabase.chapter2;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2019-04-10
 */
public class MapDemo {

    public static void main(String[] args) {

        treeMapTest();
    }


    public static void  treeMapTest(){

        TreeMap<Integer,String> treeMap = new TreeMap();
        treeMap.put(1,"java");

        treeMap.put(9,"ruby");
        treeMap.put(2,"C");
        treeMap.put(3,"C++");
        treeMap.put(4,"C#");
        treeMap.put(5,"python");
        treeMap.put(6,"php");
        treeMap.put(7,"go");
        treeMap.put(8,"nodejs");



        for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
            System.out.println("key:" + entry.getKey() +" value:" + entry.getValue());

        }

    }
}
