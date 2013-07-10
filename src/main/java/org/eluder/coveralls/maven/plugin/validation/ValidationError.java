package org.eluder.coveralls.maven.plugin.validation;

public final class ValidationError {

    public static enum Level {
        WARN, ERROR
    };
    
    private final Level level;
    private final String message;
    
    public ValidationError(final Level level, final String message) {
        if (level == null) {
            throw new IllegalArgumentException("level must be defined");
        }
        if (message == null) {
            throw new IllegalArgumentException("message must be defined");
        }
        this.level = level;
        this.message = message;
    }

    public Level getLevel() {
        return level;
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return level + ": " + message;
    }
}
