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

import org.eluder.coveralls.maven.plugin.domain.Git.Head;
import org.junit.Test;

public class JobValidatorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testMissingJob() {
        new JobValidator(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateWithoutRepoTokenOrTravis() {
        new JobValidator(new Job()).validate();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateWithInvalidTravis() {
        new JobValidator(new Job().withServiceName("travis-ci")).validate();
    }
    
    @Test
    public void testValidateWithRepoToken() {
        new JobValidator(new Job().withRepoToken("ad3fg5")).validate();
    }
    
    @Test
    public void testValidateWithTravis() {
        new JobValidator(new Job().withServiceName("travis-ci").withServiceJobId("123")).validate();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateWithoutGitCommitId() {
        Git git = new Git(new Head(null, null, null, null, null, null), null, null);
        new JobValidator(new Job().withRepoToken("ad3fg5").withGit(git)).validate();
    }
    
    @Test
    public void testValidateWithoutGit() {
        new JobValidator(new Job().withRepoToken("ad3fg5")).validate();
    }
    
    @Test
    public void testValidateWithGit() {
        Git git = new Git(new Head("bc23af5", null, null, null, null, null), null, null);
        new JobValidator(new Job().withRepoToken("ad3fg5").withGit(git)).validate();
    }

}
