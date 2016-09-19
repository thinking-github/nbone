package org.nbone.core.exception;

/**
 * 循环引用异常
 * @author thinking
 * @since 1.0.2 
 *
 */
public class CircleReferenceException extends RuntimeException {

	private static final long serialVersionUID = 7313189627007316463L;
	
	public CircleReferenceException() {
		super();
	}

	public CircleReferenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CircleReferenceException(String message) {
		super(message);
	}

	public CircleReferenceException(Throwable cause) {
		super(cause);
	}

}
