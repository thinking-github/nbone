package org.nbone;

import java.io.Closeable;

public interface IO {
	
	public void refresh();
	
	public void flush();
	
	public void close();
	
	public void close(Closeable closeable);

}
