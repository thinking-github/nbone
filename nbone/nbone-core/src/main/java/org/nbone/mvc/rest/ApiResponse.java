package org.nbone.mvc.rest;


import java.util.Map;

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

	private static String SUCCESS_MESSAGE   = "success";
	private static String ERROR_MESSAGE     = "failed";

	
	private int code;
	private String status;
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
		initialized ++;
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




    public static <T> ApiResponse<T> errorResponse(String msg) {
        return errorResponse(ERROR_CODE, msg != null ? msg : ERROR_MESSAGE);
    }

    public static <T> ApiResponse<T> errorResponse(int code, String msg) {
        return new ApiResponse<>(code, msg != null ? msg : ERROR_MESSAGE);
    }



	public static <T> ApiResponse<T> successResponse() {
		return new ApiResponse<>(SUCCESS_CODE, SUCCESS_MESSAGE);
	}

	public static <T> ApiResponse<T> successResponse(String msg) {
		return new ApiResponse<>(SUCCESS_CODE,msg);
	}

	public static <T> ApiResponse<T> successResponse(T data) {
        return new ApiResponse<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    public static ApiResponse<Map<String, Object>> successResponse(Map<String, Object> data) {
        return new ApiResponse(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }


	public static <T> ApiResponse<T> response(int code, String msg, T data) {
		return new ApiResponse(code, msg, data);
	}
    
    public static ApiResponse<Map<String, Object>> response(int code, String msg, Map<String, Object> data) {
        return new ApiResponse(code, msg, data);
    }
}
