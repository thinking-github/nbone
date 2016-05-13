package com.google.common.collect;

import java.util.List;

public class ListTest {

	public static void main(String[] args) {
		
		List<String> strings = Lists.newArrayList();
		List<Integer> ints = Lists.newArrayList(1);
		
		System.out.println(ints);
		
		Integer[] kk = {2,3,4};
		List<Integer> ss = Lists.asList(1,kk);
		
		System.out.println(ss);
		
		  
		  

	}
	

	  

}
