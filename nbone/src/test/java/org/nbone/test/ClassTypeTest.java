package org.nbone.test;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ClassTypeTest {
	
	public static void main(String[] args) {

		String[] toSmcsiteArray = new String[6]; 
	     System.out.println(toSmcsiteArray[0]);
	     
	     System.out.println(int.class.isPrimitive());
	     System.out.println(Parent.class.isAssignableFrom(Chinldren.class)); 
	     System.out.println(Chinldren.class.isAssignableFrom(Parent.class)); 
	     
	     System.out.println("==========================");
	     System.out.println(new Integer(0).getClass() == new Integer(21).getClass());
	     System.out.println(new Integer(0).getClass() == Integer.class);
	     
	     
	     
	 	System.out.println(Date.class.isPrimitive());
		System.out.println(Integer.class.isPrimitive());
		System.out.println(Locale.getDefault().toString());
		System.out.println(Calendar.class);
		Serializable chen = 1;
		System.out.println(chen.getClass());
	     
	     
	}

}
