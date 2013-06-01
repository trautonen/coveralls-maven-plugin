package org.eluder.coveralls.maven.plugin;

import java.io.IOException;


public interface CoverageParser {

    public void parse(SourceCallback callback) throws ProcessingException, IOException;
    
}
