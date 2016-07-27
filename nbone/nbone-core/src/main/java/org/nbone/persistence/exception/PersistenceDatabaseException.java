package org.nbone.persistence.exception;

/**
 * @author uap
 * @author thinking
 * @version 1.0 
 */
public class PersistenceDatabaseException extends PersistenceBaseRuntimeException {

	private static final long serialVersionUID = -2684510781888256961L;

	public PersistenceDatabaseException() {
		super();
	}

	public PersistenceDatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceDatabaseException(String message) {
		super(message);
	}

	public PersistenceDatabaseException(Throwable cause) {
		super(cause);
	}
	
	

}
