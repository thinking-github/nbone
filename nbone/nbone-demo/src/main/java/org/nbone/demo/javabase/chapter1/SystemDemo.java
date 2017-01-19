package org.nbone.demo.javabase.chapter1;

import java.io.File;
import java.net.MalformedURLException;

public class SystemDemo {
	
	
	public static void main(String[] args)  {
		try {
			String userHome = System.getProperty("user.home");
			System.out.println(userHome);
			java.net.URL url = new File(userHome).toURI().toURL();
			System.out.println(url);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
