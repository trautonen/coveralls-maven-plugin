package org.eluder.coveralls.maven.plugin.logging;

import java.io.IOException;

import org.apache.maven.plugin.logging.Log;
import org.eluder.coveralls.maven.plugin.ChainingSourceCallback;
import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.SourceCallback;
import org.eluder.coveralls.maven.plugin.domain.Source;

public class CoverageTracingLogger extends ChainingSourceCallback implements Logger {

    private long files = 0;
    private long lines = 0;
    private long relevant = 0;
    private long covered = 0;
    
    public CoverageTracingLogger(final SourceCallback chained) {
        super(chained);
    }
    
    public long getFiles() {
        return files;
    }
    
    public final long getLines() {
        return lines;
    }
    
    public final long getRelevant() {
        return relevant;
    }
    
    public final long getCovered() {
        return covered;
    }
    
    public final long getMissed() {
        return relevant - covered;
    }
    
    @Override
    public Position getPosition() {
        return Position.AFTER;
    }

    @Override
    public void log(final Log log) {
        log.info("Gathered code coverage metrics for " + getFiles() + " source files with " + getLines() + " lines of code:");
        log.info("- " + getRelevant() + " relevant lines");
        log.info("- " + getCovered() + " covered lines");
        log.info("- " + getMissed() + " missed lines");
    }
    
    @Override
    protected void onSourceInternal(final Source source) throws ProcessingException, IOException {
        if (source.getClassifier() == null) {
            files++;
            lines += source.getCoverage().length;
        }
        for (Integer coverage : source.getCoverage()) {
            if (coverage != null) {
                relevant++;
                if (coverage > 0) {
                    covered++;
                }
            }
        }
    }
}
