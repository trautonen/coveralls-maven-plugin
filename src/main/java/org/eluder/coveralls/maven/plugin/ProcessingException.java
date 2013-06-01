package org.eluder.coveralls.maven.plugin;

public class ProcessingException extends Exception {

    public ProcessingException() {
        super();
    }

    public ProcessingException(final String message) {
        super(message);
    }

    public ProcessingException(final Throwable cause) {
        super(cause);
    }

    public ProcessingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
