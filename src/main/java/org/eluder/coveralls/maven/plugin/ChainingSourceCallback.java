package org.eluder.coveralls.maven.plugin;

import java.io.IOException;

import org.eluder.coveralls.maven.plugin.domain.Source;

public abstract class ChainingSourceCallback implements SourceCallback {

    private final SourceCallback chained;

    public ChainingSourceCallback(final SourceCallback chained) {
        if (chained == null) {
            throw new IllegalArgumentException("Chained source callback must be defined");
        }
        this.chained = chained;
    }
    
    @Override
    public final void onSource(final Source source) throws ProcessingException, IOException {
        onSourceInternal(source);
        chained.onSource(source);
    }
    
    protected abstract void onSourceInternal(final Source source) throws ProcessingException, IOException;
}
