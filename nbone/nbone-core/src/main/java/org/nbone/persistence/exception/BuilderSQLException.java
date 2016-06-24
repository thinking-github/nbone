package org.nbone.persistence.exception;

import javax.persistence.PersistenceException;

/**
 * @author thinking
 * @version 1.0 
 * @see javax.persistence.PersistenceException
 */
public class BuilderSQLException extends PersistenceException {

	private static final long serialVersionUID = 5293096565896569399L;

	public BuilderSQLException() {
		super();
	}

	public BuilderSQLException(String message, Throwable cause) {
		super(message, cause);
	}

	public BuilderSQLException(String message) {
		super(message);
	}

	public BuilderSQLException(Throwable cause) {
		super(cause);
	}

}
