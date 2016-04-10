package org.nbone.test;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nbone.util.BeanMapUtils;
import org.nbone.util.json.JSONOperUtils;
import org.nbone.web.support.WebResultWrapper;
import org.springframework.util.StringUtils;



public class TestMain {
	public static void main(String[] args) {
		String wrapData=JSONOperUtils.pojoToJSON(WebResultWrapper.failedResultWraped("00"));
		System.out.println(wrapData);
		
		Set<String> set = new HashSet<String> ();
		set.add("ll");
		set.add("ll");
		set.add("llpp");
		
		System.out.println(StringUtils.collectionToDelimitedString(set, ",", "'", "'"));
		
		System.out.println(NumberFormat.getNumberInstance().format(3.88999999999999));
		System.out.println();
		List list = new ArrayList();
		list.add(new UserTest());
		list.add("chen22");
		Map map =BeanMapUtils.populateMap(WebResultWrapper.failedResultWraped("00"));
		Map map1 =BeanMapUtils.populateMap(WebResultWrapper.successResultWraped(list));
		
		System.out.println(map);
		System.out.println(map1);
		
		WebResultWrapper wrap = new WebResultWrapper();
		
		BeanMapUtils.populateBean(wrap, map1);
		
		System.out.println(wrap);
		
		System.out.println(TestMain.class.getPackage());
		
		System.out.println(-(5.0/4));
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("------------");
		
		new TestThread();
	    for (int i = 0; i < 10; i++) {
	    	TestThread ll =	new TestThread();
			ll.start();
		}
		System.out.println("===========================");
		TestRunnable run = new TestRunnable();
		for (int i = 0; i < 10; i++) {
			Thread t1 = new Thread(run);
			t1.start();
		}
			
		
	}
	
	
	

}
