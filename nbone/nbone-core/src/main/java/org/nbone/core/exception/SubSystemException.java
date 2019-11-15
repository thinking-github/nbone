package org.nbone.core.exception;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-23
 */
public class SubSystemException extends RuntimeException {

    private String systemName;

    public SubSystemException(String systemName, String message, Throwable cause) {
        super(message, cause);
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
}
