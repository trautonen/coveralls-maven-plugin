package org.eluder.coveralls.maven.plugin.validation;

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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.eluder.coveralls.maven.plugin.domain.Job;
import org.eluder.coveralls.maven.plugin.validation.ValidationError.Level;

public class JobValidator {

    private final Job job;

    public JobValidator(final Job job) {
        if (job == null) {
            throw new IllegalArgumentException("job must be defined");
        }
        this.job = job;
    }
    
    public ValidationErrors validate() {
        ValidationErrors errors = new ValidationErrors();
        errors.addAll(repoTokenOrTravis());
        errors.addAll(git());
        return errors;
    }
    
    private List<ValidationError> repoTokenOrTravis() {
        if (hasValue(job.getRepoToken())) {
            return Collections.emptyList();
        }
        if (hasValue(job.getServiceName()) && hasValue(job.getServiceJobId())) {
            return Collections.emptyList();
        }
        Level level = (job.isDryRun() ? Level.WARN : Level.ERROR);
        String message = "Either repository token or service with job id must be defined";
        return Arrays.asList(new ValidationError(level, message));
    }
    
    private List<ValidationError> git() {
        if (job.getGit() == null) {
            return Collections.emptyList();
        }
        if (hasValue(job.getGit().getHead().getId())) {
            return Collections.emptyList();
        }
        return Arrays.asList(new ValidationError(Level.ERROR, "Commit id for HEAD must be defined"));
    }
    
    private boolean hasValue(final String value) {
        return StringUtils.isNotBlank(value);
    }
}
