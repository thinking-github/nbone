package org.nbone.demo.javabase.chapter1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringDemo {

	public static void main(String[] args) {
		//string mem  
		String str1 =  "hello";
		
		String str2 =  "hello";
		
		String str3 =  "he" + new String("llo");
		
		System.out.println(str1 == str2);
		
		System.out.println(str1 == str3);
		
		System.out.println(str1.equals(str3));

		// 数据传递
		//String str = "1234";
		String str = new String("123456");
		changStr(str);
		System.out.println(str);

		Integer num = new Integer(1);
		changInt(num);
		System.out.println(num);

		List list =  new ArrayList(Arrays.asList(1,2,3,4,5));
		changList(list);
		System.out.println(list);

		
		// i+1 < i 
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Integer.MAX_VALUE+1);
		
		System.out.println(Integer.MAX_VALUE+1 < Integer.MAX_VALUE);

	}

	public static void changStr(String str){
		str="welcome";
	}

	public static void changInt(Integer integer){
		integer= 10;
	}
	public static void changList(List list){
		list =  new ArrayList(Arrays.asList(9,8,7,6,5));
		System.out.println(list);
	}

}
