package org.nbone.core.exception;


import com.fasterxml.jackson.annotation.JsonAlias;

/**
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2017/6/1.
 */
public class ExceptionInfo {

    public final static String MESSAGE_TEMPLATE = "{\"code\": %s,\"message\": \"%s\",\"timestamp\": %d}";
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
     * 异常详细信息
     */
    private String detailMessage;

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
        this(code,msg,null,null);
    }

    public ExceptionInfo(int code, String message, int statusCode) {
        this(code,message,statusCode,null);
    }

    public ExceptionInfo(int code, String message, Exception ex) {
        this(code,message,null,ex.getClass().getSimpleName());

    }


    public ExceptionInfo(int code, String message, String url) {
       this(code,message,url,null);

    }

    public ExceptionInfo(int code, String message, String url, String exceptionName) {
        this(code,message,400,url,exceptionName);

    }

    public ExceptionInfo(int code, String message, int statusCode, String exceptionName) {
        this(code,message,statusCode,null,exceptionName);
    }

    public ExceptionInfo(int code, String message, int statusCode, String url, String exceptionName) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
        this.url = url;
        this.exception = exceptionName;
        this.timestamp = System.currentTimeMillis();
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

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
    public ExceptionInfo detailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
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
