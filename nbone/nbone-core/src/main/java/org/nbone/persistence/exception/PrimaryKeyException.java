package org.nbone.persistence.exception;

import javax.persistence.PersistenceException;

/**
 * 
 * @author thinking
 * @version 1.0 
 */
public class PrimaryKeyException extends PersistenceException{

	
	private static final long serialVersionUID = 3017927819429970076L;

	
	public PrimaryKeyException() {
		super();
	}

	public PrimaryKeyException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrimaryKeyException(String message) {
		super(message);
	}

	public PrimaryKeyException(Throwable cause) {
		super(cause);
	}

	
}
