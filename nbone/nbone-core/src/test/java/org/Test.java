package org;

public class Test {
	public  Test1 tt= new Test1();
	static{
		System.out.println("stzatic----------");
	}
	{System.out.println("------------------");}
	public static void main(String[] args) {
		//notNull(null);
		
		String[] ss = {};
		System.out.println(ss.length);
		System.out.println(ss);

	}

}

class Test1{
	Test1(){
		System.out.println("Test1-------------");
	}
}
