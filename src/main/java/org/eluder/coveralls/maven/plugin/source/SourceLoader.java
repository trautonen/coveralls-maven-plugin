package org.eluder.coveralls.maven.plugin.source;

import java.io.IOException;

import org.eluder.coveralls.maven.plugin.domain.Source;

public interface SourceLoader {
    
    Source load(String sourceFile) throws IOException;
    
}
