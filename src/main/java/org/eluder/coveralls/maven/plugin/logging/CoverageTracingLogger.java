package org.eluder.coveralls.maven.plugin.logging;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2014 Tapio Rautonen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * %[license]
 */

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
