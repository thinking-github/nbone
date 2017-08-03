package org.nbone.mvc.model;

import java.io.Serializable;

/**
 * 
 * @author thinking
 * @since 2016-04-03
 *
 */
public class Range implements Serializable{
	
	private static final long serialVersionUID = 3178641967711436300L;

	private int min;
	
	private int max;

	
	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	
	

}
