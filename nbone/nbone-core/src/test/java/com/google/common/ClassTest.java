package com.google.common;

import java.io.IOException;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class ClassTest {

	public static void main(String[] args) {
		
		try {
			ClassPath  classPath = ClassPath.from(ClassTest.class.getClassLoader());
			System.out.println(classPath.getResources());
			
			for (ClassInfo classInfo : classPath.getTopLevelClasses("java")) {
				System.out.println(classInfo.getName());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
