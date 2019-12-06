package org.nbone.core.exception;

import java.io.Serializable;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-23
 */
public class ModuleSystemException extends RuntimeException {

    private String moduleName;
    private Serializable code;
    private Serializable requestId;

    public ModuleSystemException(String moduleName, String message, Throwable cause) {
        super(message, cause);
        this.moduleName = moduleName;
    }

    public ModuleSystemException(String moduleName, String message) {
        super(message);
        this.moduleName = moduleName;
    }

    public ModuleSystemException(String moduleName, Throwable cause) {
        super(cause);
        this.moduleName = moduleName;
    }

    public ModuleSystemException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        if (moduleName != null) {
            return moduleName + ": " + super.getMessage();
        }
        return this.getClass().getSimpleName() + ": " + super.getMessage();
    }

    public ModuleSystemException code(Serializable code) {
        this.code = code;
        return this;
    }

    public ModuleSystemException requestId(Serializable requestId) {
        this.requestId = requestId;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append("{");
        sb.append("code=").append(code);
        sb.append(", message=").append(super.getMessage());
        sb.append(", moduleName=").append(moduleName);

        if (requestId != null) {
            sb.append(", requestId=").append(requestId);
        }
        sb.append('}');
        return sb.toString();
    }


}
