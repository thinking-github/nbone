package org.nbone.framework.spring.web.mvc;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.mvc.UrlFilenameViewController;

public class UrlFileNameViewController extends UrlFilenameViewController implements Ordered {

	private int order = Ordered.LOWEST_PRECEDENCE;
	
	
	
	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return order;
	}

}
