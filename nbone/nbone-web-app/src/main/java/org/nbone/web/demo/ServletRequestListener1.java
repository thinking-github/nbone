package org.nbone.web.demo;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletRequestListener1 implements ServletRequestListener {

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		System.out.println("-------------ServletRequestListener1--------");

	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		System.out.println("---------ServletRequestListener1------------");

	}

}
