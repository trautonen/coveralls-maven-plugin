package org.eluder.coveralls.maven.plugin;

import java.io.IOException;

import org.eluder.coveralls.maven.plugin.domain.Source;

public interface SourceCallback {

    void onSource(Source source) throws ProcessingException, IOException;
    
}
