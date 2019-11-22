package org.nbone.core.exception;


/**
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2017/6/1.
 */
public class ExceptionInfo {

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
     * 返回操作成功代码 或者错误代码
     */
    private int code;
    /**
     * 返回操作成功消息 或者错误消息
     */
    private String message;

    /**
     * 异常名称
     */
    private  String exceptionName;
    /**
     * http 错误状态码 默认400
     */
    private  int statusCode = 400;

    private String url ;




    public ExceptionInfo() {
    }

    public ExceptionInfo(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ExceptionInfo(int statusCode, int code, String message) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public ExceptionInfo(int code, String message, int statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public ExceptionInfo(int code, String message, Exception ex) {
        this(code,message,null,ex.getClass().getSimpleName());

    }


    public ExceptionInfo(int code, String message, String url) {
       this(code,message,url,null);

    }

    public ExceptionInfo(int code, String message, String url, String exceptionName) {
        this.code = code;
        this.message = message;
        this.url = url;
        this.exceptionName = exceptionName;

    }

    public ExceptionInfo(int code, String message, int statusCode, String exceptionName) {
        this.code = code;
        this.message = message;

        this.statusCode = statusCode;
        this.exceptionName = exceptionName;

    }

    public ExceptionInfo(int code, String message, int statusCode, String url, String exceptionName) {
        this.code = code;
        this.message = message;

        this.statusCode = statusCode;
        this.url = url;

        this.exceptionName = exceptionName;

    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ExceptionInfo requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public ExceptionInfo logId(String logId) {
        this.logId = logId;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
