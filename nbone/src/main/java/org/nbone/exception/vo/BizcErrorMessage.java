package org.nbone.exception.vo;

/**
 * 业务系统处理异常代码映射消息
 * @author Thinking
 * @version 1.0  2014-05-08 
 *
 */
public class BizcErrorMessage {
	
	
	private String code;
	private String message;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public BizcErrorMessage(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public BizcErrorMessage(String code) {
		this.code = code;
	}
	
	public BizcErrorMessage() {
	}
	
}
