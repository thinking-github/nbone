package com.google.common;

import java.util.List;

import com.google.common.reflect.TypeToken;

public class reflectTest {

	public static void main(String[] args) {
		
		TypeToken<String> ss = TypeToken.of(String.class);
		TypeToken<List> lists = TypeToken.of(List.class);
		System.out.println(ss.getRawType());

	}

}
