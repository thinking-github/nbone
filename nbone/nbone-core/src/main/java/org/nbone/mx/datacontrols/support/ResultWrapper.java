package org.nbone.mx.datacontrols.support;

import java.io.Serializable;

/**
 * 用于系统交互时数据包装 <p>
 *  Base ResultWrapper
 *  @author thinking
 *  @since 2013-08-08
 *
 */
public class ResultWrapper  implements Serializable{
	
	/**
	 * <p>
	 */
	private static final long serialVersionUID = -6185220705468269585L;
	/**
	 *  DEFAULT error code 0
	 */
	public static final int DEFAULT_ERRCODE_FAILED = 0;
	/**
	 * DEFAULT SUCCESS code 1
	 */
	public static final int DEFAULT_ERRCODE_SUCCESS = 1;
	
	
	public static final String FLAG_ADD     = "add";
	public static final String FLAG_UPDATE  = "update";
	public static final String FLAG_DELETE  = "delete";
	public static final String FLAG_QUERY   = "query";
	
	
	/**
	 * successful=true表示成功;否则失败
	 */
	private boolean successful;

	/**
	 * 返回的结果集
	 */
	private Object resultValue;
	/**
	 * 提示消息
	 */
	private String resultHint;
	
	/** 
	 *  errCode and message对应，默认当errCode:1时表示成功;否则失败
	 */
	private int errCode;
	/**
	 * 提示消息
	 */
	private String message;
	
	/**
	 * 用于特殊场景标记(save中区分 add/update)
	 */
	private String operationFlag;

	
	
	public ResultWrapper() {
	}
	
	public ResultWrapper(boolean isSuccess, Object data, String resultHint) {
		this(isSuccess, data, resultHint,DEFAULT_ERRCODE_SUCCESS,resultHint);
		this.message = resultHint;
		if(isSuccess){
			this.setErrCode(DEFAULT_ERRCODE_SUCCESS);
		}
	
	}
	
	public ResultWrapper(boolean isSuccess, Object data, String resultHint,int errCode,String message) {
		this.successful = isSuccess;
		this.resultValue = data;
		this.resultHint = resultHint;
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
     * @param exMessage 提示消息
     * @return
     * @see #failedWrappedResult(String, String)
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
	public static ResultWrapper failedWrappedResult(int errCode,String message) {
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
		return new ResultWrapper(false,null, resultHint, errCode, message);
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
	public String getResultHint() {
		return resultHint;
	}
	public void setResultHint(String resultHint) {
		this.resultHint = resultHint;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getOperationFlag() {
		return operationFlag;
	}

	public ResultWrapper setOperationFlag(String operationFlag) {
		this.operationFlag = operationFlag;
		return this;
	}
	
	
	

}
