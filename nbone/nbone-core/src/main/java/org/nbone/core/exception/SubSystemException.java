package org.nbone.core.exception;

import java.io.Serializable;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-23
 */
public class SubSystemException extends RuntimeException {

    private String systemName;
    private Serializable code;
    private Serializable requestId;

    public SubSystemException(String systemName, String message, Throwable cause) {
        super(message, cause);
        this.systemName = systemName;
    }

    public SubSystemException(String systemName, String message) {
        super(message);
        this.systemName = systemName;
    }

    public SubSystemException(String systemName, Throwable cause) {
        super(cause);
        this.systemName = systemName;
    }

    public SubSystemException(Throwable cause) {
        super(cause);
    }


    @Override
    public String getMessage() {
        if (systemName != null) {
            return systemName + ": " + super.getMessage();
        }
        return this.getClass().getSimpleName() + ": " + super.getMessage();
    }

    public SubSystemException code(Serializable code) {
        this.code = code;
        return this;
    }

    public SubSystemException requestId(Serializable requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getSystemName() {
        return systemName;
    }

    public Serializable getCode() {
        return code;
    }

    public Serializable getRequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append("{");
        sb.append("code=").append(code);
        sb.append(", message=").append(super.getMessage());
        sb.append(", systemName=").append(systemName);

        if (requestId != null) {
            sb.append(", requestId=").append(requestId);
        }
        sb.append('}');
        return sb.toString();
    }
}
