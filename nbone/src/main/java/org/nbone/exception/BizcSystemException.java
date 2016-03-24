
package org.nbone.exception;

/**
 * 
 * 业务系统异常处理类<br>
 * 并返回错误码errorCode
 * 
 * @author Thinking
 * @version 1.0 2014-05-08
 * @serial  1.0
 */
public class BizcSystemException extends Exception {

	private static final long serialVersionUID = -629082297231309257L;
	/**
	 * 错误码
	 */
	private final String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}

	public BizcSystemException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

 
	public BizcSystemException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	 


}
