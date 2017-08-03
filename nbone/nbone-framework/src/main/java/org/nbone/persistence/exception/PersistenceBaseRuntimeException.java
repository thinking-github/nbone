package org.nbone.persistence.exception;

import javax.persistence.PersistenceException;

/**
 * @author uap
 * @author thinking
 * @version 1.0 
 * @see javax.persistence.PersistenceException
 */
public class PersistenceBaseRuntimeException extends PersistenceException {

	private static final long serialVersionUID = 74325761858642662L;

	public PersistenceBaseRuntimeException() {
		super();
	}

	public PersistenceBaseRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceBaseRuntimeException(String message) {
		super(message);
	}

	public PersistenceBaseRuntimeException(Throwable cause) {
		super(cause);
	}
	
	

}
