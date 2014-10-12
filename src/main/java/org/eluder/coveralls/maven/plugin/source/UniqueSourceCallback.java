package org.eluder.coveralls.maven.plugin.source;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.domain.Source;

/**
 * Source callback that tracks passed by source files and provides only unique
 * source files to the delegate. Note that the implementation is not thread
 * safe so the {@link #onSource(org.eluder.coveralls.maven.plugin.domain.Source)}
 * can be called only from single thread concurrently.
 */
public class UniqueSourceCallback implements SourceCallback {
    
    private static final String LINES_SEPARATOR = "#";
    
    private final Set<String> cache = new HashSet<String>();
    private final SourceCallback delegate;

    public UniqueSourceCallback(final SourceCallback delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onSource(final Source source) throws ProcessingException, IOException {
        String key = getKey(source);
        if (!cache.contains(key)) {
            cache.add(key);
            delegate.onSource(source);
        }
    }
    
    private String getKey(final Source source) {
        return source.getFullName() + LINES_SEPARATOR + getRelevantLines(source);
    }
    
    private int getRelevantLines(final Source source) {
        int relevant = 0;
        for (Integer cov : source.getCoverage()) {
            if (cov != null) {
                relevant++;
            }
        }
        return relevant;
    }
}
