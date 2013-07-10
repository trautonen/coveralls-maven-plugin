package org.eluder.coveralls.maven.plugin.validation;

public class ValidationException extends IllegalArgumentException {

    public ValidationException() {
        super();
    }

    public ValidationException(final String s) {
        super(s);
    }

    public ValidationException(final Throwable cause) {
        super(cause);
    }
    
    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
