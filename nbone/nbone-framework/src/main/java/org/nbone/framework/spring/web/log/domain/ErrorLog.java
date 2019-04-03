package org.nbone.framework.spring.web.log.domain;

import java.sql.Timestamp;

/**
 * <p>
 * The database table can be created with the following command:
 * </p>
 * <pre>
 * CREATE TABLE A_ERRORLOG (
 * id INT UNSIGNED AUTO_INCREMENT NOT NULL,
 * requestId  VARCHAR(64) NOT NULL,
 * remoteHost CHAR(15) NOT NULL,
 * userName CHAR(15),
 * timestamp TIMESTAMP NOT NULL,
 * virtualHost VARCHAR(64) NOT NULL,
 * method VARCHAR(8) NOT NULL,
 * query VARCHAR(255) NOT NULL,
 * body  VARCHAR(255) DEFAULT '',
 * errorMessage  VARCHAR(255) DEFAULT '',
 * stackTrace    TEXT ,
 * status SMALLINT UNSIGNED NOT NULL,
 * referer VARCHAR(128),
 * userAgent VARCHAR(128),
 * PRIMARY KEY (id),
 * INDEX (timestamp),
 * INDEX (remoteHost),
 * INDEX (virtualHost),
 * INDEX (query),
 * INDEX (userAgent)
 * );
 * </pre>
 * @author chenyicheng
 * @version 1.0
 * @since 2019-03-28
 */
public class ErrorLog {

    public static String INSERT_SQL = "INSERT INTO %s " +
            "(requestId,remoteHost,userName,timestamp,virtualHost, method, query, body,errorMessage,stackTrace,status,referer, userAgent) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private String tableName = "A_ERRORLOG";


    private String requestId;
    private String remoteHost;
    private String userName;
    private Timestamp timestamp;
    private String virtualHost;
    private String method;
    private String query;

    private String body;
    /**
     * 错误消息
     */
    private String errorMessage;
    /**
     * 错误堆栈内容信息
     */
    private String stackTrace;

    private Integer status;
    private String referer;
    private String userAgent;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Object[] getObjects() {
        Object[] objects = {requestId, remoteHost, userName, timestamp, virtualHost, method, query, body,
                errorMessage, stackTrace, status, referer, userAgent};
        return objects;
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
