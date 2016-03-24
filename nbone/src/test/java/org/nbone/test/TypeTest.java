package org.nbone.test;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TypeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println(Date.class.isPrimitive());
		System.out.println(Integer.class.isPrimitive());
		System.out.println(Locale.getDefault().toString());
		System.out.println(Calendar.class);
		

	}

}
