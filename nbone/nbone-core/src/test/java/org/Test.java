package org;
import static org.springframework.util.Assert.notNull;
public class Test {
	public  Test1 tt= new Test1();
	static{
		System.out.println("stzatic----------");
	}
	{System.out.println("------------------");}
	public static void main(String[] args) {
		//notNull(null);
		System.out.println(Test.class);
		Test ll = new Test();

	}

}

class Test1{
	Test1(){
		System.out.println("Test1-------------");
	}
}
