package org.eluder.coveralls.maven.plugin.domain;

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

import org.codehaus.plexus.util.StringUtils;
import org.eluder.coveralls.maven.plugin.service.Travis;

public class JobValidator {

    private final Job job;

    public JobValidator(final Job job) {
        if (job == null) {
            throw new IllegalArgumentException("Job must be defined");
        }
        this.job = job;
    }
    
    public void validate() {
        repoTokenOrTravis();
        git();
    }
    
    private void repoTokenOrTravis() {
        if (hasValue(job.getRepoToken())) {
            return;
        }
        if (new Travis().isSelected(job.getServiceName()) && hasValue(job.getServiceJobId())) {
            return;
        }
        throw new IllegalArgumentException("Either repository token or travis service with job id must be defined");
    }
    
    private void git() {
        if (job.getGit() == null) {
            return;
        }
        if (hasValue(job.getGit().getHead().getId())) {
            return;
        }
        throw new IllegalArgumentException("Commit id for HEAD must be defined");
    }
    
    private boolean hasValue(final String value) {
        return StringUtils.isNotBlank(value);
    }
}
