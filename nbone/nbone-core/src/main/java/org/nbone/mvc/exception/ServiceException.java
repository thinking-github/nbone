package org.nbone.mvc.exception;

/**
 * Service层公用的Exception. <br>
 * 
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 * @author thinking
 * @version 1.0 
 * @serial  2014-05-08
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -5746880608336665726L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
