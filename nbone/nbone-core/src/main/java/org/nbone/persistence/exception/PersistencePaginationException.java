package org.nbone.persistence.exception;

/**
 * @author uap
 * @author thinking
 * @version 1.0 
 */
public class PersistencePaginationException extends PersistenceBaseRuntimeException  {

	private static final long serialVersionUID = -7701596312811499374L;

	public PersistencePaginationException() {
		super();
	}

	public PersistencePaginationException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistencePaginationException(String message) {
		super(message);
	}

	public PersistencePaginationException(Throwable cause) {
		super(cause);
	}
	
	

}
