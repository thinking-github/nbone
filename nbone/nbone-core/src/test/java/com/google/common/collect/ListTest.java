package com.google.common.collect;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.common.base.Function;

public class ListTest {

	public static void main(String[] args) {
		
		List<String> strings = Lists.newArrayList();
		List<Integer> ints = Lists.newArrayList(1);
		
		System.out.println(ints);
		
		Integer[] kk = {2,3,4};
		List<Integer> ss = Lists.asList(1,kk);
		
		System.out.println(ss);
		
		  
		  

	}
	
	@Test
	public  void  testTransform(){
		List<String> arrrs = Arrays.asList("1             ","2 ","3","3");
		System.out.println(arrrs);
		Collection<String> sets = Collections2.transform(arrrs, new Function<String,String>() {
			@Override
			public String apply(String input) {
				return StringUtils.trim(input);
			}
		});
		
		
		List<String> lists = Lists.transform(arrrs, new Function<String,String>() {
			@Override
			public String apply(String input) {
				String temp = StringUtils.trim(input);
				return temp;
			}
		});
		
		final Set<String> setnew = new HashSet<String>(lists);
		System.out.println(sets);
		System.out.println(setnew);
		
		String ss = org.springframework.util.StringUtils.collectionToDelimitedString(setnew, "|");
		
		System.out.println(ss);
		
		
	}
	
	

	  

}
