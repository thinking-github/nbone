package org.nbone.demo.javabase.chapter1;

public class StringDemo {

	public static void main(String[] args) {
		//string mem  
		String str1 =  "hello";
		
		String str2 =  "hello";
		
		String str3 =  "he" + new String("llo");
		
		System.out.println(str1 == str2);
		
		System.out.println(str1 == str3);
		
		System.out.println(str1.equals(str3));
		
		
		// i+1 < i 
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Integer.MAX_VALUE+1);
		
		System.out.println(Integer.MAX_VALUE+1 < Integer.MAX_VALUE);

	}

}
