package org.nbone.core.exception;

import java.io.Serializable;

/**
 * @author thinking
 * @version 1.0
 * @see java.lang.IllegalStateException
 * @since 2019-11-27
 */
public class InvalidStateException extends RuntimeException {


    private Serializable errorCode;

    public InvalidStateException(Serializable errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public InvalidStateException(Serializable errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public InvalidStateException(Serializable errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return "code=[" + errorCode + "],message=[" + super.getMessage() + "]";
    }

}
