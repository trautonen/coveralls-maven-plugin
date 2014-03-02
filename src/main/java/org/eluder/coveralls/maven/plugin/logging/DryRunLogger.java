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

import java.io.File;

import org.apache.maven.plugin.logging.Log;

public class DryRunLogger implements Logger {

    private final boolean dryRun;
    private final File coverallsFile;
    
    public DryRunLogger(final boolean dryRun, final File coverallsFile) {
        if (coverallsFile == null) {
            throw new IllegalArgumentException("coverallsFile must be defined");
        }
        this.dryRun = dryRun;
        this.coverallsFile = coverallsFile;
    }

    @Override
    public Position getPosition() {
        return Position.AFTER;
    }
    
    @Override
    public void log(final Log log) {
        if (dryRun) {
            log.info("Dry run enabled, Coveralls report will NOT be submitted to API");
            log.info(coverallsFile.length() + " bytes of data was recorded in " + coverallsFile.getAbsolutePath());
        }
    }
    
}
