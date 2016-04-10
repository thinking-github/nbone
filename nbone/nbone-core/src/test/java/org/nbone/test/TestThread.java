package org.nbone.test;


public class TestThread extends Thread {

	int count =0;
	@Override
	public void run() {
		System.out.println(++count);
		
	}
	
	
	

}
