package org.nbone.demo.jodd;

import java.io.File;
import java.io.IOException;

import jodd.io.watch.DirWatcher;
import jodd.io.watch.DirWatcherListener;

public class DirWatcherDemo {

	public static void main(String[] args) throws IOException {
		
		DirWatcher dirWatcher = new DirWatcher("F:/what")
	            .monitor("*.txt")
	            //.startBlank(true)
	            .useWatchFile("watch.txt");
		
		
		  dirWatcher.register(new DirWatcherListener() {
		        public void onChange(File file, DirWatcher.Event event) {
		            System.out.println(file.getName() + ":" + event.name());
		        }
		    });
		
		dirWatcher.start(1000);
		
		System.in.read();// 按任意键退出

	}

}
