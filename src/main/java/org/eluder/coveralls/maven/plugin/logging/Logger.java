package org.eluder.coveralls.maven.plugin.logging;

import org.apache.maven.plugin.logging.Log;

public interface Logger {

    /**
     * Position of the log output.
     */
    public static enum Position {
        BEFORE, AFTER
    }
    
    
    /**
     * @return the position for log output, before or after the Coveralls data writing
     */
    Position getPosition();
    
    /**
     * Create the log output.
     * 
     * @param log the logger to output
     */
    void log(Log log);
    
}
