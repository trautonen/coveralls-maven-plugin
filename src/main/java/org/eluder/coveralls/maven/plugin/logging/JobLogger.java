package org.eluder.coveralls.maven.plugin.logging;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 Tapio Rautonen
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

import org.apache.maven.plugin.logging.Log;
import org.eluder.coveralls.maven.plugin.domain.Job;

public class JobLogger implements Logger {

    private final Job job;
    
    public JobLogger(final Job job) {
        if (job == null) {
            throw new IllegalArgumentException("Job must be defined");
        }
        this.job = job;
    }

    @Override
    public Position getPosition() {
        return Position.BEFORE;
    }
    
    @Override
    public void log(final Log log) {
        String starting = "Starting Coveralls job";
        if (job.getServiceName() != null) {
            starting += " for " + job.getServiceName();
            if (job.getServiceJobId() != null) {
                starting += " (" + job.getServiceJobId() + ")";
            }
        }
        log.info(starting);
        
        if (job.getRepoToken() != null) {
            log.info("Using repository token <secret>");
        }
        
        if (job.getGit() != null) {
            String commit = job.getGit().getHead().getId();
            String branch = job.getGit().getBranch();
            log.info("Git commit " + commit.substring(0, 7) + " in " + branch);
        }
    }
    
}
