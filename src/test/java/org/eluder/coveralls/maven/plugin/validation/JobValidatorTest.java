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

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.eluder.coveralls.maven.plugin.domain.Git;
import org.eluder.coveralls.maven.plugin.domain.Git.Head;
import org.eluder.coveralls.maven.plugin.domain.Job;
import org.eluder.coveralls.maven.plugin.validation.ValidationError.Level;
import org.junit.Test;

public class JobValidatorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testMissingJob() {
        new JobValidator(null);
    }
    
    @Test
    public void testValidateWithoutRepoTokenOrTravis() {
        ValidationErrors errors = new JobValidator(new Job()).validate();
        assertThat(errors, hasSize(1));
        assertThat(errors.get(0).getLevel(), is(Level.ERROR));
    }
    
    @Test
    public void testValidateWithoutRepoTokenOrTravisForDryRun() {
        ValidationErrors errors = new JobValidator(new Job().withDryRun(true)).validate();
        assertThat(errors, hasSize(1));
        assertThat(errors.get(0).getLevel(), is(Level.WARN));
    }
    
    @Test
    public void testValidateWithInvalidTravis() {
        ValidationErrors errors = new JobValidator(new Job().withServiceName("travis-ci")).validate();
        assertThat(errors, hasSize(1));
        assertThat(errors.get(0).getLevel(), is(Level.ERROR));
    }
    
    @Test
    public void testValidateWithRepoToken() {
        ValidationErrors errors = new JobValidator(new Job().withRepoToken("ad3fg5")).validate();
        assertThat(errors, is(empty()));
    }
    
    @Test
    public void testValidateWithTravis() {
        ValidationErrors errors = new JobValidator(new Job().withServiceName("travis-ci").withServiceJobId("123")).validate();
        assertThat(errors, is(empty()));
    }
    
    @Test
    public void testValidateWithoutGitCommitId() {
        Git git = new Git(new Head(null, null, null, null, null, null), null, null);
        ValidationErrors errors = new JobValidator(new Job().withRepoToken("ad3fg5").withGit(git)).validate();
        assertThat(errors, hasSize(1));
        assertThat(errors.get(0).getLevel(), is(Level.ERROR));
    }
    
    @Test
    public void testValidateWithGit() {
        Git git = new Git(new Head("bc23af5", null, null, null, null, null), null, null);
        ValidationErrors errors = new JobValidator(new Job().withRepoToken("ad3fg5").withGit(git)).validate();
        assertThat(errors, is(empty()));
    }

}
