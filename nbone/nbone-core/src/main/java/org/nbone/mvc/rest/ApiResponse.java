package org.nbone.mvc.rest;

/**
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2018/4/4
 */

public class ApiResponse<T> {

	private static int SUCCESS_CODE   = 0;
	private static int ERROR_CODE     = 1;
	private static int initialized    = 0;

	private final static String SUCCESS_MESSAGE   = "success";
	private final static String ERROR_MESSAGE     = "failed";

	//public final static ApiResponse SUCCESS_RESPONSE = new ApiResponse(SUCCESS_CODE,SUCCESS_MESSAGE);

	/**
	 * 唯一的request id，用于问题定位
	 */
	private String requestId;
	/**
	 *
	 * logId 唯一的log id，用于问题定位
	 */
	private String logId;

	/**
	 * 时间戳
	 */
	private Object timestamp;

	private int code;
	private String status;
	private String path ;
	private String message;
	private T data;




	public ApiResponse() {
	}

	public ApiResponse(int code, String msg) {
		this.code = code;
		this.message = msg;
	}

	public ApiResponse(int code, String msg, T data) {
		this.code = code;
		this.message = msg;
		this.data = data;
	}

	public ApiResponse(int code, String status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

	public ApiResponse(int code, String status, String message, T data) {
		this.code = code;
		this.status = status;
		this.message = message;
		this.data = data;
	}

	/**
     * initialize success and  error code
	 * @param successCode
     * @param errorCode
	 */
	public final static void initialize(int successCode,int errorCode){
		if(initialized >= 1){
			throw  new  IllegalStateException("success and error code initialized");
		}

		SUCCESS_CODE = successCode;
		ERROR_CODE = errorCode;
		//SUCCESS_RESPONSE.setCode(SUCCESS_CODE);
		initialized ++;
	}


	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public ApiResponse<T> requestId(String requestId) {
		this.requestId = requestId;
		return this;
	}

	public Object getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Object timestamp) {
		this.timestamp = timestamp;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public ApiResponse<T> logId(String logId) {
		this.logId = logId;
		return this;
	}


	public static <T> ApiResponse<T> error(String msg) {
        return error(ERROR_CODE, msg != null ? msg : ERROR_MESSAGE);
    }

    public static <T> ApiResponse<T> error(int code, String msg) {
        return new ApiResponse<>(code, msg != null ? msg : ERROR_MESSAGE);
    }


	public static <T> ApiResponse<T> failed(String msg) {
		return error(msg);
	}

	public static <T> ApiResponse<T> failed(int code, String msg) {
		return error(code, msg);
	}


	public static <T> ApiResponse<T> success() {
		return new ApiResponse<>(SUCCESS_CODE, SUCCESS_MESSAGE);
	}

	public static <T> ApiResponse<T> success(String msg) {
		return new ApiResponse<>(SUCCESS_CODE,msg);
	}

	public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }


	public static <T> ApiResponse<T> response(int code, String msg, T data) {
		return new ApiResponse(code, msg, data);
	}
}
