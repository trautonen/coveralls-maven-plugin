package org.eluder.coveralls.maven.plugin.domain;

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
