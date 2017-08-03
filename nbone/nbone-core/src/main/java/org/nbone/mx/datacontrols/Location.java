package org.nbone.mx.datacontrols;

import java.io.Serializable;

/**
 * 
 * @author thinking
 * @since 2016-09-07
 * @version 1.0.2
 *
 */
public class Location  implements Serializable{
	
	private static final long serialVersionUID = 8048211580119539256L;
	
	private int  x;
	private int  y;
	private int  z;
	
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	
}
