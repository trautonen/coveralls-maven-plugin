package org.eluder.coveralls.maven.plugin;

import java.io.File;
import java.io.IOException;


/**
 * Handles parsing of a coverage report. The implemenation can be statefull, and the same instance
 * should be used only one time to parse a coverage report. Completed source files are passed to
 * the {@link SourceCallback} handler. To maximize performance, the parser should use streaming.
 */
public interface CoverageParser {

    /**
     * Parses a coverage report. Parsed source files are passed to the callback handler. This
     * method should be called only once per instance.
     * 
     * @param callback the source callback handler
     * @throws ProcessingException if processing of the coverage report fails
     * @throws IOException if an I/O error occurs
     */
    public void parse(SourceCallback callback) throws ProcessingException, IOException;
    
    /**
     * @return the coverage report file under processing
     */
    public File getCoverageFile();
}
