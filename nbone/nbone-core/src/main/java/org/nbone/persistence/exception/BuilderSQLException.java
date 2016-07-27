package org.nbone.persistence.exception;

/**
 * @author thinking
 * @version 1.0 
 */
public class BuilderSQLException extends PersistenceBaseRuntimeException {

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
