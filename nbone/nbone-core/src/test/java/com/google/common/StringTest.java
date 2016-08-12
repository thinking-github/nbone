package com.google.common;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class StringTest {

	public static void main(String[] args) {
		System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "CONSTANT_NAME"));
		System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "CONSTANT_NAME"));
		
		System.out.println(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "ConstantName"));
		System.out.println(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, "constantName"));
		
		HashFunction hf = Hashing.md5();

		System.out.println(hf);
		System.out.println("-----------------------");
		System.out.println(CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE).convert("ConstantName"));;
		
	
		System.out.println(CharMatcher.JAVA_UPPER_CASE.trimFrom("UUUkkk"));
		System.out.println(CharMatcher.JAVA_UPPER_CASE.removeFrom("UUUkkk"));
		System.out.println(CharMatcher.JAVA_UPPER_CASE.retainFrom("UUUkkk"));
		System.out.println(CharMatcher.JAVA_UPPER_CASE.replaceFrom("UUUkkk","u"));
		
		List<String> ss = Arrays.asList("chen","yi","cheng",null);
		String[] strings = {"1","2","3","4","5"};
		String join = Joiner.on(",").skipNulls().join(ss);
		System.out.println(join);
		System.out.println(Joiner.on("_").join(strings));
		
		Iterable<String> it  = Splitter.on(",").split(join);
		List<String> list = Splitter.on(",").splitToList(join);
		
		System.out.println(it);
		System.out.println(list);
		
		
	}

}
