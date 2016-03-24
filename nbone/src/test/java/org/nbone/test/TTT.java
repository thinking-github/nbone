package org.nbone.test;


public class TTT {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String[] toSmcsiteArray = new String[6]; 
	     System.out.println(toSmcsiteArray[0]);
	     
	     System.out.println(int.class.isPrimitive());
	     System.out.println(Parent.class.isAssignableFrom(Chinldren.class)); 
	     System.out.println(Chinldren.class.isAssignableFrom(Parent.class)); 
	     
	     System.out.println("==========================");
	     System.out.println(new Integer(0).getClass() == new Integer(21).getClass());
	     System.out.println(new Integer(0).getClass() == Integer.class);
	     
	     
	}

}
