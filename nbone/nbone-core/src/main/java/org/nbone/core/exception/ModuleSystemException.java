package org.nbone.core.exception;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-23
 */
public class ModuleSystemException extends RuntimeException {

    private String moduleName;

    public ModuleSystemException(String moduleName, String message, Throwable cause) {
        super(message, cause);
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


}
