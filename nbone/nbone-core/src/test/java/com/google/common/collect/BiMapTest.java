package com.google.common.collect;

/**
 * 双向映射
 * @author thinking
 * @version 1.0 
 */
public class BiMapTest {

	public static void main(String[] args) {
		
		
		BiMap<String,Object> bimap = 	HashBiMap.create();
		
		bimap.put("001", "java");
		bimap.put("002", ".net");
		bimap.put("003", "C");
		bimap.put("004", "python");
		BiMap<Object,String> dd = bimap.inverse();
		System.out.println(bimap);
		System.out.println(dd);
	

	}

}
