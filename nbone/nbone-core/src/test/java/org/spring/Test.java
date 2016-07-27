package org.spring;

import org.apache.commons.lang3.JavaVersion;
import org.hibernate.Version;
import org.springframework.core.JdkVersion;
import org.springframework.core.SpringVersion;

public class Test {

	public static void main(String[] args) {
		
		System.out.println(JdkVersion.getJavaVersion());
		System.out.println(JavaVersion.JAVA_1_8);
		System.out.println(SpringVersion.getVersion());
		System.out.println(Version.getVersionString().startsWith("3"));

	}

}
