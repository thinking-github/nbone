package org.nbone.mx.datacontrols.support;

import java.io.Serializable;

import static org.nbone.constants.NboneConstants.DEFAULT_ERRCODE_FAILED;
import static org.nbone.constants.NboneConstants.DEFAULT_ERRCODE_SUCCESS;

/**
 *  <p> 系统交互时数据结果集包装
 *  Base ResultWrapper
 *  @author uap
 *  @author thinking
 *  @since 2013-08-08
 */

public class ResultWrapper  implements Serializable{
	
	private static final long serialVersionUID = -6185220705468269585L;
	
	/**
	 * successful=true表示成功;否则失败
	 */
	private boolean successful;

	/**
	 * 返回的结果集
	 */
	private Object resultValue;
	
	private String resultHint;
	
	/** 
	 *  errCode and message对应，默认当errCode:1时表示成功;否则失败
	 */
	private int errCode;
	/**
	 * 提示消息 resultHint
	 */
	private String message;
	
	/**
	 * debug 
	 */
	private boolean debug;
	/**
	 * debug模式系统交互时返回Throwable
	 */
	private Throwable cause;
	
	/**
	 * 用于特殊场景标记(save中区分 add/update)
	 */
	private String operationFlag;

	
	
	public ResultWrapper() {
	}
	
	public ResultWrapper(boolean isSuccess) {
		this(isSuccess,null,"");
	}
	
	public ResultWrapper(boolean isSuccess, Object data, String resultHint) {
		this(isSuccess, data,DEFAULT_ERRCODE_SUCCESS,resultHint);
		this.message = resultHint;
		if(isSuccess){
			this.setErrCode(DEFAULT_ERRCODE_SUCCESS);
		}
	
	}
	
	public ResultWrapper(boolean isSuccess, Object data,int errCode,String message) {
		this.successful = isSuccess;
		this.resultValue = data;
		this.errCode = errCode;
		this.message = message;
		if(isSuccess){
			this.setErrCode(DEFAULT_ERRCODE_SUCCESS);
		}
	}
	
	public static ResultWrapper successResultWraped(Object data,String message) {
		return new ResultWrapper(true, data,message);
	}
	/**
	 * by success Result
	 * @param data
	 * @return
	 */
	public static ResultWrapper successResultWraped(Object data) {
		return new ResultWrapper(true, data, "");
	}
	

	
	public static ResultWrapper successResultWraped(String message) {
		return new ResultWrapper(true, null, message);
	}
	
	
	public static ResultWrapper successResultWraped() {
		return new ResultWrapper(true, null, "");
	}
	

    /**
     * 用作失败返回消息提示
     * @param message 提示消息
     * @return
     * @see #failedResultWraped(String, int, String)
     */
	public static ResultWrapper failedResultWraped(String message) {
		return failedResultWraped(message,DEFAULT_ERRCODE_FAILED,message);
	}
	
	/**
	 * failed Result
	 * @param errCode
	 * @param message
	 * @return
	 */
	public static ResultWrapper failedResultWraped(int errCode,String message) {
		return failedResultWraped(message,errCode,message);
	}
    
	/**
	 * failed Result
	 * @param resultHint
	 * @param errCode
	 * @param message
	 * @return
	 */
	public static ResultWrapper failedResultWraped(String resultHint,int errCode,String message) {
		return new ResultWrapper(false,null, errCode, message);
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T processResultValue(Object resultValue,Class<T> clazz) {
		
		return (T) resultValue;
	}
	
	
	//geter/seter
	public boolean isSuccessful() {
		return successful;
	}
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
	public int getErrCode() {
		return errCode;
	}
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	public Object getResultValue() {
		return resultValue;
	}
	public void setResultValue(Object resultValue) {
		this.resultValue = resultValue;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getResultValue(Class<T> targetClass) {
		return (T) resultValue;
	}
	
	public String getResultHint() {
		return resultHint;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Throwable getCause() {
		return cause;
	}

	public ResultWrapper setCause(Throwable cause) {
		if(debug){
			this.cause = cause;
		}
		return this;
	}

	
	public String getOperationFlag() {
		return operationFlag;
	}

	public ResultWrapper setOperationFlag(String operationFlag) {
		this.operationFlag = operationFlag;
		return this;
	}
	
	
	

}
