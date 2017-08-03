package org.nbone.test;

public class TestRunnable implements Runnable {
    int count = 0;
	public void run() {
            System.out.println(++count);
	}

}
