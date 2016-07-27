package org.nbone.persistence.exception;

/**
 * @author uap
 * @author thinking
 * @version 1.0 
 */
public class PersistenceIllegalArgumentException extends PersistenceBaseRuntimeException {

	private static final long serialVersionUID = -5453903157207910098L;

	public PersistenceIllegalArgumentException() {
		super();
	}

	public PersistenceIllegalArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceIllegalArgumentException(String message) {
		super(message);
	}

	public PersistenceIllegalArgumentException(Throwable cause) {
		super(cause);
	}

}
