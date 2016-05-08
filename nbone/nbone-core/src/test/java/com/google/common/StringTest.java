package com.google.common;

import com.google.common.base.CaseFormat;
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
		
		
	}

}
