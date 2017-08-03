package org.nbone.demo.jodd;

import jodd.util.StringUtil;

public class StringUtilDemo {

	public static void main(String[] args) {
		
		String[] ss = {"1","2","3","4"};
		
		System.out.println(StringUtil.join(ss, ","));
		
		String demo = StringUtil.toCamelCase("user_info", true, '_');
		String demo1 = StringUtil.toCamelCase("USER_INFO", true, '_');
		System.out.println(demo);
		System.out.println(demo1);
		

	}

}
