package org.eluder.coveralls.maven.plugin.domain;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

public class GitRepositoryTest {

    @Test
    public void testLoad() throws Exception {
        Git git = new GitRepository(new File(getClass().getResource("/").toURI())).load();
        assertNotNull(git.getHead().getId());
    }
}
