package org.eluder.coveralls.maven.plugin.domain;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.eluder.coveralls.maven.plugin.domain.Git.Head;
import org.eluder.coveralls.maven.plugin.domain.Git.Remote;
import org.junit.Test;

public class JobTest {

    @Test
    public void testGetBranchWithRemote() {
        List<Remote> remotes = Arrays.asList(new Remote("origin", "git@github.com"));
        Git git = new Git(new Head(null, null, null, null, null, null), "master", remotes);
        
        Job job = new Job().withBranch("origin/master").withGit(git);
        assertEquals("master", job.getBranch());
    }
    
    @Test
    public void testGetBranch() {
        Job job = new Job().withBranch("master");
        assertEquals("master", job.getBranch());
    }
}
