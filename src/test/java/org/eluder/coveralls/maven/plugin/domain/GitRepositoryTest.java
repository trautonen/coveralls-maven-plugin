package org.eluder.coveralls.maven.plugin.domain;

import static org.junit.Assert.assertNotNull;

import org.eluder.coveralls.maven.plugin.util.TestIoUtil;
import org.junit.Test;

public class GitRepositoryTest {

    /**
     * This tests assumes that the project resides in git repository.
     */
    @Test
    public void testLoad() throws Exception {
        Git git = new GitRepository(TestIoUtil.getFile("/")).load();
        assertNotNull(git.getHead().getId());
    }
}
