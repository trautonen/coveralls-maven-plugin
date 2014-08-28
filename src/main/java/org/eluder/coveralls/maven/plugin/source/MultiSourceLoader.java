package org.eluder.coveralls.maven.plugin.source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eluder.coveralls.maven.plugin.domain.Source;

public class MultiSourceLoader implements SourceLoader {
    
    private final List<SourceLoader> sourceLoaders = new ArrayList<SourceLoader>();
    
    public MultiSourceLoader add(final SourceLoader sourceLoader) {
        this.sourceLoaders.add(sourceLoader);
        return this;
    }

    @Override
    public Source load(final String sourceFile) throws IOException {
        for (SourceLoader sourceLoader : sourceLoaders) {
            Source source = sourceLoader.load(sourceFile);
            if (source != null) {
                return source;
            }
        }
        throw new IOException("No source found for " + sourceFile);
    }
}
