package org.nbone.core.exception;


import com.fasterxml.jackson.annotation.JsonAlias;

/**
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2017/6/1.
 */
public class ExceptionInfo {

    public final static String MESSAGE_TEMPLATE = "{\"code\": %s,\"message\": \"%s\"}";
    /**
     * 唯一的request id，用于问题定位
     */
    private String requestId;

    /**
     *
     * logId 唯一的log id，用于问题定位
     */
    private String logId;

    private Object timestamp;
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
    private  String exception;
    /**
     * http 错误状态码 默认400
     */
    @JsonAlias("status")
    private  int statusCode = 400;

    @JsonAlias("path")
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
        this.exception = exceptionName;

    }

    public ExceptionInfo(int code, String message, int statusCode, String exceptionName) {
        this.code = code;
        this.message = message;

        this.statusCode = statusCode;
        this.exception = exceptionName;

    }

    public ExceptionInfo(int code, String message, int statusCode, String url, String exceptionName) {
        this.code = code;
        this.message = message;

        this.statusCode = statusCode;
        this.url = url;

        this.exception = exceptionName;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
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
