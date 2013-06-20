package org.eluder.coveralls.maven.plugin;

import java.io.IOException;

import org.eluder.coveralls.maven.plugin.domain.Source;

/**
 * Source callback handles parsed source files from coverage reports.
 */
public interface SourceCallback {

    /**
     * Handles a parsed source file.
     * 
     * @param source the source file
     * @throws ProcessingException if further processing of the source fails
     * @throws IOException if an I/O error occurs
     */
    void onSource(Source source) throws ProcessingException, IOException;
    
}
