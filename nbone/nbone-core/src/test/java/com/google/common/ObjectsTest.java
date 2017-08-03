package com.google.common;

import org.nbone.test.domain.User;

import com.google.common.base.Objects;

public class ObjectsTest {

	public static void main(String[] args) {
		User user = new User();
		user.setId("001");
		user.setName("thinking");
		System.out.println(Objects.toStringHelper(user).add("33", 33).toString());
		

	}

}
